package com.example.postapi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


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
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setAlpha(1);
                    //en
                } else {
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
        });


    }


    private void addItemToSheet() {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Adding Item", "Please Wait");
        String username = txtName.getText().toString().trim();
        String contactNo = txtContactNo.getText().toString().trim();
        String emailAddr = txtEmailAddr.getText().toString().trim();
        String date = EdDate.getText().toString().trim();
        String time = EdTime.getText().toString().trim();
        String remarks = EdRemarks.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzbX8lzKa1Wq02iK7GMTkuQiNl6tplTzoHzsOOMmwMenrR6ay1j-2wGmTXD28gQxw4O/exec", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "addItemToSheet: " + response.toString());

            }
        }, error -> {

            dialog.dismiss();
            Log.d(TAG, "addItemToSheet: " + error.toString());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("action", "addItem");
                params.put("username", username);
                params.put("contactNo", contactNo);
                params.put("emailAddr", emailAddr);
                params.put("date", date);
                params.put("time", time);
                params.put("remarks", remarks);
                return super.getParams();
            }
        };
        int timeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy((retryPolicy));

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);
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
                                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
                                if ((txtEmailAddr.getText().toString().matches(emailPattern))) {
                                    Toast.makeText(MainActivity.this, "All Good", Toast.LENGTH_SHORT).show();
                                    addItemToSheet();

                                } else
                                    txtEmailAddr.setError("Invalid Email");


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
                    txtEmailAddr.setError("check your E-mail Please");
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
    // ...


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}