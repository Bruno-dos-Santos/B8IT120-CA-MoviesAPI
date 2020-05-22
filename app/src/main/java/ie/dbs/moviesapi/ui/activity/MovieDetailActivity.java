package ie.dbs.moviesapi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.adapter.CastAdapter;
import ie.dbs.moviesapi.utils.Tools;

public class MovieDetailActivity extends BaseActivity {
    public static RequestQueue queue ;
    public static Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get the intent in the target activity
        Intent intent = getIntent();

        //get the attached bundle from the intent
        Bundle extras = intent.getExtras();

        //Extracting the stored data from the bundle
        String movieID = extras.getString("movieID");

        if (!movieID.isEmpty()){

            final String finalMovieID = movieID;

            applicationContext = getApplicationContext();

            if (MovieDetailActivity.queue == null) {
                MovieDetailActivity.queue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = getResources().getString(R.string.api_url) + "/Movie/GetMovieDetails";

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Map apiResponse = Tools.toMap(new JSONObject(response));
                                if (apiResponse.get("status").toString().equals("success")) {
                                initMovieDetails((HashMap)apiResponse.get("data"));
                                } else {
                                    Log.v("error", apiResponse.get("message").toString());
                                }
                            } catch (Exception e) {
                                Log.v("Error:", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("Response is:", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Movie_ID", finalMovieID);
                    return params;
                }
            };

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MovieDetailActivity.queue.add(stringRequest);
                }
            }, 200);
        }

    }

    private void initMovieDetails(Map details)
    {
        TextView movie_name = (TextView) findViewById(R.id.md_name);
        TextView description = (TextView) findViewById(R.id.md_description);
        RatingBar rating  = (RatingBar) findViewById(R.id.md_RatingMovie);
        TextView year_release = (TextView) findViewById(R.id.md_year_released);
        TextView date_updated = (TextView) findViewById(R.id.md_date_updated);
        TextView date_added = (TextView) findViewById(R.id.md_date_added);
        ImageView image = (ImageView)findViewById(R.id.md_thumbnail);


        try {
            URL url = new URL(MainActivity.applicationContext.getResources().getString(R.string.api_url)
                    +details.get("Movie_Cover").toString());
            Bitmap thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            image.setImageBitmap(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }



        movie_name.setText(details.get("Movie_Name").toString());
        description.setText(details.get("Movie_Description").toString());
        if (!details.get("Rating").toString().isEmpty()) {
            rating.setRating(Float.valueOf(details.get("Rating").toString()));
        } else rating.setRating(0);

        year_release.setText(details.get("Year_Released").toString());
        date_added.setText( "Added: " + Tools.formatDate(details.get("DateAdded").toString()));
        date_updated.setText("Updated: " + Tools.formatDate(details.get("DateUpdated").toString()));


        ArrayList<Object> castList = (ArrayList<Object>) details.get("cast");

        ListView listView;
        CastAdapter castAdapter;

        listView = (ListView) findViewById(R.id.md_castListView);

        castAdapter = new CastAdapter(MovieDetailActivity.this, castList);

        listView.setAdapter(castAdapter);


    }
}
