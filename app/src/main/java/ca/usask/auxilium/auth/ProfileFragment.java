package ca.usask.auxilium.auth;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class ProfileFragment extends Fragment {

    private FirebaseUser fUser;
    View myView;
    private String userId;

    //ImageView imgProfilePic;
    TextView txtFirstName;
    TextView txtLastName;
    TextView txtPrefName;
    TextView txtAge;
    TextView txtGender;
    TextView txtEmergencyContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.index_profile_page, container,false);
        //imgProfilePic = findViewById(R.id.profilePicture);
        txtFirstName = myView.findViewById(R.id.indexFirstName);
        txtLastName = myView.findViewById(R.id.indexLastName);
        txtPrefName = myView.findViewById(R.id.indexPrefName);
        txtAge = myView.findViewById(R.id.indexAge);
        txtGender = myView.findViewById(R.id.indexGender);
        txtEmergencyContact = myView.findViewById(R.id.emergencyContact);
        getActivity().setTitle("Profile");
        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            Log.e("User Error", "user is not logged in");
            return;
        }

        // get the user Id whose profile we are going to view
        Bundle args = getArguments();
        if(args != null) {
            userId = args.getString("USER_ID");
            if (userId == null) {
                userId = fUser.getUid();
            }
        } else {
            userId = fUser.getUid();
        }

    }


    private void populateViewsFromDb(){
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(userId.equals(fUser.getUid())){
            // add the edit option to the menu
            inflater.inflate(R.menu.profile_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivity(new Intent(getContext(), ProfileEditActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        populateViewsFromDb();
        super.onResume();
    }
}
