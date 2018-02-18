package ca.usask.auxilium.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

/**
 * Created by jadenball on 2018-02-17.
 */

public class ProfileEditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInAccount mAcct;
    private DatabaseReference mDatabase;
    Circle circle;

    //ImageView imgProfilePic;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtPrefName;
    EditText txtAge;
    EditText txtGender;
    EditText txtDiagnosis;
    EditText txtEmergencyContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAcct = GoogleSignIn.getLastSignedInAccount(this);
        mAuth = FirebaseAuth.getInstance();
        String userName = mAcct.getDisplayName();

        circle = new Circle();
        circle.setCircleName("JadenTestCircle");

        //imgProfilePic = findViewById(R.id.profilePicture);
        txtFirstName = findViewById(R.id.indexFirstName);
        txtLastName = findViewById(R.id.indexLastName);
        txtPrefName = findViewById(R.id.indexPrefName);
        txtAge = findViewById(R.id.indexAge);
        txtGender = findViewById(R.id.indexGender);
        txtDiagnosis = findViewById(R.id.indexDiagnosis);
        txtEmergencyContact = findViewById(R.id.emergencyContact);

        if(userName == null){
            Log.e("Google Account Error", "Display name is null");
            return;
        }
        Log.e("PROFILE USERNAME", userName);
        FirebaseDatabase.getInstance().getReference()
                .child("Circles")
                .child(circle.getCircleName())
                .child("Concerned")
                .child(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    txtFirstName.setText(user.getFirstName());
                    txtLastName.setText(user.getLastName());
                    txtPrefName.setText(user.getPreferredName());
                    txtAge.setText(user.getAge());
                    txtGender.setText(user.getGender());
                    txtDiagnosis.setText(user.getDiagnosis());
                    txtEmergencyContact.setText(user.getEmergencyContact());
                }
                else{
                    Log.e("FirebaseError", "User is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_save:
                User user = new User(txtFirstName.getText().toString(),
                        txtLastName.getText().toString(),
                        txtPrefName.getText().toString(),
                        txtAge.getText().toString(),
                        txtGender.getText().toString(),
                        txtDiagnosis.getText().toString(),
                        txtEmergencyContact.getText().toString());
                user.setUserName(mAcct.getDisplayName());
                user.setStatus("Active");
                mDatabase.child("Circles").child(circle.getCircleName()).child("Concerned").child(user.getUserName()).setValue(user);
                Log.d("FirebaseSave", "Edited user data saved");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
