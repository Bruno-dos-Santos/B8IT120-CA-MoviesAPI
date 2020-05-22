package ie.dbs.moviesapi.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey
    public final int Movie_ID;
    public String Name;
    public String Description;
    public String Rating;
    public String Year_Released;
    public String Image_Url;
    public String Date_Added;
    public String Date_Updated;


    public Movie(int Movie_ID, String Name, String Description, String Rating, String Year_Released, String Image_Url, String Date_Added, String Date_Updated) {
        this.Movie_ID = Movie_ID;
        this.Name = Name;
        this.Description = Description;
        this.Rating = Rating;
        this.Year_Released = Year_Released;
        this.Image_Url = Image_Url;
        this.Date_Added = Date_Added;
        this.Date_Updated = Date_Updated;
    }
}

