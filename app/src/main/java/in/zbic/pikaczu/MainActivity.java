package in.zbic.pikaczu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public TimePeriodSelector timeSelector;
    public AddButton addButton;
    public PeriodList periodList;
    public StartButton startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addButton = new AddButton(this);
        this.timeSelector = new TimePeriodSelector(this);
        this.periodList = new PeriodList(this);
        this.startButton = new StartButton(this);
    }

    public void addPeriod(int time) {
        this.periodList.add(time);
    }
}
