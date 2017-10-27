package in.zbic.pikaczu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by krzysiek on 26.10.17.
 */

public class TimePeriodSelector implements DialogInterface.OnClickListener {
    static int[] periods = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
    static String[] periodsChars = new String[periods.length];

    private MainActivity activity;
    private AlertDialog alert;

    TimePeriodSelector(MainActivity activity) {
        this.activity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        for (int i = 0; i<periods.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(periods[i]);
            sb.append(" sekund");
            periodsChars[i] = sb.toString();
        }
        builder.setItems(periodsChars, this);
        builder.setTitle("Poproszę o czas");
        this.alert = builder.create();
    }

    public void show() {
        this.alert.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        this.activity.addPeriod(periods[i]);
    }
}
