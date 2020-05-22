package ie.dbs.moviesapi.ui.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.adapter.MovieAdapter;
import ie.dbs.moviesapi.model.Movie;
import ie.dbs.moviesapi.utils.Tools;

public class MainActivity extends BaseActivity implements MovieAdapter.OnMovieListener {
    public static RequestQueue queue ;


    private List<Movie> movies;
    private List<Object> lMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startApi(getIsOnline());
    }

    private void startApi(final boolean isOnline) {

        if (MainActivity.queue == null) {
            MainActivity.queue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = getResources().getString(R.string.api_url) + "/Movie/GetAllMovies";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final Map apiResponse = Tools.toMap(new JSONObject(response));
                            if (apiResponse.get("status").toString().equals("success")) {

                                lMovies = (List<Object>) apiResponse.get("movies");

                            } else {
                                Log.e("error", apiResponse.get("message").toString());
                            }
                        } catch (Exception e) {
                            Log.e("Error:", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response is:", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User_ID", "1");
                params.put("ForApp", "true");
                return params;
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.queue.add(stringRequest);
            }
        }, 100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerView(isOnline);
            }
        }, 1000);

    }

    private void initRecyclerView(boolean isOnline) {

        movies = getMovieData(lMovies, isOnline);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new MovieAdapter(movies, this );
        recyclerView.setAdapter(mAdapter);
    }

    private List<Movie> getMovieData(List<Object> movies, boolean isOnline) {

        if (isOnline)  {
            if (movies != null ){
                database.movieDAO().removeAllMovies();
                for (int i = 0; i < movies.size(); i++){
                    Map mapMovie = (HashMap)movies.get(i);

                    Movie movie = new Movie( Integer.parseInt(mapMovie.get("Movie_ID").toString()),
                            mapMovie.get("Movie_Name").toString(), mapMovie.get("Movie_Description").toString(),
                            mapMovie.get("Rating").toString(), mapMovie.get("Year_Released").toString(),
                            mapMovie.get("Movie_Cover").toString(), mapMovie.get("DateAdded").toString(),
                            mapMovie.get("DateUpdated").toString());

                    database.movieDAO().addMovie(movie);
                }
            }

        }

        List<Movie> returnMovies = database.movieDAO().getAllMovies();

        return returnMovies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_refresh:
                startApi(getIsOnline());
                Toast.makeText(applicationContext, "Refreshed!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                //logout the system back to the Login Activity and delete data from sqlite
                //AppDatabase database = AppDatabase.getDatabase(getApplicationContext());
                database.userDAO().removeAllUsers();
                database.movieDAO().removeAllMovies();
                Intent mainIntent = new Intent(MainActivity.this,LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
                Toast.makeText(applicationContext, "Logged out!", Toast.LENGTH_SHORT).show();
                break;
            default:

                break;
        }
        return true;
    }

    @Override
    public void onMovieClick(int position) {

        Movie movie = movies.get(position);

        String movieID = String.valueOf(movie.Movie_ID);

        if (getIsOnline()) {
            if (!movieID.isEmpty()) {
                Intent intent = new Intent(this, MovieDetailActivity.class);
                intent.putExtra("movieID", movieID);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "offline, try again later!", Toast.LENGTH_SHORT).show();
            Log.d("onMovieClick: ", "offLine");
        };

    }
}
