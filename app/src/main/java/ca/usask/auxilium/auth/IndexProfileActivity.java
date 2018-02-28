package ca.usask.auxilium.auth;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class IndexProfileActivity extends AppCompatActivity {

    private FirebaseUser fUser;

    //ImageView imgProfilePic;
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

        Log.d("PROFILE EMAIL/ID", userId);
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
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
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivity(new Intent(getBaseContext(), ProfileEditActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
