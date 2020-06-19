package com.qboxus.hugme.All_Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qboxus.hugme.Code_Classes.Functions;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.qboxus.hugme.All_Adapters.Edit_prof_Adapter;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

import static com.qboxus.hugme.Code_Classes.Variables.user_gender;
import static com.qboxus.hugme.All_Fragments.Profile_F.mCircleView;

public class Edit_profile_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv,iv1;
    RecyclerView recyclerView;
    Edit_prof_Adapter adapter;
    RelativeLayout basic_info,living,children,smoking,drinking,relation,sex,appearance;

    FirebaseStorage storage;
    StorageReference storageReference;
    public static List<String> basic_info_titles = new ArrayList<>();


    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    public static final int REQUEST_CODE_GALLERY = 1;

    public static Activity activity = null;

    List<String> list_user_img_from_API = new ArrayList<>();

    EditText about_me;
    ImageView ic_tick;
    Context context;
    TextView TV3_id_living, TV4_id_children, TV5_id_smoking, TV6_id_drinking, TV7_id_relationship, TV8_id_sexuality;

    public static TextView user_living, user_children, user_smoking, user_drinking, user_relationship, user_sex;
    TextView TV_basic_info;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = Edit_profile_A.this;
        activity = Edit_profile_A.this;
        tb = (Toolbar) findViewById(R.id.edit_prof_TB_id);
        iv = (ImageView) tb.findViewById(R.id.edit_prof_back_id);
        iv1 = (ImageView) tb.findViewById(R.id.edit_prof_eye_id);
        about_me = findViewById(R.id.about_me);
        ic_tick = findViewById(R.id.ic_tick);
        activity = this;

        // TODO: Load FB Full Page Ads :-S
        Functions.display_fb_ad(context);


        final String about = Functions.get_info(context,"about_me");
        final String basic = Functions.get_info(context,"first_name") + ", " + Functions.get_info(context,"age");

        about_me.setText("" + about);
        TV_basic_info = findViewById(R.id.TV_basic_info);
        TV_basic_info.setText("" + basic);


        user_living = findViewById(R.id.user_living);
        user_children = findViewById(R.id.user_children);
        user_smoking = findViewById(R.id.user_smoking);
        user_drinking = findViewById(R.id.user_drinking);
        user_relationship = findViewById(R.id.user_relationship);
        user_sex = findViewById(R.id.user_sex);



        String living_1 = Functions.get_info(context,"living");
        String children_1 = Functions.get_info(context,"children");
        String smoking_1 = Functions.get_info(context,"smoking");
        String drinking_1 = Functions.get_info(context,"drinking");
        String relationship_1 = Functions.get_info(context,"relationship");
        String sexuality_1 = Functions.get_info(context,"sexuality");

            if(living_1.equals("0") || living_1.equals("")){
                living_1 = "";
            }

        if(children_1.equals("0") || children_1.equals("")){
            children_1 = "";
        }

        if(smoking_1.equals("0") || smoking_1.equals("")){
            smoking_1 = "";
        }

        if(drinking_1.equals("0") || drinking_1.equals("")){
            drinking_1 = "";
        }

        if(relationship_1.equals("0") || relationship_1.equals("")){
            relationship_1 = "";
        }

        if(sexuality_1.equals("0") || sexuality_1.equals("")){
            sexuality_1 = "";
        }


        user_living.setText("" + living_1);
        user_children.setText("" + children_1);
        user_smoking.setText("" + smoking_1);
        user_drinking.setText("" + drinking_1);
        user_relationship.setText("" + relationship_1);
        user_sex.setText("" + sexuality_1);



        basic_info = (RelativeLayout) findViewById(R.id.RL1_id);
        living = (RelativeLayout) findViewById(R.id.RL3_id);
        children = (RelativeLayout) findViewById(R.id.RL4_id);
        smoking = (RelativeLayout) findViewById(R.id.RL5_id);
        drinking = (RelativeLayout) findViewById(R.id.RL6_id);
        relation = (RelativeLayout) findViewById(R.id.RL7_id);
        sex = (RelativeLayout) findViewById(R.id.RL8_id);
        appearance = (RelativeLayout) findViewById(R.id.RL9_id);

        // init TextViews
        TV3_id_living = findViewById(R.id.TV3_id);
        TV4_id_children = findViewById(R.id.TV4_id);
        TV5_id_smoking = findViewById(R.id.TV5_id);
        TV6_id_drinking = findViewById(R.id.TV6_id);
        TV7_id_relationship = findViewById(R.id.TV7_id);
        TV8_id_sexuality = findViewById(R.id.TV8_id);


        // init Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        recyclerView = (RecyclerView) findViewById(R.id.edit_prof_RL_id);
        adapter = new Edit_prof_Adapter(context, activity, list_user_img_from_API, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

                if(view.getId()==R.id.cancel){
                    remove_image(postion);
                }
                else if(view.getId()==R.id.RL_add_img){
                    selectImage();
                }

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        iv.setOnClickListener(this);
        iv1.setOnClickListener(this);

        basic_info.setOnClickListener(this);
        living.setOnClickListener(this);
        children.setOnClickListener(this);
        smoking.setOnClickListener(this);
        drinking.setOnClickListener(this);
        relation.setOnClickListener(this);
        sex.setOnClickListener(this);
        appearance.setOnClickListener(this);
        ic_tick.setOnClickListener(this);
        final String user_id = Functions.get_info(context,"fb_id");
        getuser_info(user_id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ic_tick:
                create_Json_for_API();

            break;
            case R.id.edit_prof_back_id:
                mCircleView.setValue(SharedPrefrence.calculate_complete_profile(context));
                finish();
                break;
            case R.id.edit_prof_eye_id:
                startActivity(new Intent(Edit_profile_A.this, View_profile_A.class));
                break;
            case R.id.RL1_id:

                Intent myIntent_gender = new Intent(Edit_profile_A.this, Basic_info_A.class);
                myIntent_gender.putExtra("gender", "" + user_gender); //Optional parameters
                startActivity(myIntent_gender);

                break;
            case R.id.RL3_id:
                Intent myIntent = new Intent(Edit_profile_A.this, Living_A.class);
                myIntent.putExtra("living", "" + user_living.getText()); //Optional parameters
                startActivity(myIntent);


                break;
            case R.id.RL4_id:
                Intent myIntent_child = new Intent(Edit_profile_A.this, Children_A.class);
                myIntent_child.putExtra("child", "" + user_children.getText()); //Optional parameters
                startActivity(myIntent_child);

                break;
            case R.id.RL5_id:

                Intent myIntent_smoking = new Intent(Edit_profile_A.this, Smoking_A.class);
                myIntent_smoking.putExtra("smoking", "" + user_smoking.getText()); //Optional parameters
                startActivity(myIntent_smoking);

                 break;
            case R.id.RL6_id:

                Intent myIntent_drink = new Intent(Edit_profile_A.this, Drinking_A.class);
                myIntent_drink.putExtra("drinking", "" + user_drinking.getText()); //Optional parameters
                startActivity(myIntent_drink);


                break;
            case R.id.RL7_id:

                Intent myIntent_relation = new Intent(Edit_profile_A.this, Relationship_A.class);
                myIntent_relation.putExtra("relation", "" + user_relationship.getText()); //Optional parameters
                startActivity(myIntent_relation);

                break;
            case R.id.RL8_id:

                Intent myIntent_sexx = new Intent(Edit_profile_A.this, Sexuality_A.class);
                myIntent_sexx.putExtra("sexuality", "" + user_sex.getText()); //Optional parameters
                startActivity(myIntent_sexx);
                break;

            case R.id.RL9_id:
                startActivity(new Intent(Edit_profile_A.this, Appearance_A.class));
                break;

        }
    }



