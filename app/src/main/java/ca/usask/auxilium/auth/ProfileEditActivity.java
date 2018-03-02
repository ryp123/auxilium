package ca.usask.auxilium.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

/**
 * Created by jadenball on 2018-02-17.
 */

public class ProfileEditActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private User user;
    private FirebaseUser fUser;

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
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            Log.e("User Error", "user is not logged in");
            return;
        }
        String userId = fUser.getUid();


        //imgProfilePic = findViewById(R.id.profilePicture);
        txtFirstName = findViewById(R.id.indexFirstName);
        txtLastName = findViewById(R.id.indexLastName);
        txtPrefName = findViewById(R.id.indexPrefName);
        txtAge = findViewById(R.id.indexAge);
        txtGender = findViewById(R.id.indexGender);
        txtDiagnosis = findViewById(R.id.indexDiagnosis);
        txtEmergencyContact = findViewById(R.id.emergencyContact);

        Log.d("PROFILE email/id", userId);
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
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
                    user = new User();
                    user.setStatus("Active");
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
                String userId = fUser.getUid();
                user.setFirstName(txtFirstName.getText().toString());
                user.setLastName(txtLastName.getText().toString());
                user.setPreferredName(txtPrefName.getText().toString());
                user.setAge(txtAge.getText().toString());
                user.setGender(txtGender.getText().toString());
                user.setDiagnosis(txtDiagnosis.getText().toString());
                user.setEmergencyContact(txtEmergencyContact.getText().toString());
                mDatabase.child("users").child(userId).setValue(user);
                Log.d("FirebaseSave", "Edited user data saved");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
