package ca.usask.auxilium;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HelpScreen extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


        //This is hardcoded untilthe navigation is set up (Deliverable 5).
        root.child("conditions").child("Depression").child("resources").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                appendChatConversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        String m = dataSnapshot.getValue(String.class);
        arrayAdapter.add(m);
        listView.setSelection(arrayAdapter.getCount() - 1);
    }
}






