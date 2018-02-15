package ca.usask.auxilium.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.usask.auxilium.R;

public class WelcomeJoinCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_join_create);
    }

    public void onCreateBtnClick(View v) {

        Intent intent = new Intent(getBaseContext(), CreateRoomActivity.class);
        startActivity(intent);
    }
}
