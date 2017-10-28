package in.zbic.pikaczu;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private final int UNKNOWN_INDEX = -1;

    private ArrayList<Integer> chosenPeriods = new ArrayList<Integer>();
    private int editedPeriodIndex = this.UNKNOWN_INDEX;
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

        Toast toast = Toast.makeText(this.getApplicationContext(), "Ding!", Toast.LENGTH_SHORT);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        //this.pikacz = new Pikacz(this, ringtone, toast);
    }

    public void startBeeper() {
        this.buttonStart.setVisibility(View.GONE);
        this.buttonStop.setVisibility(View.VISIBLE);
    }

    public void stopBeeper() {
        this.buttonStart.setVisibility(View.GONE);
        this.buttonStop.setVisibility(View.VISIBLE);
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
                this.chosenPeriods.remove(position);
                this.listPeriodsAdapter.notifyDataSetChanged();
                break;
            case R.id.buttonEditPeriod:
                position = (int)view.getTag();
                this.editedPeriodIndex = position;
                this.dialogTimeSelector.show();
                break;
        }
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

        if (this.editedPeriodIndex == this.UNKNOWN_INDEX) {
            this.chosenPeriods.add(item);
        } else {
            this.chosenPeriods.set(this.editedPeriodIndex, item);
        }

        this.editedPeriodIndex = this.UNKNOWN_INDEX;
        this.listPeriodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        this.editedPeriodIndex = this.UNKNOWN_INDEX;
    }
}
