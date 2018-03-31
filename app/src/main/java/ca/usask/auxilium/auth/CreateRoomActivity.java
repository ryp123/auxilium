package ca.usask.auxilium.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    GoogleSignInAccount mAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        } else {
            this.firstInviteEmail = email;
        }
        emailInput = (EditText) findViewById(R.id.friendEmail2);
        email = emailInput.getText().toString();
        if (this.isEmailTheDefaultValue(email)) {
            this.secondInviteEmail = null;
        } else if (!this.isEmailValid(email)) {
            emailInput.setError("Invalid email");
            return;
        } else {
            this.secondInviteEmail = email;
        }

        // set room info
        EditText circleInfo = (EditText) findViewById(R.id.roomInfoText);
        String circleInfoTxt = circleInfo.getText().toString();
        mCircle.setCircleInfo(circleInfoTxt);

        // get db/account info reference
        mAcct = GoogleSignIn.getLastSignedInAccount(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        updateDatabase();

        finish();
    }

     public void updateDatabase(){
         String circleId= mDatabase.push().getKey();
         String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
         mDatabase.child("circles").child(circleId).child("name").setValue(mCircle.getCircleName());
         mDatabase.child("circles").child(circleId).child("diagnosis").setValue(mCircle.getAilment());
         mDatabase.child("users").child(userId)
                 .child("circles").child(circleId).child("role").setValue("Index");
         mDatabase.child("users").child(userId)
                 .child("lastCircleOpen").setValue(circleId);
         HashMap<String, HashMap<String, String>> member = new HashMap<>();
         HashMap <String, String> memberDetails = new HashMap<>();
         memberDetails.put("role", "Index");
         memberDetails.put("status", "Active");
         member.put(userId, memberDetails);
         mDatabase.child("circleMembers").child(circleId).setValue(member);
         String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
         if (this.firstInviteEmail != null && this.secondInviteEmail != null) {
             if (!this.firstInviteEmail.equals(this.secondInviteEmail)) {
                 Invitations invite = new Invitations(circleId, this.firstInviteEmail, currentEmail);
                 mDatabase.child("invitations").push().setValue(invite);
                 Invitations secondInvite = new Invitations(circleId, this.secondInviteEmail, currentEmail);
                 mDatabase.child("invitations").push().setValue(secondInvite);
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

    private boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isEmailTheDefaultValue(String email) {
        String defaultValue = "Email address";
        return email.equals(defaultValue);

    }







}
