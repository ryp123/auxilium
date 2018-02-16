package ca.usask.auxilium.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class CreateRoomActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Circle mCircle;
    private String mUserName;

    GoogleSignInAccount mAcct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
    }

    public void  onCreateButtonClick(View v) {
        EditText edt = (EditText) findViewById(R.id.roomNameText);


        String circleName = edt.getText().toString();

        Log.d("the name of circle being created",circleName);

        mCircle = new Circle();
        mCircle.setCircleName(circleName);

        mAcct = GoogleSignIn.getLastSignedInAccount(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        updateDatabase();

    }

     public void updateDatabase(){

         mDatabase.child("Circles").child(mCircle.getCircleName()).setValue(mCircle);

         if (mAcct != null) {

             mUserName = mAcct.getDisplayName();
             User user = new User();
             user.setUserName(mUserName);
             user.setStatus("Active");
             mDatabase.child("Circles").child(mCircle.getCircleName()).child("Concerned").child(user.getUserName()).setValue(user);

         }

    }

}
