package com.example.postapi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText txt_1, txt_2;
    Button btn_1, btnSubmit;
    CheckBox checkBox;

    ProgressBar loadingPB;

    TextView responseTV;

    TextInputEditText txtName, txtContactNo, txtEmailAddr, EdDate, EdTime, EdRemarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);


        initViews();


        ClickListener();


    }

    private void ClickListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: "+isChecked);
                if(isChecked){
                    btnSubmit.setEnabled(true);
                    btnSubmit.setAlpha(1);
                    //en
                }else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setAlpha(0.5f);
                }
            }
        });

        btnSubmit.setOnClickListener(v -> {

            if (checkBox.isChecked()) {
                validateTxt();
            } else {
                Toast.makeText(this, "Tick the checkbox", Toast.LENGTH_SHORT).show();
            }

            return;
        });
    }

    private void initViews() {
        btnSubmit = findViewById(R.id.btnSubmit);
        txtName = findViewById(R.id.txtName);
        txtContactNo = findViewById(R.id.txtContactNo);
        txtEmailAddr = findViewById(R.id.txtEmailAddr);
        EdDate = findViewById(R.id.EdDate);
        EdTime = findViewById(R.id.EdTime);
        EdRemarks = findViewById(R.id.EdRemarks);
        checkBox = findViewById(R.id.checkBox);
    }

    private void validateTxt() {


        if (!txtName.getText().toString().equals("") || txtName.getText().toString() == null) {

            if (!txtContactNo.getText().toString().equals("") || txtContactNo.getText().toString() == null) {

                if (!txtEmailAddr.getText().toString().equals("") || txtEmailAddr.getText().toString().equals(null)) {

                    if (!EdDate.getText().toString().equals("") || EdDate.getText().toString().equals(null)) {

                        if (!EdTime.getText().toString().equals("") || EdTime.getText().toString().equals(null)) {

                            if (!EdRemarks.getText().toString().equals("") || EdRemarks.getText().toString().equals(null)) {


                                Toast.makeText(MainActivity.this, "All Good", Toast.LENGTH_SHORT).show();


                            } else {
                                EdRemarks.setError("empty not allowed");
                            }


                        } else {
                            EdTime.setError("empty not allowed");
                        }

                    } else {
                        EdDate.setError("empty not allowed");
                    }


                } else {
                    txtEmailAddr.setError("empty not allowed");
                }

            } else {
                txtContactNo.setError("empty not allowed");
            }

        } else {
            txtName.setError("empty not allowed");
        }


    }

    private void postData(String name, String job) {
//        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://reqres.in/api/").addConverterFactory(GsonConverterFactory.create()).build();

        API api = retrofit.create(API.class);

        DataModel dataModel = new DataModel(name, job);

        Call<ResponseModel> call = api.createPost(dataModel);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(MainActivity.this, response.body().getCreatedAt() + "\n" + response.body().getId(), Toast.LENGTH_SHORT).show();


                txt_1.setText("");
                txt_2.setText("");

                ResponseModel response_fromAPI = response.body();

//                String responseString = "Response Code : " + response.code() + "\nName : " + response_fromAPI.getName() + "\n" + "Job : " + response_fromAPI.getJob();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });

    }
}