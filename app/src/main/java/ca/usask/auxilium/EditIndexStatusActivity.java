package ca.usask.auxilium;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kocur on 2018-03-29.
 */

//Change user to Index status

public class EditIndexStatusActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private IndexStatus indexStatus;
    private FirebaseUser fUser;

    //ImageView imgProfilePic;

    TextView lastUsed;
    EditText lastSeenBy;
    EditText lastType;
    EditText assesment;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String currentCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_indexstatus);
        setTitle("Update Index Status");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            Log.e("User Error", "user is not logged in");
            return;
        }

        lastUsed = findViewById(R.id.indexLastTimeUsed);
        lastSeenBy = findViewById(R.id.indexLastSeenBy);
        lastType = findViewById(R.id.indexLastSeenVia);
        assesment = findViewById(R.id.indexLastAssessment);


        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastCircleOpen")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String curCircle = dataSnapshot.getValue(String.class);
                        currentCircle = curCircle;
                        root.child("circles")
                                .child(currentCircle)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        indexStatus = dataSnapshot.getValue(IndexStatus.class);
                                        if (indexStatus != null) {
                                            lastUsed.setText(indexStatus.getLastUsed());
                                            lastSeenBy.setText(indexStatus.getLastSeenBy());
                                            lastType.setText(indexStatus.getLastSeenVia());
                                            assesment.setText(indexStatus.getReportedAssessment());
                                        } else {
                                            Log.e("Firebase Error", "Index status is null");
                                            indexStatus = new IndexStatus();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("Firebase Error", "Current Circle doesn't exist");
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /// FIX -- Where is profile_edit_menu?
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_save:
                indexStatus.setLastSeenBy(lastSeenBy.getText().toString());
                indexStatus.setLastSeenVia(lastType.getText().toString());;
                indexStatus.setReportedAssessment(assesment.getText().toString());
                mDatabase.child("circles").child(currentCircle).setValue(indexStatus);
                Toast.makeText(getBaseContext(), "Changes Saved", Toast.LENGTH_LONG).show();
                Log.d("Firebase Save", "Update Saved");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
