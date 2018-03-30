package ca.usask.auxilium;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ChatLanding extends Fragment {

    private View myView;
    private Button send;
    private EditText message;
    private String senderName = "null";
    private String currentCircle = "null";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Concerned Members");
        myView = inflater.inflate(R.layout.activity_chat_landing, container, false);
        send = (Button) myView.findViewById(R.id.btn_add_room);
        message = (EditText) myView.findViewById(R.id.room_name_edittext);
        listView = (ListView) myView.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastOpenCircle")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String curCircle = dataSnapshot.getValue(String.class);
                        currentCircle = curCircle;
                        root.child("concernedMessages")
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

        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getPreferredName() != null) {
                            senderName = user.getPreferredName();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Firebase Error", "user doesn't have a preffered name");
                    }
                });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Message newMessage = new Message(senderName, message.getText().toString());
                root.child("concernedMessages").child(currentCircle).push().setValue(newMessage);
                message.setText("");

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






