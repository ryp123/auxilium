package ca.usask.auxilium.Services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import org.joda.time.DateTime;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.MutableData;



import ca.usask.auxilium.Invitations;

/**
 * Created by rpiper on 3/3/18.
 */

public class InvitationService {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    public void createNewInvitations(Invitations invite) {
        root.child("invitations").push().setValue(invite);
    }

    public void processInvitationEmail(String dynamicLink) {
      Uri deepLink = Uri.parse(dynamicLink);
      final String invitationId = deepLink.getQueryParameter("invitationId");

    }



}
