package ie.dbs.moviesapi.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public final int User_ID;
    public String FullName;
    public String Email;
    public String UserName;
    public String Password;
    public String User_Type;
    public String Avatar;
    public String DateCreated;
    public String LastLogin;
    public int Active;

    public User(int User_ID,String FullName,String Email,String UserName,String Password, String User_Type,String
            Avatar,String DateCreated, String LastLogin, int Active ){
        this.User_ID = User_ID;
        this.FullName = FullName;
        this.Email = Email;
        this.UserName = UserName;
        this.Password = Password;
        this.User_Type = User_Type;
        this.Avatar = Avatar;
        this.DateCreated = DateCreated;
        this.LastLogin = LastLogin;
        this.Active = Active;
    }
}