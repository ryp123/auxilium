package ca.usask.auxilium.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.R;

public class CreateRoomActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
    }

    private void addRoomName(){

    }

    public void  do_click(View v){
        EditText edt = (EditText)findViewById(R.id.roomNameText);
        String CircleName = edt.getText().toString();

        Circle circle = new Circle();

        circle.setCircleName(CircleName);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Circles").child("Circle 3").setValue(circle);

    }


}
