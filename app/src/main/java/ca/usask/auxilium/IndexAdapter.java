package ca.usask.auxilium;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        IndexListItem item = itemsArrayList.get(position);

        TextView msgView = (TextView) itemView.findViewById(R.id.msg);
        TextView msgCount = (TextView) itemView.findViewById(R.id.msg_count);

        msgView.setText(item.getMsg());
        msgCount.setText(String.valueOf(item.getCount()));

        return itemView;
    }
}
