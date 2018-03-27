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
      this.root.runTransaction(new Transaction.Handler() {

        @Override
        public Transaction.Result doTransaction(MutableData dbRef) {
          if (invitationExists(dbRef, invitationId)) {
            Log.d("invitations", "Mapping the invitation to a object.");
            Invitations invite = getInvitation(dbRef, invitationId);
            String circleId = invite.getCircleId();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserId = currentUser.getUid();
            Log.d("invitations",
                  "Got user info\n uid: " + currentUserId + "\nemail: " +
                  currentUser.getEmail());
            if (isInvitationValid(invite, currentUser.getEmail())) {
              Log.d("invitations",
                    "Adding user to the index's circle with\n circleId: " +
                    circleId + "\nuserId: " + currentUserId);
              updateUserWithCircleId(dbRef, circleId, currentUserId);
              updateCircleMembers(dbRef, circleId, currentUserId);
              updateLastViewedCircle(dbRef, currentUserId, circleId);
              Log.d("invitations", "Adding user to the index's concerned party chat.");
              String concernedChatId = getConcernedChatId(dbRef, circleId);
              if (concernedChatId == null) {
                Log.e("invitations", "This shouldn't happen because if a circle" +
                                      "is made then a corresponding concerned" +
                                      "chat should have been made as well.");
                return Transaction.abort();
              } else {
                  Log.d("invitations",
                        "Adding user to the index's concerned party chat with\n" +
                        "concernedPartyId: " + concernedChatId + "\nuserId: " + currentUserId);
                  updateUserWithConcernedChatId(dbRef, concernedChatId, currentUserId);
                  updateConcernedPartyMembers(dbRef, concernedChatId, currentUserId);
                  Log.d("invitations", "Deleting invitation with id: " + invitationId);
                  deleteUsedInvitation(dbRef, invitationId);
                  return Transaction.success(dbRef);
              }
            } else {
                return Transaction.abort();
            }

          } else {
            Log.d("invitations", "The invitation wasn't processed because it" +
                                  "was used or is after the expiration date");
            return Transaction.abort();
          }
        }

        @Override
        public void onComplete(DatabaseError dbError, boolean b, DataSnapshot snapShot) {
          Log.d("invitations", dbError.getDetails());
        }
      });
    }

    private void updateUserWithCircleId(MutableData dbRef,
                                        String circleId,
                                        String currentUserId) {
      HashMap<String, HashMap<String, String>> userCircleListing = new HashMap();
      HashMap<String, String> circleRole = new HashMap();
      circleRole.put("role", "Concerned");
      userCircleListing.put(circleId, circleRole);
      dbRef.child("users")
           .child(currentUserId)
           .child("circles")
           .setValue(userCircleListing);
    }

    private void updateUserWithConcernedChatId(MutableData dbRef,
                                               String concernedPartyId,
                                               String currentUserId) {
        HashMap<String, Boolean> concernedChat = new HashMap();
        concernedChat.put(concernedPartyId, true);
        dbRef.child("users")
             .child(currentUserId)
             .child("concernedChat")
             .setValue(concernedChat);
    }

    private void updateCircleMembers(MutableData dbRef,
                                     String circleId,
                                     String currentUserId) {
      HashMap<String, HashMap<String, String>> circleMember = new HashMap();
      HashMap<String, String> circleMemberRole = new HashMap();
      circleMemberRole.put("role", "Concerned");
      circleMemberRole.put("status", "Active");
      circleMember.put(currentUserId, circleMemberRole);
      dbRef.child("circleMembers")
           .child(circleId)
           .setValue(circleMember);
    }

    private void updateConcernedPartyMembers(MutableData dbRef,
                                            String concernedChatId,
                                            String currentUserId) {
      HashMap<String, Boolean> concernedPartyMember = new HashMap();
      concernedPartyMember.put(currentUserId, true);
      dbRef.child("concernedPartyMember")
           .child(concernedChatId)
           .setValue(concernedPartyMember);
    }

    private String getConcernedChatId(MutableData dbRef,
                                      String circleId) {
        for (MutableData children: dbRef.child("concernedCircle").getChildren()) {
           // if the concerned chat is related to the given circle id we want that cp chat id.
          if (children.child("relatedTo").getValue() == circleId) {
            // get the concerned chat id.
            return children.getKey();
          }
        }
        return null;
    }

    private void deleteUsedInvitation(MutableData dbRef, String invitationId) {
      dbRef.child("invitations").child(invitationId).setValue(null);
    }

    private Invitations getInvitation(MutableData dbRef, String invitationId) {
        return dbRef.child("invitations")
                    .child(invitationId)
                    .getValue(Invitations.class);
    }

    private boolean invitationExists(MutableData dbRef, String invitationId) {
      for (MutableData dbChild: dbRef.child("invitations").getChildren()) {
          if (dbChild.getKey() == invitationId) {
              return true;
          }
      }
      return false;
    }

    private void updateLastViewedCircle(MutableData dbRef, String userId, String circleID) {
        MutableData dbChild = dbRef.child("users")
                                   .child(userId)
                                   .child("lastViewedCircle");

        if (dbChild.getValue() == null) {
            dbChild.setValue(circleID);
        }
    }

    private boolean isInvitationValid(Invitations invite,
                                      String currentUsersEmail) {
      DateTime expirationDate = new DateTime(invite.getExpirationDate());
      if (!invite.getSenderEmail().equals(currentUsersEmail)) {
        return false;
      } else if (!expirationDate.isAfterNow()) {
        return false;
      } else {
        return true;
      }
    }
}
