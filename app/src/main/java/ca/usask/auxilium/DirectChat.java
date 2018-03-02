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


public class DirectChat extends AppCompatActivity {

    private Button send;
    private EditText message;
    private String senderName = "null";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String roomType1 = "5t1zId575gYA1sjpSIFUy3M42fn2" + "_" + "zDESySWUskPLODDtoGLnOtJ8o8J3";
    private String roomType2 = "zDESySWUskPLODDtoGLnOtJ8o8J3" + "_" + "5t1zId575gYA1sjpSIFUy3M42fn2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_landing);
        send = (Button) findViewById(R.id.btn_add_room);
        message = (EditText) findViewById(R.id.room_name_edittext);
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



        root.child("personalMessages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomId;
                if(dataSnapshot.hasChild(roomType2)) roomId = roomType2;
                else roomId = roomType1;

                FirebaseDatabase.getInstance().getReference()
                        .child("personalMessages")
                        .child(roomId)
                        .addChildEventListener(new ChildEventListener() {

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Message newMessage = new Message(senderName, message.getText().toString());


                root.child("personalMessages")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String roomId;
                                if (dataSnapshot.hasChild(roomType2)) roomId = roomType2;
                                else roomId = roomType1;
                                root.child("personalMessages")
                                        .child(roomId)
                                        .push().setValue(newMessage);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                message.setText("");

            }
        });




    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Message m = dataSnapshot.getValue(Message.class);

        arrayAdapter.add(m.getSender() + " : " + m.getMessage());

    }
}






