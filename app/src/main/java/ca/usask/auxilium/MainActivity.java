package ca.usask.auxilium;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ca.usask.auxilium.auth.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    ArrayList<MenuItem> mMenuItems = new ArrayList<>();

    ArrayList<String> mUsers = new ArrayList<>();

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getAllUsersFromFirebase();

        fab.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new IndexChat()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            // Google sign out
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getBaseContext(), "Signed Out", Toast.LENGTH_LONG).show();
                        }
                    });
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("groupid: ", Integer.toString(item.getGroupId()));
        Log.d("item: ", Integer.toString(item.getItemId()));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();


        if(item.getGroupId() == R.id.users){
            String userId = mUsers.get(id);
            // To pass some value from FragmentA
            DirectChat mDirectChat = new DirectChat();
            Bundle args = new Bundle();
            args.putString("USER_ID", userId);
            mDirectChat.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content_frame, mDirectChat).commit();
        }
        FragmentManager fragmentManager2 = getFragmentManager();
        if (id == R.id.nav_profile_page) {
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            // Handle the camera action
        } else if (id == R.id.nav_indexstatus) {
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();

        } else if (id == R.id.nav_helpscreen) {
            fab.setVisibility(View.GONE);
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new HelpScreen()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_indexchat){
            fab.setVisibility(View.GONE);
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new IndexChat()).commit();
        } else if (id == R.id.nav_concernchat){
            fab.setVisibility(View.GONE);
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new ChatLanding()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart();
    }



    public void getAllUsersFromFirebase() {


        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren()
                                .iterator();

                        final List<User>  users = new ArrayList<>();
                        ArrayList<String> userIds = new ArrayList<>();

                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            User user = dataSnapshotChild.getValue(User.class);
                            users.add(user);
                            userIds.add(dataSnapshotChild.getKey());
                        }
                        // All users are retrieved except the one who is currently logged
                        // in device.

                        for (int i = 0; i < users.size(); i++) {
                            Log.d("*** USER NAME: ", users.get(i).getPreferredName());
                            Log.d("*** USER ID: ", userIds.get(i));
                            addNewUser(users.get(i), userIds.get(i));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Unable to retrieve the users.
                    }

                });



    }


    public boolean addNewUser(User user, String userId){
        Menu menu = navigationView.getMenu();
        MenuItem menuItem;
        menuItem = menu.add(R.id.users,mMenuItems.size(),mMenuItems.size(),user.getPreferredName());
        mMenuItems.add(menuItem);

        mUsers.add(userId);

        return true;
    }


}
