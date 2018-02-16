package ca.usask.auxilium.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ca.usask.auxilium.R;

public class IndexProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_profile_page);
        mAuth = FirebaseAuth.getInstance();

        ImageView imgProfilePic = findViewById(R.id.profilePicture);
        TextView txtFirstName = findViewById(R.id.indexFirstName);
        TextView txtLastName = findViewById(R.id.indexLastName);
        TextView txtPrefName = findViewById(R.id.indexPrefName);
        TextView txtAge = findViewById(R.id.indexAge);
        TextView txtGender = findViewById(R.id.indexGender);
        TextView txtDiagnosis = findViewById(R.id.indexDiagnosis);
        TextView txtEmergencyContact = findViewById(R.id.emergencyContact);

        if(mAuth.getCurrentUser() != null) {
            //user logged in
            Log.d("AUTH", mAuth.getCurrentUser().getEmail());

            imgProfilePic.setImageURI(mAuth.getCurrentUser().getPhotoUrl());
            txtPrefName.setText(mAuth.getCurrentUser().getDisplayName());
            txtEmergencyContact.setText(mAuth.getCurrentUser().getPhoneNumber());
        }


    }
}
