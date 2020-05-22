package ie.dbs.moviesapi.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.model.User;
import ie.dbs.moviesapi.utils.Tools;

public class LoginActivity extends BaseActivity {

    public static RequestQueue queue;
    public EditText email;
    public EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginActivity.queue == null) {
                    LoginActivity.queue = Volley.newRequestQueue(getApplicationContext());
                }

                String url = getResources().getString(R.string.api_url)+"/User/Login";

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Map loginResponse = Tools.toMap(new JSONObject(response));
                                    if(loginResponse.get("status").toString().equals("success")){

                                        Map userResponse = (HashMap)loginResponse.get("user");

                                        User user = new User(Integer.parseInt(userResponse.get("User_ID").toString()),userResponse.get("FullName").toString(), userResponse.get("Email").toString(),
                                                userResponse.get("Username").toString(), userResponse.get("Password").toString(),userResponse.get("User_Type").toString(), userResponse.get("Avatar").toString(),
                                                userResponse.get("DateCreated").toString(),userResponse.get("LastLogin").toString(),Integer.parseInt(userResponse.get("Active").toString()));
                                        database.userDAO().addUser(user);

                                        // calling the mainActivity after added.
                                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        LoginActivity.this.startActivity(mainIntent);
                                        LoginActivity.this.finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), loginResponse.get("message").toString(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Log.v("Error:", "Error Creating JSON object");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response is:",error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        return params;
                    }
                };
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getIsOnline()) {
                            LoginActivity.queue.add(stringRequest);
                        }
                        else{
                            Toast.makeText(applicationContext, "no internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 200);

            }
        });
    }
}