package ie.dbs.moviesapi.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ie.dbs.moviesapi.model.Movie;

@Dao
public interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(Movie movie);

    @Query("SELECT * FROM Movie")
    public List<Movie> getAllMovies();
    @Query("SELECT * FROM Movie WHERE Movie_ID = :Movie_ID")
    public Movie getMovie(long Movie_ID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Query("DELETE FROM Movie")
    void removeAllMovies();
}

