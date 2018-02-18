package ca.usask.auxilium.auth;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class IndexProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInAccount mAcct;
    Circle circle;

    ImageView imgProfilePic;
    TextView txtFirstName;
    TextView txtLastName;
    TextView txtPrefName;
    TextView txtAge;
    TextView txtGender;
    TextView txtDiagnosis;
    TextView txtEmergencyContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_profile_page);

        mAcct = GoogleSignIn.getLastSignedInAccount(this);
        mAuth = FirebaseAuth.getInstance();
        String userName = mAcct.getDisplayName();
        circle = new Circle();
        circle.setCircleName("JadenTestCircle");

        imgProfilePic = findViewById(R.id.profilePicture);
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
}
