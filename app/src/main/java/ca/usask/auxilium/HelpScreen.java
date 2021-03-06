package ca.usask.auxilium;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class HelpScreen extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private View myView;
    private String condition;
    private String currentCircle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_help_screen, container, false);
        getActivity().setTitle("Help and Resources");
        listView = (ListView) myView.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


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
                                .child("diagnosis")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String curDiagnosis = dataSnapshot.getValue(String.class);
                                        condition = curDiagnosis;
                                        if(curDiagnosis == null){
                                            Log.d("HelpScreen:createView", "circle diagnosis is null");
                                        }
                                        root.child("conditions")
                                                .child(condition)
                                                .child("resources")
                                                .addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        appendResource(dataSnapshot);
                                                    }


                                                    @Override
                                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                                        appendResource(dataSnapshot);
                                                    }


                                                    @Override
                                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Log.d("HelpScreen:createView", "resources not found");

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("HelpScreen:createView", "error querying diagnosis");
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void appendResource(DataSnapshot dataSnapshot) {

        String m = dataSnapshot.getValue(String.class);
        arrayAdapter.add(m);
        listView.setSelection(arrayAdapter.getCount() - 1);
    }
}






