package com.example.android.googlelogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SecActivity extends AppCompatActivity {

    private static GoogleApiClient mGoogleApiClient;
    public static Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        Intent intent = getIntent();
        TextView t = (TextView)findViewById(R.id.text1);
        t.setText("Hi "+intent.getStringExtra("FNAME").split(" ")[0]);


        ImageView im = (ImageView)findViewById(R.id.image1);
        try {
            System.out.println(intent.getStringExtra("IMAGE_URL"));
            new DownloadImage().execute(intent.getStringExtra("IMAGE_URL")).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        im.setImageBitmap(bm);
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.logout:
                        logOut();
                }
                return false;
            }
        });
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
    }

    public void logOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Logged Out", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }
                });
    }
}

class DownloadImage extends AsyncTask<String,Integer,Long>{

    private ImageView im;
    protected Long doInBackground(String... params) {
        try {
            System.out.println(params[0]);
            InputStream in = (InputStream)new URL(params[0]).getContent();
            SecActivity.bm = BitmapFactory.decodeStream(in);
            return null;
        } catch (Exception e) {
            return null;
        }

    }
}