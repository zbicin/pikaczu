package in.zbic.pikaczu;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> pendingIntents = new ArrayList<PendingIntent>();


    public ArrayList<Integer> availablePeriods = new ArrayList<Integer>();
    public ArrayList<String> periodLabels = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.chosenPeriods == null) {
            this.chosenPeriods  = new ArrayList<Integer>();
        }

        this.alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        this.sharedPreferences = this.getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
        restoreChosenPeriods();

        Collections.addAll(this.availablePeriods, Constants.AVAILABLE_PERIODS);

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

        if(isServiceRunning(NotificationService.class)) {
            this.buttonStart.setVisibility(View.GONE);
            this.buttonStop.setVisibility(View.VISIBLE);
            this.listPeriodsAdapter.setEnabled(false);
        }
        this.buttonStart.setEnabled(this.chosenPeriods.size() > 0);
    }

    public void startBeeper() {
        this.buttonStart.setVisibility(View.GONE);
        this.buttonStop.setVisibility(View.VISIBLE);
        this.listPeriodsAdapter.setEnabled(false);

        Intent startIntent = new Intent(MainActivity.this, NotificationService.class);
        startIntent.setAction(Constants.START_SERVICE_ACTION);
        startIntent.putExtra(Constants.CHOSEN_PERIODS, this.chosenPeriods);
        startService(startIntent);

        this.pendingIntents.clear();

        Integer periodsSum = 0;
        for(Integer period : this.chosenPeriods) {
            periodsSum += period;
            Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, periodsSum, myIntent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (periodsSum*1000), pendingIntent);
            this.pendingIntents.add(pendingIntent);
        }
    }

    public void stopBeeper() {
        this.buttonStart.setVisibility(View.VISIBLE);
        this.buttonStop.setVisibility(View.GONE);
        this.listPeriodsAdapter.setEnabled(true);

        Intent stopIntent = new Intent(MainActivity.this, NotificationService.class);
        stopIntent.setAction(Constants.STOP_SERVICE_ACTION);
        startService(stopIntent);

        for(PendingIntent pendingIntent : this.pendingIntents) {
            this.alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.storeChosenPeriods();
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
            this.buttonStart.setEnabled(false);
        }
        this.listPeriodsAdapter.notifyDataSetChanged();
    }

    private AlertDialog createDialogTimeSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        for (int i = 0; i<this.availablePeriods.size(); i++) {
            StringBuilder sb = new StringBuilder();
            Integer period = this.availablePeriods.get(i);
            boolean isMinutes = period >= 60;
            Integer normalizedPeriod = isMinutes ? (period / 60) : period;
            String unit = isMinutes ? " minut" : " sekund";
            sb.append(normalizedPeriod);
            sb.append(unit);
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
            this.listPeriodsAdapter.setEnabled(true);
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

    @Override
    public void onResume() {
        super.onResume();

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

    private void storeChosenPeriods() {
        String stringifiedChosenPeriods = TextUtils.join(Constants.SEPARATOR, this.chosenPeriods);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.STRINGIFIED_CHOSEN_PERIODS_KEY, stringifiedChosenPeriods);
        editor.commit();
    }

    private void restoreChosenPeriods() {
        String stringifiedChosenPeriods = this.sharedPreferences.getString(Constants.STRINGIFIED_CHOSEN_PERIODS_KEY, "");
        if(stringifiedChosenPeriods.length() > 0) {
            String[] items = stringifiedChosenPeriods.split(Constants.SEPARATOR);
            for (String item : items) {
                Integer period = Integer.parseInt(item);
                this.chosenPeriods.add(period);
            }
        }
    }
}