public void remove_image(int position){
    final String user_id = Functions.get_info(context,"fb_id");
    Functions.delete_image( context,"" + list_user_img_from_API.get(position), user_id );

    list_user_img_from_API.remove(position);
    adapter.notifyDataSetChanged();

}



    // Update Profile_F
    public static void update_profile (final Context context, final JSONObject parameters){

        Functions.Show_loader(context,false,false);

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.edit_profile,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {

                        try {
                            Functions.cancel_loader();
                            JSONObject response = new JSONObject(resp);

                            if (response.getString("code").equals("200")) {


                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                Functions.Log_d_msg(context,"Profile_F Updated Successfully.");


                                SharedPrefrence.save_string(context,"" + user_info_obj.toString(),
                                        "" + SharedPrefrence.u_login_detail);

                               activity.finish();

                               mCircleView.setValue(SharedPrefrence.calculate_complete_profile(context));


                            } else {
                                Functions.cancel_loader();

                            }


                        } catch (Exception b) {
                            Functions.Log_d_msg(context, "Error " + b.toString());
                            Functions.cancel_loader();

                        }


                    }
                }


        );



    }



    public static void get_val_from_radio_box (TextView title, String value, Context context){


        if(basic_info_titles.indexOf(title.getText().toString()) != -1){

        }else{

            basic_info_titles.add(title.getText().toString());
        }
        SharedPrefrence.save_string(context,
                "" +  value,
                "" + title.getText().toString()
        );

    }



    public void create_Json_for_API (){

        final String user_id = Functions.get_info(context,"fb_id");


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", "" + user_id);
            parameters.put("about_me", "" + about_me.getText().toString().replaceAll("[-+.^:,']",""));
            parameters.put("job_title", "job_title");
            parameters.put("company", "company");
            parameters.put("school", "school");
            parameters.put("living", "" + Variables.Var_living.replaceAll("[-+.^:,']",""));
            parameters.put("children", "" + Variables.Var_children.replaceAll("[-+.^:,']",""));
            parameters.put("smoking", "" + Variables.Var_smoking.replaceAll("[-+.^:,']",""));
            parameters.put("drinking", "" + Variables.Var_drinking.replaceAll("[-+.^:,']",""));
            parameters.put("relationship", "" + Variables.Var_relationship.replaceAll("[-+.^:,']",""));
            parameters.put("sexuality", "" + Variables.Var_sexuality.replaceAll("[-+.^:,']",""));

            parameters.put("image1", "" +  Functions.get_info(context,"image1"));
            parameters.put("image2", "" +  Functions.get_info(context,"image2"));
            parameters.put("image3", "" +  Functions.get_info(context,"image3"));
            parameters.put("image4", "" +  Functions.get_info(context,"image4"));
            parameters.put("image5", "" +  Functions.get_info(context,"image5"));
            parameters.put("image6", "" +  Functions.get_info(context,"image6"));

            parameters.put("gender", "" + user_gender.replaceAll("[-+.^:,']",""));
            parameters.put("birthday", "" + Functions.get_info(context,"birthday"));


            update_profile(context,parameters);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }




    private void selectImage() {
        try {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, context.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            openCameraIntent();

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto,PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });


                builder.show();
            } else
                Toast.makeText(context, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
            try {

                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                // New Code
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Functions.Log_d_msg(context,"Angel " + orientation);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            Functions.Log_d_msg(context,"Angel 90 " + ExifInterface.ORIENTATION_ROTATE_90);
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            Functions.Log_d_msg(context,"Angel 180 " + ExifInterface.ORIENTATION_ROTATE_180);
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            matrix.postRotate(0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(),imagebitmap.getHeight() , matrix, true);



                Uri selectedImage_1 = Functions.getImageUri(context,rotatedBitmap);
                beginCrop(selectedImage_1);



                Functions.Log_d_msg(context,"Path " + imageFilePath);
                Functions.Log_d_msg(context,"" + imageFilePath);


            } catch (Exception e) {
                Functions.toast_msg(context,"Camera Error " + e.toString());
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {


            try {
                Uri selectedImage = data.getData();

                beginCrop(selectedImage);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == 123) {

            handleCrop(resultCode, data);
        }else{

            handleCrop(resultCode, data);
        }
    }

    String imageFilePath;
    public File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(500,500).start( Edit_profile_A.this,123);
    }

    private void handleCrop (int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            Uri userimageuri=Crop.getOutput(result);

            Add_profile_pic(userimageuri);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(context, "You cancel This.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"You Cancel This.", Toast.LENGTH_SHORT).show();
        }
    }



    public void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }




    // Upload User Profile_F.
    public void Add_profile_pic (Uri filePath) {

        Functions.Show_loader(context,true,false);


        final String fb_id = Functions.get_info(context,"fb_id");

        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        list_user_img_from_API.add(""+downloadUrl);

                        for (int i=0;i<list_user_img_from_API.size();i++){
                            if(list_user_img_from_API.get(i).equals("") || list_user_img_from_API.get(i).equals("0")){
                                list_user_img_from_API.remove(i);
                            }
                        }

                        if(list_user_img_from_API.size()<6){
                            list_user_img_from_API.add("0");
                        }
                        adapter.notifyDataSetChanged();


                        final JSONObject parameters = new JSONObject();
                        try {
                            parameters.put("image_link", downloadUrl);
                            parameters.put("fb_id", fb_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ApiRequest.Call_Api(
                                context,
                                "" + Api_Links.uploadImages,
                                parameters,
                                new CallBack() {
                                    @Override
                                    public void Get_Response(String requestType, String response) {

                                        Functions.cancel_loader();

                                         try{
                                            JSONObject resp = new JSONObject(response);
                                            JSONArray msg_arr = resp.getJSONArray("msg");
                                            msg_arr.getJSONObject(0).getString("response");

                                              Functions.get_user_info(fb_id,context);  // Method to save User data into Local Shared Prefrence

                                            Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));
                                        }catch (Exception v){

                                        }




                                    }
                                }
                        );

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());

                    }
                });





    }



    public void getuser_info (final String user_id){

        Functions.Show_loader(context,false,false);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                Api_Links.getUserInfo,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try{

                            Functions.cancel_loader();


                            JSONObject response=new JSONObject(resp);


                            if(response.getString("code").equals("200")) {


                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                SharedPrefrence.save_string(context,"" + user_info_obj.toString(),
                                        "" + SharedPrefrence.u_login_detail);


                                Variables.Var_sexuality = user_info_obj.getString("about_me");
                                Variables.Var_living = user_info_obj.getString("living");
                                Variables.Var_children = user_info_obj.getString("children");
                                Variables.Var_smoking = user_info_obj.getString("smoking");
                                Variables.Var_drinking = user_info_obj.getString("drinking");
                                Variables.Var_relationship = user_info_obj.getString("relationship");
                                Variables.Var_sexuality = user_info_obj.getString("sexuality");
                                Variables.Var_about_me = user_info_obj.getString("about_me");
                                user_gender = user_info_obj.getString("gender");



                                if(!user_info_obj.getString("image1").equals("") && !user_info_obj.getString("image1").equals("0")){
                                    list_user_img_from_API.add(user_info_obj.getString("image1"));
                                }

                                if(!user_info_obj.getString("image2").equals("") && !user_info_obj.getString("image2").equals("0")){
                                    list_user_img_from_API.add(user_info_obj.getString("image2"));

                                }

                                if(!user_info_obj.getString("image3").equals("") && !user_info_obj.getString("image3").equals("0")){
                                    list_user_img_from_API.add(user_info_obj.getString("image3"));

                                }

                                if(!user_info_obj.getString("image4").equals("") && !user_info_obj.getString("image4").equals("0")){
                                    list_user_img_from_API.add(user_info_obj.getString("image4"));
                                }

                                if(!user_info_obj.getString("image5").equals("") && !user_info_obj.getString("image5").equals("0")){
                                    list_user_img_from_API.add(user_info_obj.getString("image5"));

                                }

                                if(!user_info_obj.getString("image6").equals("") && !user_info_obj.getString("image6").equals("0")){

                                    list_user_img_from_API.add(user_info_obj.getString("image6"));
                                }



                                String living = user_info_obj.getString("living");
                                String children = user_info_obj.getString("children");
                                String smoking = user_info_obj.getString("smoking");
                                String drinking = user_info_obj.getString("drinking");
                                String relationship = user_info_obj.getString("relationship");
                                String sexuality = user_info_obj.getString("sexuality");



                                if(living.equals("0") || living.equals("")){
                                    living = "";
                                }

                                if(children.equals("0") || children.equals("")){
                                    children = "";
                                }

                                if(smoking.equals("0") || smoking.equals("")){
                                    smoking = "";
                                }

                                if(drinking.equals("0") || drinking.equals("")){
                                    drinking = "";
                                }

                                if(relationship.equals("0") || relationship.equals("")){
                                    relationship = "";
                                }

                                if(sexuality.equals("0") || sexuality.equals("")){
                                    sexuality = "";
                                }

                                user_living.setText("" +  living);
                                user_children.setText("" + children);
                                user_smoking.setText("" + smoking);
                                user_drinking.setText("" + drinking);
                                user_relationship.setText("" + relationship);
                                user_sex.setText("" +  sexuality);

                                about_me.setText("" + user_info_obj.getString("about_me"));

                                image_drawable_to_URI();
                            }
                        }catch (Exception b){
                            Functions.cancel_loader();
                            Functions.toast_msg(context,"" + b.toString());
                        }

                    }
                }
        );
    }


    public void image_drawable_to_URI (){

        if(list_user_img_from_API.size() <6){
            list_user_img_from_API.add("0");
        }

        adapter.notifyDataSetChanged();

    }


}
