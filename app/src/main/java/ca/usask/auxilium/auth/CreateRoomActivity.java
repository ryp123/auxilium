package ca.usask.auxilium.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
    }

    private void addRoomName(){

    }

    public void  do_click(View v){
        EditText edt = (EditText)findViewById(R.id.roomNameText);
        String CircleName = edt.getText().toString();

        Circle circle = new Circle();

        circle.setCircleName(CircleName);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Circles").child(circle.getCircleName()).setValue(circle);

        String personName;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            personName = acct.getDisplayName();
            User user1 = new User();
            user1.setUserName(personName);
            user1.setActive(Boolean.TRUE);

            mDatabase.child("Circles").child(circle.getCircleName()).child("Concerned").setValue(user1);

        }

    }


}
