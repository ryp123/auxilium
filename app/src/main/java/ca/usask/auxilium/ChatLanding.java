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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ChatLanding extends AppCompatActivity {

    private Button send;
    private EditText message;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_messages = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_landing);

        send = (Button) findViewById(R.id.btn_add_room);
        message = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        listView.setAdapter(arrayAdapter);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                map.put("Message: ","");
                String newMessage = message.getText().toString();

                //Post message to screen
                arrayAdapter.add(newMessage);

                //Add to database -- change later to make it adaptive.  Find a way to auto increment
                root.child("Circles").child("Circle 2").child("Chat").child("Concerned Group").updateChildren(map);
                root.child("Circles").child("Circle 2").child("Chat").child("Concerned Group").child(newMessage).setValue(newMessage);
                //Clear the message box
                message.setText("");


            }
        });







    }




}

