package com.qboxus.hugme.All_Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Login_Phone_A extends AppCompatActivity {

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;
    EditText phoneText,digit1,digit2,digit3,digit4,digit5,digit6;
    TextView countrytxt,countrycodetxt,sendtotxt;
    RelativeLayout select_country;
    ViewFlipper viewFlipper;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        fbAuth = FirebaseAuth.getInstance();

        fbAuth.setLanguageCode("en");


        phoneText=findViewById(R.id.phonetxt);

        select_country=findViewById(R.id.select_country);
        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opencountry();
            }
        });

        countrytxt    =findViewById(R.id.countrytxt);
        countrycodetxt=findViewById(R.id.countrycodetxt);

        sendtotxt=findViewById(R.id.sendtotxt);

        viewFlipper=findViewById(R.id.viewfillper);

        codefill();

    }

    //message code fill in edittext and change focus in android
    public void codefill(){

        digit1=findViewById(R.id.digitone);
        digit2=findViewById(R.id.digittwo);
        digit3=findViewById(R.id.digitthree);
        digit4=findViewById(R.id.digitfour);
        digit5=findViewById(R.id.digitfive);
        digit6=findViewById(R.id.digitsix);

        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(digit1.getText().toString().length()==0){
                    digit2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(digit2.getText().toString().length()==0){
                    digit3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(digit3.getText().toString().length()==0){
                    digit4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(digit4.getText().toString().length()==0){
                    digit5.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(digit5.getText().toString().length()==0){
                    digit6.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    String country_iso_code="US";
    @SuppressLint("WrongConstant")
    public void Opencountry(){

        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                countrytxt.setText(name);
                countrycodetxt.setText(dialCode);
                picker.dismiss();
                country_iso_code=code;
            }
        });
        picker.setStyle(R.style.countrypicker_style,R.style.countrypicker_style);
        picker.show(getSupportFragmentManager(), "Select Country");

    }

    public void Nextbtn(View view) {

        phoneNumber=countrycodetxt.getText().toString()+phoneText.getText().toString();
        Send_Number_tofirebase(phoneNumber);

    }


    public void Send_Number_tofirebase(String phoneNumber){
        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {
       Functions.Show_loader(this,false,true);
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                           Functions.cancel_loader();
                            signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Functions.cancel_loader();

                        Log.d("responce",e.toString());
                        Toast.makeText(Login_Phone_A.this, "Enter Correct Number.", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                        Functions.cancel_loader();

                        phoneVerificationId = verificationId;
                        resendToken = token;
                        sendtotxt.setText("Send to ( "+phoneNumber+" )");
                        viewFlipper.setInAnimation(Login_Phone_A.this, R.anim.in_from_right);
                        viewFlipper.setOutAnimation(Login_Phone_A.this, R.anim.out_to_left);
                        viewFlipper.setDisplayedChild(1);

                    }
                };
    }


    public void verifyCode(View view) {
    String code=""+digit1.getText().toString()+digit2.getText().toString()+digit3.getText().toString()+digit4.getText().toString()+digit5.getText().toString()+digit6.getText().toString();
    if(!code.equals("")){
       Functions.Show_loader(this,false,true);
       PhoneAuthCredential credential =
               PhoneAuthProvider.getCredential(phoneVerificationId, code);
       signInWithPhoneAuthCredential(credential);
    }else {
        Toast.makeText(this, "Enter the Correct varification Code", Toast.LENGTH_SHORT).show();
    }


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // get the user info to know that user is already login or not
                            Get_User_info();

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {

                                Functions.cancel_loader();
                            }
                        }
                    }
                });
    }


    public void resendCode(View view) {

        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }


    public void Goback_1(View view) {
        finish();
    }

    public void Goback(View view) {
        viewFlipper.setInAnimation(Login_Phone_A.this, R.anim.in_from_left);
        viewFlipper.setOutAnimation(Login_Phone_A.this, R.anim.out_to_right);
        viewFlipper.setDisplayedChild(0);
    }



    private void Get_User_info(){
        final String phone_no=phoneNumber.replace("+","");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", phone_no);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                this,
                Api_Links.getUserInfo,
                parameters,
                new CallBack() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try{
                            JSONObject response=new JSONObject(resp);


                            if(response.getString("code").equals("200")) {


                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                SharedPrefrence.save_string(Login_Phone_A.this,user_info_obj.toString(),
                                        SharedPrefrence.u_login_detail);


                                SharedPrefrence.save_string(Login_Phone_A.this,phone_no,
                                        SharedPrefrence.u_id
                                );

                                SharedPrefrence.remove_value_of_key(Login_Phone_A.this,
                                         SharedPrefrence.share_social_info);

                                enable_location();


                            }else{

                                Intent intent=new Intent(Login_Phone_A.this, Get_User_Info_A.class);
                                intent.putExtra("id",phone_no);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                finish();

                            }

                        }catch (Exception b){
                            Functions.toast_msg(Login_Phone_A.this,"" + b.toString());
                        }

                    }
                }
        );






    }


    private void enable_location() {
        // will move the user for enable location screen
        startActivity(new Intent(this, Enable_location_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finishAffinity();

    }



}
