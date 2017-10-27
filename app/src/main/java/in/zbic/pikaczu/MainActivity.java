package in.zbic.pikaczu;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public TimePeriodSelector timeSelector;
    public AddButton addButton;
    public PeriodList periodList;
    public StartButton startButton;
    public StopButton stopButton;
    public Pikacz pikacz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addButton = new AddButton(this);
        this.timeSelector = new TimePeriodSelector(this);
        this.periodList = new PeriodList(this);
        this.startButton = new StartButton(this);
        this.stopButton = new StopButton(this);

        Toast toast = Toast.makeText(this.getApplicationContext(), "Ding!", Toast.LENGTH_SHORT);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        this.pikacz = new Pikacz(this, ringtone, toast);
    }

    public void addPeriod(int time) {
        this.periodList.add(time);
    }

    public void startPikacz() {
        this.pikacz.start(this.periodList.getItems());
        this.startButton.hide();
        this.stopButton.show();
    }

    public void stopPikacz() {
        this.pikacz.stop();
        this.startButton.show();
        this.stopButton.hide();
    }
}
