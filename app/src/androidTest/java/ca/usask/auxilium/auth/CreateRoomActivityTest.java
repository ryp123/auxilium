package ca.usask.auxilium.auth;

import android.view.View;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;


/**
 * Created by gongcheng on 2018-02-14.
 */

@RunWith(MockitoJUnitRunner.class)
public class CreateRoomActivityTest {

    @Mock
    private GoogleSignIn mockGoogleSignIn;

    @Mock
    private GoogleSignInAccount mockGoogleSignInAccount;

    @Mock
    private FirebaseDatabase mockFirebaseDatabase;

    @Mock
    private DatabaseReference mockDatabaseReference;

    @InjectMocks
    CreateRoomActivity mockRoomActivity;


    @Test
    public void testOnCreateButtonClick(){

    }




}