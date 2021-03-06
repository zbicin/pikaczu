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

public class PeriodAdapter extends ArrayAdapter<Integer> {
    private MainActivity mainActivity;
    private Boolean enabled;
    public PeriodAdapter(Context context, ArrayList<Integer> periods, MainActivity mainActivity) {
        super(context, 0, periods);
        this.mainActivity = mainActivity;
        this.enabled = true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Integer period = getItem(position);
        int periodIndex = this.mainActivity.availablePeriods.indexOf(period);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.period_item, parent, false);
        }

        Button removeButton = (Button) convertView.findViewById(R.id.buttonRemovePeriod);
        removeButton.setTag(position);
        removeButton.setEnabled(this.enabled);
        removeButton.setOnClickListener(this.mainActivity);
        Button editButton = (Button) convertView.findViewById(R.id.buttonEditPeriod);
        editButton.setTag(position);
        editButton.setEnabled(this.enabled);
        editButton.setOnClickListener(this.mainActivity);

        TextView label = (TextView) convertView.findViewById(R.id.labelPeriod);
        label.setEnabled(this.enabled);
        label.setText(this.mainActivity.periodLabels.get(periodIndex));

        return convertView;
    }

    public void setEnabled(Boolean value) {
        this.enabled = value;
        this.notifyDataSetChanged();
    }
}
