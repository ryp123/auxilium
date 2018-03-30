package ca.usask.auxilium.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.joda.time.DateTime;

import java.util.HashMap;

import ca.usask.auxilium.Invitations;
import ca.usask.auxilium.MainActivity;
import ca.usask.auxilium.R;

/**
 * Created by gongcheng on 2018-01-29.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_CREATE_PROFILE = 42;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        //Change the text in Google Sign In button to "Sign in with Google"
        setGooglePlusButtonText((SignInButton) findViewById(R.id.sign_in_button),"Sign in with Google");

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        if(currentUser != null){
            checkFirstTimeLogin();
        }
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                checkFirstTimeLogin();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
        else if(requestCode == RC_CREATE_PROFILE && resultCode == RESULT_OK){
            // user registered on the registration screen
            checkCircleInvitesAndStatus();
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                        checkFirstTimeLogin();
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(null);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

   public void onCreateJoinButtonClick(View v){
        Intent intent = new Intent(getBaseContext(), WelcomeJoinCreateActivity.class);
        startActivity(intent);
   }

   public void onMainActivityButtonClick(View v){
       startActivity(new Intent(getBaseContext(), MainActivity.class));
   }


   public void checkFirstTimeLogin(){
       FirebaseUser user = mAuth.getCurrentUser();
       if(user != null) {
           DatabaseReference db = FirebaseDatabase.getInstance().getReference();
           db.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if(!dataSnapshot.exists()){
                       startActivityForResult(new Intent(getBaseContext(), ProfileEditActivity.class), RC_CREATE_PROFILE);
                   } else {
                       checkCircleInvitesAndStatus();
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   Log.e("FirebaseError", databaseError.getDetails());
               }
           });
       }
   }

   public void checkCircleInvitesAndStatus(){
       final FirebaseUser user = mAuth.getCurrentUser();
       if(user != null) {
           FirebaseDynamicLinks.getInstance()
                   .getDynamicLink(getIntent())
                   .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                       @Override
                       public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                           Uri deepLink = null;
                           if (pendingDynamicLinkData != null) {
                               deepLink = pendingDynamicLinkData.getLink();
                               Toast.makeText(getBaseContext(),
                                       "Processing Invitation",
                                       Toast.LENGTH_LONG).show();
                               final String invitationId = deepLink.getQueryParameter("invitationId");
                               Log.d("invitations", "Id:" + invitationId);
                               final DatabaseReference db = FirebaseDatabase.getInstance()
                                                                            .getReference();
                               db.addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dbRef) {
                                               Invitations invitation = dbRef.child("invitations")
                                                       .child(invitationId)
                                                       .getValue(Invitations.class);
                                               Log.d("invitations", "Proof not null: " + invitation.getEmail());
                                               if (invitation == null) {
                                                   Toast.makeText(getBaseContext(),
                                                           "Failed to process invitation.",
                                                           Toast.LENGTH_LONG).show();
                                                   redirectToActivity(db);
                                               } else {
                                                   if (isInvitationValid(invitation, invitation.getSenderEmail())) {
                                                       String circleId = invitation.getCircleId();
                                                       Log.d("invitations", "circle:" + circleId);
                                                       String userId = user.getUid();
                                                       Log.d("invitations", "userId:" + userId);
                                                       String concernedChatId = getConcernedChatId(dbRef, circleId);
                                                       Log.d("invitations", "concernedChat:" + concernedChatId);
                                                       updateUserWithCircleId(db,circleId, userId);
                                                       updateCircleMembers(db,circleId, userId);
                                                       updateConcernedPartyMembers(db,concernedChatId, userId);
                                                       updateUserWithConcernedChatId(db, concernedChatId, userId);
                                                       if (!islastOpenedCircleSet(dbRef,userId)) {
                                                           updateLastOpenedCircle(db,userId,circleId);
                                                       }
                                                       deleteUsedInvitation(db, invitationId);
                                                       redirectToActivity(db);
                                                   } else {
                                                       redirectToActivity(db);
                                                   }
                                               }
                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {
                                                Log.e("invitations", databaseError.getDetails());
                                           }
                                       });
                           }
                       }
                   })
                   .addOnFailureListener(this, new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w(TAG, "getDynamicLink:onFailure", e);
                           DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                           redirectToActivity(db);
                       }
                   });
       }
   }

    private boolean isInvitationValid(Invitations invite,
                                      String currentUsersEmail) {
        Log.d("invitations", "got here 101");
        DateTime expirationDate = new DateTime(invite.getExpirationDate());
        Log.d("invitations", "date:" + expirationDate.toString());
        if (!invite.getSenderEmail().equals(currentUsersEmail)) {
            Toast.makeText(getBaseContext(),
                    "Failed to process invitation email. Invalid email detected.",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (expirationDate.isAfterNow()) {
            Toast.makeText(getBaseContext(),
                    "Failed to process invitation email. Past expiration date.",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.d("invitations", "Got here!");
            return true;
        }
    }

    private void updateUserWithCircleId(DatabaseReference dbRef,
                                        String circleId,
                                        String currentUserId) {
        HashMap<String, HashMap<String, String>> userCircleListing = new HashMap();
        HashMap<String, String> circleRole = new HashMap();
        circleRole.put("role", "Concerned");
        userCircleListing.put(circleId, circleRole);

        dbRef.child("users")
                .child(currentUserId)
                .child("circles")
                .setValue(userCircleListing);
    }

    private void updateUserWithConcernedChatId(DatabaseReference dbRef,
                                               String concernedPartyId,
                                               String currentUserId) {
        HashMap<String, Boolean> concernedChat = new HashMap();
        concernedChat.put(concernedPartyId, true);
        dbRef.child("users")
                .child(currentUserId)
                .child("concernedChat")
                .setValue(concernedChat);
    }

    private void updateCircleMembers(DatabaseReference dbRef,
                                     String circleId,
                                     String currentUserId) {
        HashMap<String, HashMap<String, String>> circleMember = new HashMap();
        HashMap<String, String> circleMemberRole = new HashMap();
        circleMemberRole.put("role", "Concerned");
        circleMemberRole.put("status", "Active");
        circleMember.put(currentUserId, circleMemberRole);
        dbRef.child("circleMembers")
                .child(circleId)
                .setValue(circleMember);
    }

    private void updateConcernedPartyMembers(DatabaseReference dbRef,
                                             String concernedChatId,
                                             String currentUserId) {
        HashMap<String, Boolean> concernedPartyMember = new HashMap();
        concernedPartyMember.put(currentUserId, true);
        dbRef.child("concernedPartyMember")
                .child(concernedChatId)
                .setValue(concernedPartyMember);
    }

    private String getConcernedChatId(DataSnapshot dbRef,
                                      String circleId) {
        for (DataSnapshot children: dbRef.child("concernedCircle").getChildren()) {
            // if the concerned chat is related to the given circle id we want that cp chat id.
            if (children.child("relatedTo").getValue().equals(circleId)) {
                // get the concerned chat id.
                return children.getKey();
            }
        }
        return null;
    }

    private void deleteUsedInvitation(DatabaseReference dbRef, String invitationId) {
        dbRef.child("invitations").child(invitationId).setValue(null);
    }

    private boolean islastOpenedCircleSet(DataSnapshot dbRef, String userId) {
        return dbRef.child("users")
                .child(userId)
                .child("lastCircleOpen").exists();
    }

    private void updateLastOpenedCircle(DatabaseReference db, String userId, String circleId) {
       db.child("users").child(userId).child("lastCircleOpen").setValue(circleId);
    }

    private void redirectToActivity(DatabaseReference db) {
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.child("users").child(user.getUid()).child("circles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            // user is already a part of a circle
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                        } else {
                            // user is not a part of any circle
                            startActivity(new Intent(getBaseContext(), CreateRoomActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }




}
