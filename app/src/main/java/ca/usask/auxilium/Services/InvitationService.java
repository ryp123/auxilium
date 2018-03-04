package ca.usask.auxilium.Services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.usask.auxilium.Invitations;

/**
 * Created by rpiper on 3/3/18.
 */

public class InvitationService {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    public void createNewInvitations(Invitations invite) {
        root.child("invitations").push().setValue(invite);
    }
}
