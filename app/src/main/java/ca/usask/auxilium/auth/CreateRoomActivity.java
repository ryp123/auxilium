package ca.usask.auxilium.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.regex.Pattern;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.Invitations;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.MainActivity;
import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class CreateRoomActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Circle mCircle;

    private String firstInviteEmail;
    private String secondInviteEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CircleActivity", "new instance created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        //Set the default items for the ailment drop down
        Spinner spinner = (Spinner) findViewById(R.id.ailmentSpinner);
        String[] spinItems = new String[]{"Alcoholism", "Depression", "Drug Addiction"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinItems);
        spinner.setAdapter(adapter);
    }

    public void  onCreateButtonClick(View v) {
        EditText edt = (EditText) findViewById(R.id.roomNameText);
        String circleCreatorEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String circleName = edt.getText().toString();
        Log.d("the name of circle being created",circleName);
        mCircle = new Circle();
        mCircle.setCircleName(circleName);

        // get ailment information
        Spinner spinner = (Spinner) findViewById(R.id.ailmentSpinner);
        mCircle.setAilment(spinner.getSelectedItem().toString());

        //get email information
        Log.d("Invitations", "Getting the emails to send invitations to");
        EditText emailInput = (EditText) findViewById(R.id.friendEmail1);
        String email = emailInput.getText().toString();
        if (this.isEmailTheDefaultValue(email)) {
            this.firstInviteEmail = null;
        } else if (!this.isEmailValid(email)) {
            emailInput.setError("Invalid email");
            return;
        } else if(email.equals(circleCreatorEmail)) {
            emailInput.setError("Cannot send invitation to yourself.");
        }  else {
            this.firstInviteEmail = email;
        }
        emailInput = (EditText) findViewById(R.id.friendEmail2);
        email = emailInput.getText().toString();
        if (this.isEmailTheDefaultValue(email)) {
            this.secondInviteEmail = null;
        } else if (!this.isEmailValid(email)) {
            emailInput.setError("Invalid email");
            return;
        } else if(email.equals(circleCreatorEmail)) {
            emailInput.setError("Cannot send invitation to yourself.");
        }  else {
            this.secondInviteEmail = email;
        }

        // set room info
        EditText circleInfo = (EditText) findViewById(R.id.roomInfoText);
        String circleInfoTxt = circleInfo.getText().toString();
        mCircle.setCircleInfo(circleInfoTxt);

        // get db/account info reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("CircleActivity", "About to update DB.");

        final Activity createRoomActivity = this;
        final String circleId = mDatabase.push().getKey();
        Log.d("CircleActivity", "The circle id is: " + circleId);
        Log.d("CircleActivity", "The user id is: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        Toast.makeText(this.getBaseContext(), "Creating your circle!", Toast.LENGTH_SHORT);


        /////////notification///////
        FirebaseMessaging.getInstance().subscribeToTopic(circleId);

        /////end of notification/////////

        mDatabase.updateChildren(getUpdateTasks(circleId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("CircleActivity", "Created new circle!");
                    processInvitations(circleId);
                } else {
                    Log.d("CircleActivity", "Failed to create new circle!");
                }
                createRoomActivity.finish();
            }
        });




    }

     private HashMap<String, Object> getUpdateTasks(String circleId){
         String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
         HashMap<String, Object> updateTasks = new HashMap();
         String circlePath = "/circles/" + circleId;
         String userPath = "/users/" + userId;
         HashMap<String, String> userCircleDetails =  new HashMap<>();
         userCircleDetails.put("role", "Index");
         HashMap <String, String> memberDetails = new HashMap<>();
         memberDetails.put("role", "Index");
         memberDetails.put("status", "Active");
         HashMap <String, String> circleDetails = new HashMap<>();
         circleDetails.put("name", mCircle.getCircleName());
         circleDetails.put("diagnosis", mCircle.getAilment());
         circleDetails.put("role", "Index");
         updateTasks.put(circlePath, circleDetails);
         updateTasks.put(userPath + "/lastCircleOpen", circleId);
         updateTasks.put(userPath + "/circles/" + circleId, userCircleDetails);
         updateTasks.put("/circleMembers/" + circleId + "/" + userId, memberDetails);
         return updateTasks;

    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isEmailTheDefaultValue(String email) {
        String defaultValue = "Email address";
        return email.equals(defaultValue);

    }


    private void processInvitations(String circleId) {
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (this.firstInviteEmail != null && this.secondInviteEmail != null) {
            if (!this.firstInviteEmail.equals(this.secondInviteEmail)) {
                Invitations invite = new Invitations(circleId, this.firstInviteEmail, currentEmail);
                mDatabase.child("invitations").push().setValue(invite);
                Invitations secondInvite = new Invitations(circleId, this.secondInviteEmail, currentEmail);
                mDatabase.child("invitations").push().setValue(secondInvite);
            } else {
                Invitations invite = new Invitations(circleId, this.firstInviteEmail, currentEmail);
                mDatabase.child("invitations").push().setValue(invite);
            }
        } else if(this.firstInviteEmail != null && this.secondInviteEmail == null) {
            Invitations invite = new Invitations(circleId, this.firstInviteEmail, currentEmail);
            mDatabase.child("invitations").push().setValue(invite);
        } else if(this.firstInviteEmail == null && this.secondInviteEmail != null) {
            Invitations secondInvite = new Invitations(circleId, this.secondInviteEmail, currentEmail);
            mDatabase.child("invitations").push().setValue(secondInvite);
        } else {
            // both are null thus no invites need to be sent.
            return;
        }
    }







}
