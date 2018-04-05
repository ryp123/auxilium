package ca.usask.auxilium.auth;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
        setTitle("Edit Profile");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            Log.e("User Error", "user is not logged in");
            return;
        }
        String userId = fUser.getUid();

        setupUI(findViewById(R.id.edit_profile));


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

                if(!user.isValid()){
                    Toast.makeText(getBaseContext(), "Error: please fill out the required fields", Toast.LENGTH_LONG).show();
                    return true;
                }

                if(user.getPreferredName().isEmpty()){
                    user.setPreferredName(user.getFirstName());
                }
                HashMap<String, Object> updateTasks = new HashMap<>();
                String userPath = "/users/" + userId + "/";
                updateTasks.put(userPath + "firstName", user.getFirstName());
                updateTasks.put(userPath + "lastName", user.getLastName());
                updateTasks.put(userPath + "preferredName", user.getPreferredName());
                updateTasks.put(userPath + "age", user.getAge());
                updateTasks.put(userPath + "gender", user.getGender());
                updateTasks.put(userPath + "diagnosis", user.getDiagnosis());
                updateTasks.put(userPath + "emergencyContact", user.getEmergencyContact());
                final ProfileEditActivity thisActivity = this;
                mDatabase.updateChildren(updateTasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Changes Saved", Toast.LENGTH_LONG).show();
                            Log.d("ProfileEditActivity", "updated profile!");
                            thisActivity.setResult(RESULT_OK);

                        } else {
                            Log.d("CircleActivity", "Failed to create new circle!");
                        }
                        thisActivity.finish();
                    }
                });
                Log.d("FirebaseSave", "Edited user data saved");
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }



    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    BaseActivity.hideSoftKeyboard(ProfileEditActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
