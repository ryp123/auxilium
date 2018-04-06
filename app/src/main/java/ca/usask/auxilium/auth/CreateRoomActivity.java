package ca.usask.auxilium.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.Set;
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

    private String [] emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CircleActivity", "new instance created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        setupUI(findViewById(R.id.parent_create_room));

        //Set the default items for the ailment drop down
        Spinner spinner = (Spinner) findViewById(R.id.ailmentSpinner);
        String[] spinItems = new String[]{"Alcoholism", "Depression", "Drug Addiction"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinItems);
        spinner.setAdapter(adapter);
        final EditText emailInput = (EditText) findViewById(R.id.friendEmail1);
        final String hint = "Comma delimited list of emails.";
        emailInput.setText(hint);
        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean hasFocus) {
                if (hasFocus) {
                    Log.d("invitations", "set text to empty?");
                    emailInput.setHint(hint);
                    emailInput.setText("");
                } else {
                    emailInput.setText(hint);
                }
            }
        });

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
        Log.d("Invitations", "Getting the emails to send invitations to");
        final String circleCreatorEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        EditText emailInput = (EditText) findViewById(R.id.friendEmail1);
        String emailParts = emailInput.getText().toString();
        String[] emails = emailParts.split(",");
        List<String> emailList = Arrays.asList(emails);
        Set<String> emailSet = new LinkedHashSet<String>(emailList);
        for (String email: emailSet) {
            Log.d("invitations", "The email I processed is: " + email);
            if (isEmailTheDefaultValue(email)) {
                emailSet.remove(email);
            } else if (circleCreatorEmail.equals(email)) {
                Toast.makeText(getBaseContext(),
                          "You cannot send an email to yourself!",
                               Toast.LENGTH_LONG).show();
                return;
            } else if (isEmailValid(email)) {
               continue;
            } else {
                Toast.makeText(getBaseContext(),
                               "The email " + email + " is invalid.",
                                Toast.LENGTH_LONG).show();
                return;
            }
        }
        final Set<String> validEmails = emailSet;
        /////////notification///////
        FirebaseMessaging.getInstance().subscribeToTopic(circleId);

        /////end of notification/////////

        mDatabase.updateChildren(getUpdateTasks(circleId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("CircleActivity", "Created new circle!");
                    processInvitations(validEmails, circleId, circleCreatorEmail);
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
        String defaultValue = "Comma delimited list of emails.";
        return email.equals(defaultValue) || email.isEmpty();

    }

    private void processInvitations(Set<String> emailSet, String circleId, String circleCreatorEmail) {

        //get email information

        for (String email: emailSet) {
            sendInvitation(new Invitations(circleId, email, circleCreatorEmail));
        }
    }


    private void sendInvitation(Invitations invite) {
        if (invite == null) {
            return;
        } else {
            mDatabase.child("invitations").push().setValue(invite);
        }
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    BaseActivity.hideSoftKeyboard(CreateRoomActivity.this);
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
