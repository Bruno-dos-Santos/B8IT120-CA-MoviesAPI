package ie.dbs.moviesapi.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ie.dbs.moviesapi.dao.MovieDAO;
import ie.dbs.moviesapi.dao.UserDAO;
import ie.dbs.moviesapi.model.Movie;
import ie.dbs.moviesapi.model.User;

@Database(entities = {User.class, Movie.class}, version = 16, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract UserDAO userDAO();
    public abstract MovieDAO movieDAO();
    public static AppDatabase getDatabase(Context context){
        if (INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context,AppDatabase.class, "MovieApiDB").allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
}
