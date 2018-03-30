package ca.usask.auxilium;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;


public class IndexChat extends Fragment {

    private Button btnMsg1, btnMsg2, btnMsg3;
    private String senderName = "null";
    private String senderFUid = "null";
    private ListView listView;
    //private ArrayAdapter<String> arrayAdapter;
    private IndexAdapter arrayAdapter;
    private ArrayList<IndexListItem> arrayList;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private View myView;
    private String con_inq1 = "How are you doing?";
    private String con_inq2 = "Wishing you the best";
    private String con_inq3 = "I am worried about you";
    private String currentCircle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_index_chat, container,false);
        getActivity().setTitle("Index Chat");
        btnMsg1 = (Button) myView.findViewById(R.id.btn_msg1);
        btnMsg2 = (Button) myView.findViewById(R.id.btn_msg2);
        btnMsg3 = (Button) myView.findViewById(R.id.btn_msg3);
        btnMsg1.setText(con_inq1);
        btnMsg2.setText(con_inq2);
        btnMsg3.setText(con_inq3);
        //message = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) myView.findViewById(R.id.chat_listView);
        //arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1);
        arrayList = new ArrayList<>();
        arrayAdapter = new IndexAdapter(this.getActivity(), arrayList);
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
                        senderFUid = dataSnapshot.getKey();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastOpenCircle")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String curCircle = dataSnapshot.getValue(String.class);
                        currentCircle = curCircle;
                        root.child("messages")
                                .child(currentCircle).addChildEventListener(new ChildEventListener() {
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
                        Log.d("Firebase Error", "user doesn't have a current circle");
                    }
                });

/*

        root.child("messages").child("testCircle").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


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
*/



        btnMsg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, senderFUid, btnMsg1.getText().toString());

                root.child("messages").child(currentCircle).push().setValue(newMessage);



            }
        });

        btnMsg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, senderFUid, btnMsg2.getText().toString());

                root.child("messages").child(currentCircle).push().setValue(newMessage);



            }
        });

        btnMsg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Message newMessage = new Message(senderName, senderFUid, btnMsg3.getText().toString());

                root.child("messages").child(currentCircle).push().setValue(newMessage);



            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IndexListItem item = (IndexListItem) listView.getItemAtPosition(i);
                item.expanded = !item.expanded;
                arrayAdapter.notifyDataSetChanged();
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

        boolean msgInList = false;
        // check if someone already sent the message
        for(IndexListItem item : arrayList){
            if(item.getMsg().equals(m.getMessage())){
                item.addCount(m.getSender(), m.getSenderFUid());
                msgInList = true;
                break;
            }
        }
        if(!msgInList) {
            arrayList.add(new IndexListItem(m.getMessage(), m.getSender(), m.getSenderFUid()));
        }

        arrayAdapter.notifyDataSetChanged();
        listView.setSelection(arrayAdapter.getCount() - 1);
    }
}
