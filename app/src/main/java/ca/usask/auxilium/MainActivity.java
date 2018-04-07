package ca.usask.auxilium;

import android.app.FragmentManager;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import ca.usask.auxilium.auth.BaseActivity;
import ca.usask.auxilium.auth.ProfileEditActivity;
import ca.usask.auxilium.auth.ProfileFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class  MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static volatile boolean isAppRunning;
    GoogleSignInAccount mGoogleSignInAccount;

    NavigationView navigationView;
    ArrayList<MenuItem> mMenuItems = new ArrayList<>();
    ArrayList<String> mUsers = new ArrayList<>();

    ArrayList<MenuItem> mCircleMenuItems = new ArrayList<>();
    ArrayList<String> mCircleIDs = new ArrayList<>();

    HashMap<String,String>  mCirclename_ID = new HashMap<>();
    ArrayList<String> mCircleNames = new ArrayList<>();


    String role = "null";

    private String currentCircle = "null";
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI(findViewById(R.id.drawer_layout));


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                BaseActivity.hideSoftKeyboard(MainActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                BaseActivity.hideSoftKeyboard(MainActivity.this);
            }
        };


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


                                            navigationView.getMenu().findItem(R.id.nav_concernchat).setVisible(false);
                                            navigationView.getMenu().findItem(R.id.nav_indexstatus).setVisible(false);

                                        } else {
                                            navigationView.getMenu().findItem(R.id.nav_concernchat).setVisible(true);
                                            navigationView.getMenu().findItem(R.id.nav_indexstatus).setVisible(true);

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
        getCirclesFromFirebase();


        fab.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new IndexChat()).commit();


        // set profile picture and text on the drawer-------------------------
        mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        final TextView mDisplayname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        mDisplayname.setText(mGoogleSignInAccount.getDisplayName());
        ImageView mImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);

    try {
         Uri uri = mGoogleSignInAccount.getPhotoUrl();
         mImageView.setImageBitmap(BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream()));

        }catch (MalformedURLException e){
               e.printStackTrace();
        }catch (IOException e){
                 e.printStackTrace();
        }


        root.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getPreferredName()  != null)
                        {
                            mDisplayname.setText(user.getPreferredName());
                        }else {
                            mDisplayname.setText(mGoogleSignInAccount.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //----------------------------------------------------------------


        ImageView iv  = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.switch_circle_button);
        iv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {

                                      if(navigationView.getMenu().getItem(0).isVisible()) {
                                          navigationView.getMenu().setGroupVisible(0, false);
                                          navigationView.getMenu().getItem(6).setVisible(false);
                                          navigationView.getMenu().getItem(7).setVisible(true);




                                      }else{
                                          navigationView.getMenu().setGroupVisible(0, true);
                                          navigationView.getMenu().getItem(6).setVisible(true);
                                          navigationView.getMenu().getItem(7).setVisible(false);

                                      }


                                      root.child("users")
                                              .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                              .child("circles")
                                              .child(currentCircle)
                                              .child("role")
                                              .addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                      role = dataSnapshot.getValue().toString();
                                                      if (role.equals("Index")){
                                                          navigationView.getMenu().findItem(R.id.nav_concernchat).setVisible(false);
                                                          navigationView.getMenu().findItem(R.id.nav_indexstatus).setVisible(false);
                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(DatabaseError databaseError) {
                                                  }
                                              });

                                  }
                              }

        );



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

        BaseActivity.hideSoftKeyboard(MainActivity.this);

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

        if (id == R.id.nav_profile_page) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            // Handle the camera action
        } else if (id == R.id.nav_indexstatus) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new IndexStatusActivity()).commit();

        } else if (id == R.id.nav_helpscreen) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HelpScreen()).commit();

        } else if (id == R.id.nav_indexchat){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new IndexChat()).commit();
        } else if (id == R.id.nav_concernchat){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ChatLanding()).commit();
        } else if (id == R.id.nav_invite) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new InviteFragment()).commit();
        }

        if(item.getGroupId() == R.id.circles){
            String circleId = mCirclename_ID.get(item.getTitle());
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("lastCircleOpen").setValue(circleId);

            recreate();
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


    public void getCirclesFromFirebase() {


        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("circles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren()
                                .iterator();

                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            mCircleIDs.add(dataSnapshotChild.getKey());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        FirebaseDatabase.getInstance()
                .getReference()
                .child("circles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for(String circleId:mCircleIDs){
                            mCircleNames.add(dataSnapshot.child(circleId).child("name").getValue(String.class));
                            mCirclename_ID.put(dataSnapshot.child(circleId).child("name").getValue(String.class),circleId);
                        }


                        for (int i = 0; i < mCircleNames.size(); i++) {
                            addNewCircle(mCircleNames.get(i));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });


    }

    public void getAllUsersFromFirebase() {

       final ArrayList<String> userIds = new ArrayList<>();

                FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastCircleOpen")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        currentCircle = dataSnapshot.getValue().toString();

                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("circleMembers")
                                .child(currentCircle)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren()
                                                .iterator();

                                        Log.d("current Circle ccccccc", "onDataChange: "+currentCircle);

                                        while (dataSnapshots.hasNext()) {
                                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                                            userIds.add(dataSnapshotChild.getKey());
                                            Log.d("userID in that circleeeeeeeeeee", "onDataChange: "+dataSnapshotChild.getKey());
                                        }

                                        FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child("users")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren()
                                                                .iterator();

                                                        final List<User>  users = new ArrayList<>();

                                                        while (dataSnapshots.hasNext()) {
                                                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                                                            User user = dataSnapshotChild.getValue(User.class);

                                                            if (userIds.contains(dataSnapshotChild.getKey())) {
                                                                users.add(user);
                                                            }
                                                        }

                                                        for (int i = 0; i < users.size(); i++) {
                                                            Log.d("*** USER NAME: ", users.get(i).getPreferredName());
                                                            Log.d("*** USER ID: ", userIds.get(i));
                                                            addNewUser(users.get(i), userIds.get(i));
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }

                                                });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Unable to retrieve the users.
                                    }

                                });

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }


    public boolean addNewUser(User user, String userId){
        Menu menu = navigationView.getMenu().getItem(6).getSubMenu();
        MenuItem menuItem;
        menuItem = menu.add(R.id.users,mMenuItems.size(),mMenuItems.size(),user.getPreferredName());
        menuItem.setCheckable(true);
        mMenuItems.add(menuItem);
        mUsers.add(userId);
        return true;
    }

    public boolean addNewCircle(String circleName){
        Menu menu = navigationView.getMenu().getItem(7).getSubMenu();
        MenuItem menuItem;
        menuItem = menu.add(R.id.circles,mCircleMenuItems.size(),mCircleMenuItems.size(),circleName);
        menuItem.setCheckable(true);
        mCircleMenuItems.add(menuItem);
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
        MainActivity.setAppRunningFalse();
    }
    //-------notification stuff ends--------------------------------------

    public static void setAppRunningFalse(){
        MainActivity.isAppRunning = false;
    }



    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    BaseActivity.hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }



}
