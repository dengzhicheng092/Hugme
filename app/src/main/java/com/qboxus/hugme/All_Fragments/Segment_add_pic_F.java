package com.qboxus.hugme.All_Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.qboxus.hugme.All_Activities.Enable_location_A;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import es.guiguegon.gallerymodule.GalleryHelper;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.qboxus.hugme.All_Fragments.Segment_dob_F.dob_complete;
import static com.qboxus.hugme.All_Fragments.Segment_gender_F.gender;
import static com.qboxus.hugme.All_Fragments.Segment_name_F.signup_name;
import static com.qboxus.hugme.All_Activities.Edit_profile_A.REQUEST_CODE_GALLERY;

public class Segment_add_pic_F extends RootFragment {

    View v;
    TextView textView;
    Button btn;
    ImageView iv;
    RelativeLayout add_pic_rl;
    Context context;
    Uri imag_uri;

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;

    FirebaseStorage storage;
    StorageReference storageReference;

    LocationManager locationManager;
    String img_url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_add_pic, null);

        context = getContext();

        textView = (TextView) v.findViewById(R.id.add_pic_title_id);
        String text = "Picture time! <br> Choose your photo";
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        add_pic_rl = (RelativeLayout) v.findViewById(R.id.add_pic_rl_id);

        textView.setText(Html.fromHtml(text));

        img_url = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.share_social_info,
                "img_url"
        );


        ImageView profile_pic = v.findViewById(R.id.profile_pic);

        try{
            Picasso.get().load(img_url).fit().centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(profile_pic);

        }catch (Exception b){

        }



        btn = (Button) v.findViewById(R.id.add_pic_btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imag_uri != null){

                    upload_image_to_Firebase(imag_uri);

                }else{
                    upload_image_to_Firebase(Uri.parse(img_url));
                }

            }
        });

        iv = (ImageView) v.findViewById(R.id.add_pic_plus_id);
        add_pic_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        return v;
    }


    private void openGalleryMultiplSelection(int maxSelectedItems) {
        startActivityForResult(new GalleryHelper().setMultiselection(true)
                .setMaxSelectedItems(maxSelectedItems)
                .getCallingIntent(getActivity()), REQUEST_CODE_GALLERY);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            // ...
        }
    }

    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
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
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {

                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                beginCrop(selectedImage);

                InputStream imageStream = null;
                try {
                    imageStream = context.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                Functions.Log_d_msg(context,"Path " + imageFilePath);

                Functions.Log_d_msg(context,"" + imageFilePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {

            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                bitmap.getWidth();
                bitmap.getHeight();
                imgPath = getRealPathFromURI(selectedImage);
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

    private void openCameraIntent() {

       Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(context.getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Functions.Log_d_msg(context,"" + ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), getActivity().getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }

    String imageFilePath;
    private File createImageFile() throws IOException {
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
        Crop.of(source, destination).asSquare().withMaxSize(500,500).start(context,getCurrentFragment(),123);
    }

    private void handleCrop (int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri userimageuri=Crop.getOutput(result);

            InputStream imageStream = null;
            try {
                imageStream = context.getContentResolver().openInputStream(userimageuri);
            } catch (FileNotFoundException e) {
                Functions.toast_msg(context,"" + e.toString());
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
            imag_uri = userimageuri;

            String path=userimageuri.getPath();

            ImageView profile_pic = v.findViewById(R.id.profile_pic);
            profile_pic.setImageBitmap(imagebitmap);


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void upload_image_to_Firebase (Uri filePath){

        Functions.Show_loader(context,false,false);

        final String user_id = SharedPrefrence.get_social_info(context,SharedPrefrence.share_social_info,
                "user_id");

        if(filePath.toString().startsWith("http")){

            JSONObject parameters = new JSONObject();
            try {
                parameters.put("fb_id", user_id);
                parameters.put("first_name",signup_name);
                parameters.put("last_name", "");
                parameters.put("birthday", dob_complete);
                parameters.put("gender", "" + gender);
                parameters.put("image1",filePath);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // TODO: Calling API for SignUp
            API_calling(Api_Links.API_SignUp,context,parameters);



        }else{

            StorageReference ref = storageReference.child("images/"+ user_id);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            JSONObject parameters = new JSONObject();
                            try {
                                parameters.put("fb_id", user_id);
                                parameters.put("first_name",signup_name);
                                parameters.put("last_name", "");
                                parameters.put("birthday", dob_complete);
                                parameters.put("gender", "" + gender);
                                parameters.put("image1",downloadUrl);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // TODO: Calling API for SignUp
                            API_calling(Api_Links.API_SignUp,context,parameters);

                            Functions.cancel_loader();  // todo: Cancel Loader


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // progressDialog.dismiss();
                            Functions.toast_msg(context,"" + e.toString());

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

    }


    public Fragment getCurrentFragment(){

        return getActivity().getSupportFragmentManager().findFragmentById(R.id.view_pager_id);

    }


      public void API_calling (String url, final Context context_9, JSONObject parameters){
        try{

            ApiRequest.Call_Api(
                    context,
                     url,
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



                                    SharedPrefrence.save_string(context,"" + user_info_obj.toString(),
                                            "" + SharedPrefrence.u_login_detail);

                                    user_info_obj.getString("fb_id");

                                    SharedPrefrence.save_string(
                                            context,
                                            "" + SharedPrefrence.u_id,
                                            "" + user_info_obj.getString("fb_id")
                                    );

                                    SharedPrefrence.remove_value_of_key(context,
                                            "" + SharedPrefrence.share_social_info);
                                    Functions.cancel_loader();  // todo: Cancel Loader


                                    Open_enable_location();


                                }

                                Functions.cancel_loader();

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


    public void Open_enable_location(){
        getActivity().startActivity(new Intent(context, Enable_location_A.class));
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        getActivity().finish();
    }


}
