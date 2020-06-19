package com.qboxus.hugme.All_Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import static com.qboxus.hugme.All_Activities.Segments_A.spb;
import static com.qboxus.hugme.All_Activities.Segments_A.vp;

public class Segment_dob_F extends RootFragment {

    View v;
    Button btn;
    EditText ET1,ET2,ET3,ET4,ET5,ET6,ET7,ET8;
    TextView dob_title_id;
    public static String birth_day, birth_month, birthday_year, dob_complete;
    int mDay, mMonth, mYear;
    Context context;
    RelativeLayout dob_RL_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_dob, null);
        context = getContext();
        ET1 = (EditText) v.findViewById(R.id.day_ET1_id);
        ET2 = (EditText) v.findViewById(R.id.day_ET2_id);
        ET3 = (EditText) v.findViewById(R.id.month_ET1_id);
        ET4 = (EditText) v.findViewById(R.id.month_ET2_id);
        ET5 = (EditText) v.findViewById(R.id.year_ET1_id);
        ET6 = (EditText) v.findViewById(R.id.year_ET2_id);
        ET7 = (EditText) v.findViewById(R.id.year_ET3_id);
        ET8 = (EditText) v.findViewById(R.id.year_ET4_id);
        dob_title_id = v.findViewById(R.id.dob_title_id);
        btn = (Button) v.findViewById(R.id.dob_btn_id);

        dob_RL_id = v.findViewById(R.id.dob_RL_id);
        dob_RL_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //date_picker();
            }
        });


        dob_title_id.setText("Hey, " + SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.share_social_info,
                "name"
        ) + " !, Whats your birthday?"

        );

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                birth_day = ET1.getText().toString() + "" + ET2.getText().toString();
                birth_month = ET3.getText().toString() + "" + ET4.getText().toString();
                birthday_year = ET5.getText().toString() + "" + ET6.getText().toString() + "" + ET7.getText().toString() + "" + ET8.getText().toString();
                dob_complete = birth_day + "/" + birth_month + "/" + birthday_year;


                int age = Functions.getAge( dob_complete );

                if(age > Variables.min_age){
                    vp.setCurrentItem(3);
                    spb.setCompletedSegments(3);

                }else{
                    Functions.alert_dialogue(getContext(),"Info","You are not under 18");
                }



            }
        });

        TextWatcher tw1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    int digit_et1 = Integer.parseInt(s.toString());
                    if(digit_et1 > 3){

                    ET1.setText("");
                    ET1.requestFocus();

                    }else{
                        if (s.length() == 1){
                            ET2.requestFocus();
                        }

                    }


                } catch (Exception b){

                } // End Catch Statement


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1){
                    ET3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw3 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    int digit_et1 = Integer.parseInt(s.toString());
                    if(digit_et1 > 1){
                        ET3.setText("");
                        ET3.requestFocus();

                    }else{
                        if (s.length() == 1){
                            ET4.requestFocus();
                        }

                    }


                } catch (Exception b){

                } // End Catch Statement



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw4 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    int digit_et1 = Integer.parseInt(s.toString());
                    int digit_et4 = Integer.parseInt(ET4.getText().toString());

                    handle_validation(ET4,ET3,ET5);

                } catch (Exception b){

                } // End Catch Statement

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw5 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                    ET6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw6 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                    ET7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw7 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                    ET8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw8 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        ET1.addTextChangedListener(tw1);
        ET2.addTextChangedListener(tw2);
        ET3.addTextChangedListener(tw3);
        ET4.addTextChangedListener(tw4);
        ET5.addTextChangedListener(tw5);
        ET6.addTextChangedListener(tw6);
        ET7.addTextChangedListener(tw7);
        ET8.addTextChangedListener(tw8);

        ET1.setSelectAllOnFocus(true);
        ET2.setSelectAllOnFocus(true);
        ET3.setSelectAllOnFocus(true);
        ET4.setSelectAllOnFocus(true);
        ET5.setSelectAllOnFocus(true);
        ET6.setSelectAllOnFocus(true);
        ET7.setSelectAllOnFocus(true);
        ET8.setSelectAllOnFocus(true);


        del_in_soft_key();
        return v;

    }

    public void del_in_soft_key(){

        ET1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E1");

                    ET1.requestFocus();

                }
                return false;
            }
        });


        ET2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E2");

                    ET1.requestFocus();
                }
                return false;
            }
        });

        ET3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E3");

                    ET2.requestFocus();

                }
                return false;
            }
        });

        ET4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E4");

                    ET3.requestFocus();
                }
                return false;
            }
        });

        ET5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E5");
                    ET4.requestFocus();

                }
                return false;
            }
        });

        ET6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E6");
                    ET5.requestFocus();

                }
                return false;
            }
        });

        ET7.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E7");
                    ET6.requestFocus();

                }
                return false;
            }
        });

        ET8.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    Log.e("IME_TEST", "DEL KEY E8");
                    ET7.requestFocus();

                }
                return false;
            }
        });


    } // End Delete

    public void handle_validation (EditText et_current, EditText et_previous, EditText et_focus_able){
        // Handle Validation
        int digit_et_curr = Integer.parseInt(et_current.getText().toString());
        int digit_et_pre = Integer.parseInt(et_previous.getText().toString());


        if(digit_et_pre == 0){
            et_focus_able.requestFocus();


        }else{
            if(digit_et_curr > 2){

                et_current.setText("");
                et_current.requestFocus();

            }else{
                if (et_current.getText().toString().length() == 1){
                    et_focus_able.requestFocus();
                }

            }
        }



    }


    public void date_picker(){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        ET1.setText("");
                        birth_day = "" +  dayOfMonth;
                        birth_month = "" + (monthOfYear + 1);
                        birthday_year = "" + year;


                        if(birth_day.length()==1){
                            // If length is 1.
                            birth_day = "0" + birth_day;
                        }

                        if(birth_month.length()==1){
                            // If length is 1.
                            birth_month = "0" + birth_month;
                        }


                        ET1.setText("" + birth_day.substring(0));
                        ET2.setText("" + birth_day.substring(1));

                        // Month
                        ET3.setText("" + birth_month.substring(0));
                        ET4.setText("" + birth_month.substring(1));
                        // Year
                        ET5.setText("" + birthday_year.substring(0));
                        ET6.setText("" + birthday_year.substring(1));
                        ET7.setText("" + birthday_year.substring(2));
                        ET8.setText("" + birthday_year.substring(3));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    } // End Method to show date picker dialogue

}
