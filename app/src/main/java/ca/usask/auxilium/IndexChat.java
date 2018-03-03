package ca.usask.auxilium;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class IndexChat extends AppCompatActivity {

    private Button btnMsg1, btnMsg2, btnMsg3;
    private String senderName = "null";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    /** Need these for next deliverable
     private EditText message;

     private String con_inq1 = "How are you doing?";
    private String con_inq2 = "Wishing you the best";
    private String con_inq3 = "I am worried about you";

    private String index_msg1 = "I'm not doing well";
    private String index_msg2 = "Doing fine";
    **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_chat);
        btnMsg1 = (Button) findViewById(R.id.btn_msg1);
        btnMsg2 = (Button) findViewById(R.id.btn_msg2);
        btnMsg3 = (Button) findViewById(R.id.btn_msg3);
        //message = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getPreferredName()  != null)
                        {
                            senderName = user.getPreferredName();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        root.child("messages").child("testCircle").addChildEventListener(new ChildEventListener() {
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



        btnMsg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, btnMsg1.getText().toString());

                root.child("messages").child("testCircle").push().setValue(newMessage);



            }
        });

        btnMsg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, btnMsg2.getText().toString());

                root.child("messages").child("testCircle").push().setValue(newMessage);



            }
        });

        btnMsg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, btnMsg3.getText().toString());

                root.child("messages").child("testCircle").push().setValue(newMessage);



            }
        });




    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Message m = dataSnapshot.getValue(Message.class);

        arrayAdapter.add(m.getSender() + " : " + m.getMessage());

    }
}






