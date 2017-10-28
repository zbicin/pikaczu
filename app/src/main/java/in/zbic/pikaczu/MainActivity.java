package in.zbic.pikaczu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private ArrayList<Integer> chosenPeriods = null;
    private int editedPeriodIndex = Constants.UNKNOWN_INDEX;
    private Button buttonAdd;
    private Button buttonStart;
    private Button buttonStop;
    private ListView listPeriods;
    private PeriodAdapter listPeriodsAdapter;
    private AlertDialog dialogTimeSelector;

    public ArrayList<Integer> availablePeriods = new ArrayList<Integer>();
    public ArrayList<String> periodLabels = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.chosenPeriods == null) {
            this.chosenPeriods  = new ArrayList<Integer>();
        }

        Collections.addAll(this.availablePeriods, 1, 2, 3, 5, 10);

        setContentView(R.layout.activity_main);

        this.dialogTimeSelector = this.createDialogTimeSelector();
        this.dialogTimeSelector.setOnCancelListener(this);
        this.buttonAdd = (Button)findViewById(R.id.buttonAdd);
        this.buttonAdd.setOnClickListener(this);
        this.buttonStart = (Button)findViewById(R.id.buttonStart);
        this.buttonStart.setOnClickListener(this);
        this.buttonStop = (Button)findViewById(R.id.buttonStop);
        this.buttonStop.setOnClickListener(this);
        this.listPeriods = (ListView)findViewById(R.id.listPeriods);
        this.listPeriodsAdapter = new PeriodAdapter(this, this.chosenPeriods, this);
        this.listPeriods.setAdapter(this.listPeriodsAdapter);

        if(isServiceRunning(BeeperService.class)) {
            this.buttonStart.setVisibility(View.GONE);
            this.buttonStop.setVisibility(View.VISIBLE);
        }
    }

    public void startBeeper() {
        this.buttonStart.setVisibility(View.GONE);
        this.buttonStop.setVisibility(View.VISIBLE);

        Intent startIntent = new Intent(MainActivity.this, BeeperService.class);
        startIntent.setAction(Constants.START_SERVICE_ACTION);
        startService(startIntent);
    }

    public void stopBeeper() {
        this.buttonStart.setVisibility(View.VISIBLE);
        this.buttonStop.setVisibility(View.GONE);

        Intent stopIntent = new Intent(MainActivity.this, BeeperService.class);
        stopIntent.setAction(Constants.STOP_SERVICE_ACTION);
        startService(stopIntent);
    }

    @Override
    public void onClick(View view) {
        int position;
        switch(view.getId()) {
            case R.id.buttonStart:
                this.startBeeper();
                break;
            case R.id.buttonStop:
                this.stopBeeper();
                break;
            case R.id.buttonAdd:
                this.dialogTimeSelector.show();
                break;
            case R.id.buttonRemovePeriod:
                position = (int)view.getTag();
                this.removePeriod(position);
                break;
            case R.id.buttonEditPeriod:
                position = (int)view.getTag();
                this.editedPeriodIndex = position;
                this.dialogTimeSelector.show();
                break;
        }
    }

    private void removePeriod(int position) {
        this.chosenPeriods.remove(position);
        if(this.chosenPeriods.size() == 0) {
            this.buttonStart.setEnabled(true);
        }
        this.listPeriodsAdapter.notifyDataSetChanged();
    }

    private AlertDialog createDialogTimeSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        for (int i = 0; i<this.availablePeriods.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.availablePeriods.get(i));
            sb.append(" sekund");
            this.periodLabels.add(sb.toString());
        }

        builder.setItems(this.periodLabels.toArray(new CharSequence[this.periodLabels.size()]), this);
        builder.setTitle("Poproszę o czas");
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Integer item = this.availablePeriods.get(i);

        if (this.editedPeriodIndex == Constants.UNKNOWN_INDEX) {
            this.chosenPeriods.add(item);
            this.buttonStart.setEnabled(true);
        } else {
            this.chosenPeriods.set(this.editedPeriodIndex, item);
        }

        this.editedPeriodIndex = Constants.UNKNOWN_INDEX;
        this.listPeriodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        this.editedPeriodIndex = Constants.UNKNOWN_INDEX;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
