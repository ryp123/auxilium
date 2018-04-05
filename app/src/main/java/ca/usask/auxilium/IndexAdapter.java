package ca.usask.auxilium;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by jadenball on 2018-03-28.
 */

public class IndexAdapter extends ArrayAdapter<IndexListItem> {

    private final Context context;
    private final ArrayList<IndexListItem> itemsArrayList;

    public IndexAdapter(@NonNull Context context, @NonNull ArrayList<IndexListItem> objects) {
        super(context, R.layout.index_list_item, objects);
        this.context = context;
        this.itemsArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = inflater.inflate(R.layout.index_list_item, parent, false);
        final IndexListItem item = itemsArrayList.get(position);

        final Button btnPosResponse = (Button) itemView.findViewById(R.id.btn_pos_response);
        final Button btnNegResponse = (Button) itemView.findViewById(R.id.btn_neg_response);
        final TextView msgView = (TextView) itemView.findViewById(R.id.msg);
        TextView msgCount = (TextView) itemView.findViewById(R.id.msg_count);
        final TextView msgUsers = (TextView) itemView.findViewById(R.id.msg_users);

        msgView.setText(item.getMsg());
        msgCount.setText(String.valueOf(item.getCount()));
        if(item.expanded) {
            btnPosResponse.setVisibility(View.GONE);
            btnNegResponse.setVisibility(View.GONE);
            msgUsers.setText(item.getUsers());
            if(!item.respondedTo) {
                final DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
                root.child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("lastCircleOpen")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String curCircle = dataSnapshot.getValue(String.class);
                                String currentCircle = curCircle;
                                root.child("circleMembers")
                                        .child(currentCircle)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("role")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String role = dataSnapshot.getValue(String.class);
                                                                                if (role.equals("Index")) {
                                                                                    if(msgView.getText().toString().equals(IndexChat.con_inq1) ||
                                                                                            msgView.getText().toString().equals(IndexChat.con_inq2) ||
                                                                                            msgView.getText().toString().equals(IndexChat.con_inq3)){
                                                                                        btnPosResponse.setVisibility(View.VISIBLE);
                                                                                        btnNegResponse.setVisibility(View.VISIBLE);
                                                                                        btnPosResponse.setText("I am doing fine, thanks.");
                                                                                        btnNegResponse.setText("Not doing well right now.");
                                                                                    }
                                                                                } else { // concerned
                                                                                    btnPosResponse.setVisibility(View.GONE);
                                                                                    btnNegResponse.setVisibility(View.GONE);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Firebase Error", "user doesn't have a current circle");
                            }
                        });




                btnPosResponse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item.setMsg("\n" + msgView.getText().toString() + "\n" + btnPosResponse.getText().toString());
                        msgView.setText(item.getMsg());
                        item.respondedTo = true;
                        btnPosResponse.setVisibility(View.GONE);
                        btnNegResponse.setVisibility(View.GONE);
                    }
                });
                btnNegResponse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item.setMsg("\n" + msgView.getText().toString() + "\n" + btnNegResponse.getText().toString());
                        msgView.setText(item.getMsg());
                        item.respondedTo = true;
                        btnPosResponse.setVisibility(View.GONE);
                        btnNegResponse.setVisibility(View.GONE);
                    }
                });
            } else {
                btnPosResponse.setVisibility(View.GONE);
                btnNegResponse.setVisibility(View.GONE);
            }
        } else {
            btnPosResponse.setVisibility(View.GONE);
            btnNegResponse.setVisibility(View.GONE);
            msgUsers.setText("");
        }

        return itemView;
    }
}
