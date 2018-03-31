package ca.usask.auxilium;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.MotionEvent;
import android.graphics.Color;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * Created by Kocur on 2018-03-29.
 */

public class IndexStatusActivity extends Fragment {

    private TextView lastUsed;
    private TextView lastSeenBy;
    private TextView lastType;
    private TextView assesment;
    private View myView;
    private FirebaseUser fUser;
    private String userId;
    private IndexStatus indexStatus;
    private String currentCircle;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.index_status_page, container,false);
        lastSeenBy= myView.findViewById(R.id.indexLastSeenBy);
        lastUsed = myView.findViewById(R.id.indexLastTimeUsed);
        lastType = myView.findViewById(R.id.indexLastSeenVia);
        assesment = myView.findViewById(R.id.indexLastAssessment);
        lastUsed = myView.findViewById(R.id.indexLastTimeUsed);
        getActivity().setTitle("Index Status");


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
                                .child("indexStatus")
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




        return myView;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            Log.e("User Error", "user is not logged in");
            return;
        }

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
                startActivity(new Intent(getContext(), EditIndexStatusActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
