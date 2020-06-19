package com.qboxus.hugme.All_Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.theartofdev.edmodo.cropper.CropImage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Get_User_Info_A extends AppCompatActivity {

    ImageView profile_image;
    TextView first_name,last_name;

    DatabaseReference rootref;



    ImageButton edit_profile_image;
    EditText dateofbrith_edit;
    RadioButton male_btn,female_btn;
    byte [] image_byte_array;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);




        rootref= FirebaseDatabase.getInstance().getReference();


        profile_image=findViewById(R.id.profile_image);

        edit_profile_image=findViewById(R.id.edit_profile_image);
        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);


        dateofbrith_edit=findViewById(R.id.dateofbirth_edit);

        male_btn=findViewById(R.id.male_btn);
        female_btn=findViewById(R.id.female_btn);


        dateofbrith_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Functions.Opendate_picker(Get_User_Info_A.this,dateofbrith_edit);

            }
        });



        findViewById(R.id.nextbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f_name= first_name.getText().toString();
                String l_name=last_name.getText().toString();
                String date_of_birth=dateofbrith_edit.getText().toString();

                if(image_byte_array==null){
                    Toast.makeText(Get_User_Info_A.this, "Select Image", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(f_name)){

                    Toast.makeText(Get_User_Info_A.this, "Please enter your First Name", Toast.LENGTH_SHORT).show();

                } else if(TextUtils.isEmpty(l_name)){

                    Toast.makeText(Get_User_Info_A.this, "Please enter your Last Name", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(date_of_birth)){

                    Toast.makeText(Get_User_Info_A.this, "Please enter your Date of Birth", Toast.LENGTH_SHORT).show();
                }
                else {

                    Save_info();
                }

            }
        });




        Intent intent=getIntent();
        if(intent.hasExtra("id")) {
            user_id = intent.getExtras().getString("id");
            user_id = user_id.replace("+", "");
        }
        if(intent.hasExtra("fname")){
            first_name.setText(intent.getExtras().getString("fname"));
        }

        if(intent.hasExtra("lname")){
            last_name.setText(intent.getExtras().getString("lname"));
        }


    }



    // open the gallary to select and upload the picture
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    // on select the bottom method will reture the uri of that image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){

        if (requestCode == 2) {

            Uri selectedImage = data.getData();
            beginCrop(selectedImage);
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCrop(result.getUri());
        }

        }

    }



    private void beginCrop(Uri source) {

        CropImage.activity(source)
                .start(this);
        }


    private void handleCrop(Uri userimageuri) {

            InputStream imageStream = null;
            try {
                imageStream =getContentResolver().openInputStream(userimageuri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path=userimageuri.getPath();
            Matrix matrix = new Matrix();
            android.media.ExifInterface exif = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    exif = new android.media.ExifInterface(path);
                    int orientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image_byte_array = out.toByteArray();


            profile_image.setImageBitmap(null);
            profile_image.setImageURI(null);
            profile_image.setImageBitmap(rotatedBitmap);




    }



    // this method is used to store the selected image into database
    public void Save_info(){
        Functions.Show_loader(this,false,false);
        // first we upload image after upload then get the picture url and save the group data in database
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filelocation = storageReference.child("User_image")
                .child(user_id + ".jpg");
        filelocation.putBytes(image_byte_array).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                Call_Api_For_Signup(user_id,
                        first_name.getText().toString(),
                        last_name.getText().toString(),
                        dateofbrith_edit.getText().toString()
                        ,taskSnapshot.getDownloadUrl().toString());

            }});


    }



    // this method will store the info of user to  database
    private void Call_Api_For_Signup(String user_id,
                                     String f_name, String l_name,
                                     String birthday, String picture) {

        f_name=f_name.replaceAll("\\W+","");
        l_name=l_name.replaceAll("\\W+","");

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);
            parameters.put("first_name",f_name);
            parameters.put("last_name", l_name);
            parameters.put("birthday", birthday);

            if(male_btn.isChecked()){
                parameters.put("gender","Male");

            }else{
                parameters.put("gender","Female");
            }
            parameters.put("image1",picture);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{

            ApiRequest.Call_Api(
                    this,
                    Api_Links.API_SignUp,
                    parameters,
                    new CallBack() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void Get_Response(String requestType, String resp) {

                            Functions.cancel_loader();
                            try{
                                JSONObject response=new JSONObject(resp);

                                if(response.getString("code").equals("200")) {

                                    JSONArray msg_obj = response.getJSONArray("msg");
                                    JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                    SharedPrefrence.save_string(Get_User_Info_A.this, user_info_obj.toString(),
                                             SharedPrefrence.u_login_detail);


                                    SharedPrefrence.save_string(Get_User_Info_A.this,  user_info_obj.optString("fb_id"),SharedPrefrence.u_id
                                    );



                                    SharedPrefrence.remove_value_of_key(Get_User_Info_A.this,
                                             SharedPrefrence.share_social_info);




                                    enable_location();

                                }


                            }catch (Exception b){
                                Functions.cancel_loader();
                            }
                        }
                    }
            );
        }catch (Exception b){
            Functions.cancel_loader();
        }

    }




    private void enable_location() {

        startActivity(new Intent(this, Enable_location_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finishAffinity();

    }



    public void Goback(View view) {
        finish();
    }


}
