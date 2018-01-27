package br.com.codinglab.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import br.com.codinglab.myapp.R;
import br.com.codinglab.myapp.data.SessionHandler;
import br.com.codinglab.myapp.model.Contact;
import br.com.codinglab.myapp.model.User;
import br.com.codinglab.myapp.web.WebTaskContacts;

public class UserActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionHandler sessionHandler = new SessionHandler();
        try {
            user = sessionHandler.getUser(this);
        } catch (RuntimeException x){
            sessionHandler.removeSession(this);
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(myIntent);
            return;
        }

        EventBus.getDefault().register(this);

        initializeScreenFields();

        WebTaskContacts myCall = new WebTaskContacts(this, user.getToken());
        myCall.execute();
    }

    private void initializeScreenFields() {
        getSupportActionBar().setTitle(user.getUsername());
        ImageView imageView = findViewById(R.id.imageview_user);
        Picasso.with(this).load(user.getPhotoUrl()).into(imageView);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void handleError(Error error){
        Log.d("",error.getMessage());
    }

    @Subscribe
    public void handleList(List<Contact> contactList){
        for(Contact contact : contactList){
            Log.d("", contact.getName());
        }
    }
}
