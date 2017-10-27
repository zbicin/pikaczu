package in.zbic.pikaczu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by krzysiek on 26.10.17.
 */

public class PeriodAdapter extends ArrayAdapter<Integer> implements View.OnClickListener{
    public PeriodAdapter(Context context, ArrayList<Integer> periods) {
        super(context, 0, periods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Integer period = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.period_item, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.periodLabel);
        Button removeButton = (Button) convertView.findViewById(R.id.removePeriodButton);
        StringBuilder sb = new StringBuilder();
        sb.append(period);
        sb.append(" sekund");
        label.setText(sb.toString());
        removeButton.setTag(position);
        removeButton.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        int position = (int)view.getTag();
        Integer item = this.getItem(position);
        this.remove(item);
    }
}
