package ca.usask.auxilium;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

        View itemView = inflater.inflate(R.layout.index_list_item, parent, false);
        final IndexListItem item = itemsArrayList.get(position);

        final Button btnPosResponse = (Button) itemView.findViewById(R.id.btn_pos_response);
        final Button btnNegResponse = (Button) itemView.findViewById(R.id.btn_neg_response);
        final TextView msgView = (TextView) itemView.findViewById(R.id.msg);
        TextView msgCount = (TextView) itemView.findViewById(R.id.msg_count);
        TextView msgUsers = (TextView) itemView.findViewById(R.id.msg_users);

        msgView.setText(item.getMsg());
        msgCount.setText(String.valueOf(item.getCount()));
        if(item.expanded) {
            msgUsers.setText(item.getUsers());
            if(!item.respondedTo) {
                btnPosResponse.setVisibility(View.VISIBLE);
                btnNegResponse.setVisibility(View.VISIBLE);
                btnPosResponse.setText("I am doing fine, thanks.");
                btnNegResponse.setText("Not doing well right now.");

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
