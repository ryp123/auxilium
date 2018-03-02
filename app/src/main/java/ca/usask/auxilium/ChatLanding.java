package ca.usask.auxilium;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatLanding extends AppCompatActivity {

    private Button send;
    private EditText message;
    private String senderName = "null";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String chat_msg,chat_user_name;
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


        root.child("messages").child("testCircle").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

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



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              Message newMessage = new Message(senderName, message.getText().toString());

              root.child("messages").child("testCircle").push().setValue(newMessage);


              message.setText("");

                /**
                Map<String,Object> map = new HashMap<String, Object>();
                String newMessage = message.getText().toString();

                //Post message to screen
                arrayAdapter.add(newMessage);

                //Add to database -- change later to make it adaptive.  Find a way to auto increment
                root.child("Circles").child("Circle 2").child("Chat").child("Concerned Group").updateChildren(map);
                root.child("Circles").child("Circle 2").child("Chat").child("Concerned Group").child(newMessage).setValue(newMessage);


                //Clear the message box
                message.setText(""); */


            }
        });




        }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Message m = dataSnapshot.getValue(Message.class);

        arrayAdapter.add(m.getSender() + " : " + m.getMessage());
//        Iterator i = dataSnapshot.getChildren().iterator();
//
//        while (i.hasNext()){
//
//            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
//            //chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
//
//            //chat_conversation.append(chat_user_name +" : "+chat_msg +" \n");
//            arrayAdapter.add(chat_msg + "\n");
//        }



    }
}






