package in.zbic.pikaczu;

import android.view.View;
import android.widget.Button;

/**
 * Created by krzysiek on 27.10.17.
 */

public class StopButton implements View.OnClickListener {
    private Button button;
    private MainActivity activity;
    StopButton(MainActivity activity) {
        this.activity = activity;
        this.button = (Button)activity.findViewById(R.id.buttonStop);
        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.activity.startPikacz();
        //this.activity.startPikaczService();
    }

    public void show() {
        this.button.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.button.setVisibility(View.GONE);
    }
}
