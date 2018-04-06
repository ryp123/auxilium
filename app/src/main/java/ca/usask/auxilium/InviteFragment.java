package ca.usask.auxilium;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jadenball on 2018-04-05.
 */

public class InviteFragment extends Fragment {

    View myView;
    Button btnInvite;
    EditText emailInput;

    private DatabaseReference mDatabase;

    private Circle mCircle;

    private String [] emails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_invite, container,false);
        //imgProfilePic = findViewById(R.id.profilePicture);
        btnInvite = myView.findViewById(R.id.btn_invite);
        emailInput = (EditText) myView.findViewById(R.id.edit_emails);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("lastCircleOpen")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String curCircle = dataSnapshot.getValue(String.class);
                                Toast.makeText(getContext(), "Sending invites...", Toast.LENGTH_SHORT).show();
                                processInvitations(curCircle);
                                Toast.makeText(getContext(), "Invite(s) sent!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Firebase Error", "Current Circle doesn't exist");
                            }
                        });
            }
        });

        getActivity().setTitle("Invite");
        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //get email information
        Log.d("Invitations", "Getting the emails to send invitations to");
        String circleCreatorEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailParts = emailInput.getText().toString();
        String[] emails = emailParts.split(",");
        List<String> emailList = Arrays.asList(emails);
        Set<String> emailSet = new LinkedHashSet<String>(emailList);
        for (String email: emailSet) {
            if (isEmailTheDefaultValue(email)) {
                sendInvitation(null);
            } else if (circleCreatorEmail.equals(email)) {
                sendInvitation(null);
            } else if (isEmailValid(email)) {
                sendInvitation(new Invitations(circleId, email, circleCreatorEmail));
            } else {
                sendInvitation(null);
            }
        }
        emailInput.setText("");
    }


    private void sendInvitation(Invitations invite) {
        if (invite == null) {
            return;
        } else {
            mDatabase.child("invitations").push().setValue(invite);
        }
    }
}
