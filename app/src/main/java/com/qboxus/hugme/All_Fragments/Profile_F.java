package com.qboxus.hugme.All_Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qboxus.hugme.All_Activities.Edit_profile_A;
import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Activities.View_profile_A;
import com.qboxus.hugme.Boost.Boost_F;
import com.qboxus.hugme.Code_Classes.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;

import com.qboxus.hugme.Bottom_Sheet.View_Profile_Bottom_My;
import com.qboxus.hugme.All_Activities.Edit_profile_VP_A;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.InAppSubscription.InApp_Subscription_A;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Profile_F extends Fragment implements View.OnClickListener {

    View v;
    SimpleDraweeView IV;
    ImageView IV1;
    TextView prof_tv;

    RelativeLayout prof_rl;
    CardView get_more_aten;
    public static CircleProgressView mCircleView;

    RelativeLayout popularity_rl,credits_activate_popularity;
    LinearLayout credit_premium_ll;
    Context context;
    FirebaseStorage storage;
    StorageReference storageReference;
    RelativeLayout profile_RL_id;

    SimpleDraweeView userimage_img;
    TextView username_txt;
    TextView complete_text;

    String user_image;
    String user_name;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null);

        context = getContext();
        IV = v.findViewById(R.id.userimage_img);
        IV.setClipToOutline(true);
        profile_RL_id = v.findViewById(R.id.profile_RL_id);
        complete_text = v.findViewById(R.id.complete_text);

        username_txt = v.findViewById(R.id.username_txt);
        Main_F.tb.setVisibility(View.GONE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userimage_img = v.findViewById(R.id.userimage_img);
        userimage_img.setOnClickListener(this::onClick);


        profile_RL_id.setOnClickListener(this::onClick);


        Set_user_data();

        prof_rl = (RelativeLayout) v.findViewById(R.id.profile_RL_id);
        popularity_rl = (RelativeLayout) v.findViewById(R.id.boost_layout);
        credits_activate_popularity = (RelativeLayout) v.findViewById(R.id.credits_activate_popularity_rl_id);

        prof_tv = (TextView) v.findViewById(R.id.see_profile);

        credit_premium_ll = (LinearLayout) v.findViewById(R.id.credtis_premium_ll_id);

        get_more_aten = (CardView) v.findViewById(R.id.get_more_atten_cv_id);

        IV1 = (ImageView) v.findViewById(R.id.add_photo_img_id);
        IV1.setOnClickListener(this::onClick);


        mCircleView = (CircleProgressView) v.findViewById(R.id.cpv_id);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {

            }
        });


        mCircleView.setMaxValue(100);

        mCircleView.setDirection(Direction.CW);

        mCircleView.setUnit("%");

        mCircleView.setUnitVisible(true);

        mCircleView.setMinimumWidth(42);
        mCircleView.setMinimumHeight(42);

        //text sizes
        mCircleView.setTextSize(30); // text size set, auto text size off
        mCircleView.setUnitSize(22); // if i set the text size i also have to set the unit size
        mCircleView.setUnitScale(1f);
        mCircleView.setTextScale(1f);
        mCircleView.setTextColor(getResources().getColor(R.color.purple));
        mCircleView.setUnitColor(getResources().getColor(R.color.purple));

        mCircleView.setBarColor(getResources().getColor(R.color.purple));
        mCircleView.setBarWidth(6);

        mCircleView.setRimColor(getResources().getColor(R.color.off_white));
        mCircleView.setRimWidth(6);

        mCircleView.setInnerContourSize(0);
        mCircleView.setOuterContourSize(0);

        get_more_aten.setOnClickListener(this);
        prof_tv.setOnClickListener(this);
        username_txt.setOnClickListener(this);


        v.findViewById(R.id.premium_layout).setOnClickListener(this::onClick);
        v.findViewById(R.id.boost_layout).setOnClickListener(this::onClick);

        return v;
    }



    public void Set_user_data(){
        user_image = Functions.get_info(context,"image1");

        user_name = Functions.get_info(context,"first_name")
                +" "+Functions.get_info(context,"last_name");
        username_txt.setText(user_name+ " ," + Functions.get_info(context,"age"));
        try{
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(user_image))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            IV.getHierarchy().setPlaceholderImage(R.drawable.image_placeholder);
            IV.getHierarchy().setFailureImage(R.drawable.image_placeholder);
            IV.getHierarchy().setRoundingParams(roundingParams);
            IV.setController(controller);
        }
        catch (Exception v){
            Functions.Log_d_msg(context,"" + v.toString());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_more_atten_cv_id:

                if(SharedPrefrence.calculate_complete_profile(context) == 100){
                    startActivity(new Intent(getActivity(), Edit_profile_A.class));

                }else{

                    startActivity(new Intent(context, Edit_profile_VP_A.class));

                }
                break;

            case R.id.see_profile:
                startActivity(new Intent(getActivity(), View_profile_A.class));
                break;
            case R.id.username_txt:
                startActivity(new Intent(getActivity(), View_profile_A.class));
                break;

            case R.id.premium_layout:
                open_subscription_view();
                break;

            case R.id.boost_layout:
                if(MainActivity.purduct_purchase)
                openBoost();
                else
                    open_subscription_view();
                break;

            case R.id.add_photo_img_id:
                selectImage();
                break;

            case R.id.profile_RL_id:
                startActivity(new Intent(getActivity(), Edit_profile_A.class));
                break;

            case R.id.userimage_img:
                Add_user_record();
                break;
        }
    }




    public void open_subscription_view(){

        InApp_Subscription_A inApp_subscription_a = new InApp_Subscription_A();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_F, inApp_subscription_a)
                .addToBackStack(null)
                .commit();

    }


    public void openBoost(){
        Boost_F match_f = new Boost_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.Main_F, match_f).commit();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if(SharedPrefrence.calculate_complete_profile(context) == 100){
                complete_text.setText("Get more attention - \nProfile_F completed.");
            }


            mCircleView.setValue(SharedPrefrence.calculate_complete_profile(context));

        }
    }


    public void Add_user_record(){
        String info = SharedPrefrence.get_string(context,SharedPrefrence.u_login_detail);

        try{
            JSONObject user_obj = new JSONObject(info);
            Get_Set_Nearby nearby = new Get_Set_Nearby(
                    "" +  user_obj.getString("fb_id"),
                    "" + user_obj.getString("first_name"),
                    "" + user_obj.getString("last_name"),
                    "" + user_obj.getString("age"),
                    "" + user_obj.getString("about_me"),
                    "no",
                    "" +  user_obj.getString("image1"),
                    "like",

                    "Job",
                    "" + user_obj.getString("company"),
                    "" + user_obj.getString("school"),
                    "" + user_obj.getString("living"),
                    "" + user_obj.getString("children"),
                    "" + user_obj.getString("smoking"),
                    "" + user_obj.getString("drinking"),
                    "" + user_obj.getString("relationship"),
                    "" + user_obj.getString("sexuality"),
                    "" ,
                    "" +  user_obj.getString("image2"),
                    "" +  user_obj.getString("image3"),
                    "" +  user_obj.getString("image4"),
                    "" +  user_obj.getString("image5"),
                    "" +  user_obj.getString("image6")

            );

            Bundle bundle_user_profile = new Bundle();
            bundle_user_profile.putString("user_id", "" + user_obj.getString("fb_id"));
            bundle_user_profile.putString("current_position", "" );
            bundle_user_profile.putSerializable("user_near_by",  nearby);

            View_Profile_Bottom_My view_profile = new View_Profile_Bottom_My();
            view_profile.setArguments(bundle_user_profile);

            view_profile.show(getActivity().getSupportFragmentManager(), view_profile.getTag());


        }catch (Exception b){
            Functions.toast_msg(context,"Err " + b.toString());
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

        if(resultCode ==RESULT_OK) {
            if (requestCode == PICK_IMAGE_CAMERA) {
                try {

                    Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                    // New Code
                    Matrix matrix = new Matrix();
                    try {
                        ExifInterface exif = new ExifInterface(imageFilePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Functions.Log_d_msg(context, "Angel " + orientation);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                Functions.Log_d_msg(context, "Angel 90 " + ExifInterface.ORIENTATION_ROTATE_90);
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                Functions.Log_d_msg(context, "Angel 180 " + ExifInterface.ORIENTATION_ROTATE_180);
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
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);


                    Uri selectedImage_1 = Functions.getImageUri(context, rotatedBitmap);
                    beginCrop(selectedImage_1);


                    Functions.Log_d_msg(context, "Path " + imageFilePath);
                    Functions.Log_d_msg(context, "" + imageFilePath);


                } catch (Exception e) {
                    Functions.toast_msg(context, "Camera Error " + e.toString());
                    e.printStackTrace();
                }
            }

            else if (requestCode == PICK_IMAGE_GALLERY) {


                try {
                    Uri selectedImage = data.getData();

                    beginCrop(selectedImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if (requestCode == 123) {

                handleCrop(resultCode, data);
            }

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

        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(500,500).start(getActivity(),123);
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
        if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getActivity().getPackageName()+".fileprovider", photoFile);
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
                        Functions.Log_d_msg(context,"Img Url " + downloadUrl);


                        final JSONObject parameters = new JSONObject();
                        try {
                            parameters.put("image_link", downloadUrl);
                            parameters.put("fb_id", fb_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ApiRequest.Call_Api(
                                context,
                                "" + Api_Links.changeProfilePicture,
                                parameters,
                                new CallBack() {
                                    @Override
                                    public void Get_Response(String requestType, String response) {

                                        Functions.cancel_loader();
                                        try {
                                            JSONObject  resp = new JSONObject(response);
                                        if(resp.getString("code").equals("200")) {


                                            JSONArray msg_obj = resp.getJSONArray("msg");
                                            JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                            SharedPrefrence.save_string(context, "" + user_info_obj.toString(),
                                                    "" + SharedPrefrence.u_login_detail);

                                            Set_user_data();
                                        }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
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




}
