package ca.usask.auxilium;

import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import ca.usask.auxilium.auth.ProfileFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static boolean isAppRunning;


    NavigationView navigationView;
    ArrayList<MenuItem> mMenuItems = new ArrayList<>();
    ArrayList<String> mUsers = new ArrayList<>();
    private String currentCircle = "null";
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //notification stuff march 29--------------------------------------------

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2", NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);
        }

            //notification ends  -----------------------------------------------


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


          root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastCircleOpen")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String curCircle = dataSnapshot.getValue(String.class);
                        currentCircle = curCircle;
                        root.child("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("circles")
                                .child(currentCircle)
                                .child("role")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String userType = dataSnapshot.getValue(String.class);
                                        if(userType.equals("Index")){
                                            String timeStamp = new SimpleDateFormat("yyyy/MM/dd @ HH:mm:ss").format(Calendar.getInstance().getTime());
                                            root.child("circles")
                                                    .child(currentCircle)
                                                    .child("indexStatus")
                                                    .child("lastUsed")
                                                    .setValue(timeStamp);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("Firebase Error", "Current Circle doesn't exist");
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
            // pass in the fuid of the user you are chatting with
            int userId = getCheckedUserNavItem();
            ProfileFragment profileFragment = new ProfileFragment();
            if(userId != -1) {
                // pass to fragment the fuid of the users profile we are direct chatting with
                Bundle args = new Bundle();
                args.putString("USER_ID", mUsers.get(userId));
                profileFragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, profileFragment).commit();
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
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new IndexStatusActivity()).commit();

        } else if (id == R.id.nav_helpscreen) {
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new HelpScreen()).commit();

        } else if (id == R.id.nav_indexchat){
            fragmentManager2.beginTransaction().replace(R.id.content_frame, new IndexChat()).commit();
        } else if (id == R.id.nav_concernchat){
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
        menuItem.setCheckable(true);
        mMenuItems.add(menuItem);

        mUsers.add(userId);

        return true;
    }

    /* Returns index of checked user item, or -1 if none is selected*/
    private int getCheckedUserNavItem(){
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getGroupId() == R.id.users && item.isChecked()) {
                return item.getItemId();
            }
        }
        return -1;
    }


    //-------notification stuff------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.isAppRunning = false;
    }
    //-------notification stuff ends--------------------------------------



}
