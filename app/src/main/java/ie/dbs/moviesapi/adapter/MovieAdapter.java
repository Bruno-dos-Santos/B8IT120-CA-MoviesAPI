package ie.dbs.moviesapi.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import java.net.URL;
import java.util.List;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.model.Movie;
import ie.dbs.moviesapi.ui.activity.MainActivity;
import ie.dbs.moviesapi.utils.Tools;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movies;
    private OnMovieListener mOnMovieListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnMovieListener onMovieListener;
        public TextView name;
        public TextView description;
        public RatingBar rating;
        public TextView year_release;
        public TextView date_added;
        public TextView date_updated;
        public View layout;
        public ImageView thumbnail;

        public ViewHolder(View v, OnMovieListener onMovieListener) {
            super(v);
            layout = v;
            name = (TextView) v.findViewById(R.id.name);
            description = (TextView) v.findViewById(R.id.description);
            rating = (RatingBar) v.findViewById(R.id.RatingMovie);
            year_release = (TextView) v.findViewById(R.id.year_released);
            date_added = (TextView) v.findViewById(R.id.date_added);
            date_updated = (TextView) v.findViewById(R.id.date_updated);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            this.onMovieListener = onMovieListener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View movieView = inflater.inflate(R.layout.movie, parent, false);
        ViewHolder movie = new ViewHolder(movieView, mOnMovieListener);
        return movie;
    }

    public MovieAdapter(List<Movie> dataset, OnMovieListener onMovieListener) {
        movies = dataset;
        mOnMovieListener = onMovieListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Movie movie = movies.get(position);
        holder.name.setText(movie.Name);
        holder.description.setText(movie.Description);
        if (!movie.Rating.equals("")) {
            holder.rating.setRating((Float.valueOf(movie.Rating)));
        } else holder.rating.setRating(0);
        holder.year_release.setText(movie.Year_Released);
        holder.date_updated.setText("Updated: " + Tools.formatDate(movie.Date_Added));
        holder.date_added.setText("Added: " + Tools.formatDate(movie.Date_Updated));


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(MainActivity.applicationContext.getResources().getString(R.string.api_url)
                    +movie.Image_Url);
            Bitmap thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.thumbnail.setImageBitmap(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnMovieListener{
        void onMovieClick(int position);
    }
}
