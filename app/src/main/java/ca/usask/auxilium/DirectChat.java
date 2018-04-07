package ca.usask.auxilium;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.usask.auxilium.auth.BaseActivity;


public class DirectChat extends Fragment {

    private Button send;
    private EditText message;
    private String senderName = "null";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String roomType1;
    private String roomType2;
    private String talkingTo;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_chat_landing, container,false);


        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser != null){
            Bundle args = getArguments();
            String value = args.getString("USER_ID");

            roomType1 = fUser.getUid() + "_" + value;
            roomType2 = value + "_" + fUser.getUid();
            root.child("users").child(value).addListenerForSingleValueEvent(new ValueEventListener(){
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        talkingTo = user.getPreferredName();
                        getActivity().setTitle(talkingTo);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FirebaseError", databaseError.getDetails());
                }
            });


        }

        send = (Button) myView.findViewById(R.id.btn_add_room);
        message = (EditText) myView.findViewById(R.id.room_name_edittext);
        listView = (ListView) myView.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                BaseActivity.hideSoftKeyboard(getActivity());
            }
        });


        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            User user = dataSnapshot.getValue(User.class);
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

                String messageTobeSent = message.getText().toString().trim();
                if (messageTobeSent.length() > 1000) {
                    message.setError("Message exceeded the 1000 maximum character length.");

                } else {
                    final Message newMessage = new Message(senderName, messageTobeSent);
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




            }
        });


        return myView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Message m = dataSnapshot.getValue(Message.class);
        if(m.getMessage() != null && m.getMessage().trim().length() != 0)
        {
            arrayAdapter.add(m.getSender() + " : " + m.getMessage());
            listView.setSelection(arrayAdapter.getCount() - 1);
        }
    }
}

