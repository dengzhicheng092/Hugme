package com.qboxus.hugme.All_Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONObject;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Bottom_Sheet.View_Profile_Bottom_My;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

public class Account_A extends AppCompatActivity {

    Toolbar tb;
    ImageView iv;
    TextView  delete_account;
    Context context;
    Button account_signout_id;
    CheckBox account_CB_id;


    TextView profile_username_id;
    SimpleDraweeView IV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        delete_account = findViewById(R.id.delete_account);
        context = Account_A.this;

        tb = (Toolbar) findViewById(R.id.account_TB_id);
        iv = (ImageView) tb.findViewById(R.id.account_back_id);
        account_CB_id = findViewById(R.id.account_CB_id);

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Del_Account();
            }
        });


        profile_username_id = findViewById(R.id.username_txt);
        IV = findViewById(R.id.userimage_img);


        String img = Functions.get_info(context,"image1");
        String first_name = Functions.get_info(context,"first_name") + " ," + Functions.get_info(context,"age");
        profile_username_id.setText("" + first_name);
        try{
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img))
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
        }catch (Exception v){
            Functions.Log_d_msg(context,"" + v.toString());
        }


        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                Add_user_record();
            }
        });


        String is_hide = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_profile_hide_or_not);

        if(is_hide != null){

            if(is_hide.equals("1")){
                account_CB_id.setChecked(true);
            }else{
                account_CB_id.setChecked(false);
            }

        }



        account_CB_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                boolean varBoolSoundOn = (isChecked) ? true : false;
                String user_id = Functions.get_info(context,"fb_id");
                if(varBoolSoundOn == true){

                    SharedPrefrence.save_string(context,"1","" + SharedPrefrence.share_profile_hide_or_not);

                    Functions.Show_or_hide_profile(user_id,"1",context);

                }else{

                    SharedPrefrence.save_string(context,"0","" + SharedPrefrence.share_profile_hide_or_not);

                    Functions.Show_or_hide_profile(user_id,"0",context);

                }

            }
        });

        account_signout_id = findViewById(R.id.account_signout_id);
        account_signout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrence.logout_user(context);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Del_Account(){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Info");
        builder1.setMessage("Are you sure to delete Account?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);
                       Functions.delete_Account(context,"" + user_id);
                    }
                });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert11 = builder1.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            alert11.show();

        }else{
            alert11.show();
        }

    } // End Del Account_A


    public void Add_user_record(){
        // Method to add Logged in Record
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

            view_profile.show(getSupportFragmentManager(), view_profile.getTag());


        }catch (Exception b){
            Functions.toast_msg(context,"Err " + b.toString());
        }

    }// End Method to add Logged in Record

}
