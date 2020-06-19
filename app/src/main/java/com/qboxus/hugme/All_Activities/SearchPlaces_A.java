package com.qboxus.hugme.All_Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.qboxus.hugme.All_Adapters.PlaceAutocompleteAdapter;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Model_Classes.SavedAddress;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import static com.qboxus.hugme.All_Fragments.Swipe_F.search_place;

public class SearchPlaces_A extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface,View.OnClickListener{

    Context context;

    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;


    EditText mSearchEdittext;

    Button close_places;
    ImageView mClear;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
        context = SearchPlaces_A.this;

        close_places = findViewById(R.id.cancel_places);
        close_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();
    }

    private void initViews(){


        findViewById(R.id.people_nearby_layout).setOnClickListener(this::onClick);

        mRecyclerView = (RecyclerView)findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText)findViewById(R.id.search_et);
        mClear = (ImageView)findViewById(R.id.clear);
        mClear.setOnClickListener(this);


        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.item_placesearch, null);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));



        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{


                    if (count > 0) {
                        mClear.setVisibility(View.VISIBLE);
                        if (mAdapter != null) {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    } else {
                        mClear.setVisibility(View.GONE);

                    }
                    if (!s.toString().equals("") ) {
                        mAdapter.getFilter().filter(s.toString());
                    }


                }catch (Exception b){
                    Functions.toast_msg(context,"Writing " + b.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.people_nearby_layout){
            Save_Nearby_Location();
        }
        else if(v == mClear){
            mSearchEdittext.setText("");
            if(mAdapter!=null){
                mAdapter.clearList();
            }

        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {


        if(mResultList!=null){
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);

                SharedPrefrence.save_string(context,"" + mResultList.get(position).description,SharedPrefrence.share_user_search_place_name);

                try{
                    search_place.setText("" + mResultList.get(position).description);
                }catch (Exception b){

                }

                    try{
                        StringBuilder stringBuilder=new StringBuilder();
                        stringBuilder.append("https://maps.googleapis.com/maps/api/place/details/json?placeid=");
                        stringBuilder.append(URLEncoder.encode(placeId, "utf8"));
                        stringBuilder.append("&key=");
                        stringBuilder.append(getResources().getString(R.string.google_developer_api_key));

                        RequestQueue rq = Volley.newRequestQueue(this);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, stringBuilder.toString(), null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject jsonResults) {
                                        String respo=jsonResults.toString();
                                        Log.d("responce",respo);

                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(jsonResults.toString());

                                            JSONObject result = jsonObj.getJSONObject("result");
                                            JSONObject geometry = result.getJSONObject("geometry");
                                            JSONObject location = geometry.getJSONObject("location");


                                            Intent data = new Intent();
                                            data.putExtra("lat", String.valueOf(location.opt("lat")));
                                            data.putExtra("lng", String.valueOf(location.opt("lng")));
                                            setResult(RESULT_OK, data);
                                            finish();

                                            String location_string = location.opt("lat")+", "+location.opt("lng");

                                            SharedPrefrence.save_string(context, location_string,SharedPrefrence.share_user_search_place_lat_lng);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        Log.d("respoeee",error.toString());
                                    }
                                });
                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        rq.getCache().clear();
                        rq.add(jsonObjectRequest);



                    }catch (Exception b){
                }

            }
            catch (Exception e){

            }

        }
    }

    @Override
    public void onSavedPlaceClick(ArrayList<SavedAddress> mResultList, int position) {
        if(mResultList!=null){
            try {
                Intent data = new Intent();
                data.putExtra("lat", String.valueOf(mResultList.get(position).getLatitude()));
                data.putExtra("lng", String.valueOf(mResultList.get(position).getLongitude()));
                setResult(SearchPlaces_A.RESULT_OK, data);
                finish();

            }
            catch (Exception e){

            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void Save_Nearby_Location(){
        try{
            search_place.setText("People Nearby");
        }catch (Exception b){

        }
        SharedPrefrence.remove_value_of_key(this,SharedPrefrence.share_user_search_place_lat_lng);
        SharedPrefrence.remove_value_of_key(this,SharedPrefrence.share_user_search_place_name);
        finish();
    }

}
