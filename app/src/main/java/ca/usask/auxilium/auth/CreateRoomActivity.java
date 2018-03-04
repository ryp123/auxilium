package ca.usask.auxilium.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.usask.auxilium.Circle;
import ca.usask.auxilium.MainActivity;
import ca.usask.auxilium.R;
import ca.usask.auxilium.User;

public class CreateRoomActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Circle mCircle;

    GoogleSignInAccount mAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        //Set the default items for the ailment drop down
        Spinner spinner = (Spinner) findViewById(R.id.ailmentSpinner);
        String[] spinItems = new String[]{"Alcoholic", "Depression", "Drug Addiction"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinItems);
        spinner.setAdapter(adapter);
    }

    public void  onCreateButtonClick(View v) {
        EditText edt = (EditText) findViewById(R.id.roomNameText);

        String circleName = edt.getText().toString();

        Log.d("the name of circle being created",circleName);

        mCircle = new Circle();
        mCircle.setCircleName(circleName);

        Spinner spinner = (Spinner) findViewById(R.id.ailmentSpinner);

        mCircle.setAilment(spinner.getSelectedItem().toString());

        EditText circleInfo = (EditText) findViewById(R.id.roomInfoText);
        String circleInfoTxt = circleInfo.getText().toString();
        mCircle.setCircleInfo(circleInfoTxt);

        mCircle.setAilment(spinner.getSelectedItem().toString());

        mAcct = GoogleSignIn.getLastSignedInAccount(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        updateDatabase();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("CircleName", mCircle.getCircleName());

        startActivity(intent);

    }

     public void updateDatabase(){

         mDatabase.child("circles").child(mDatabase.push().getKey()).child("name").setValue(mCircle.getCircleName());



         if (mAcct != null) {
             
             mDatabase.child("circles").child(mDatabase.push().getKey()).child("name").child(mCircle.getCircleName());

         }


    }



}
