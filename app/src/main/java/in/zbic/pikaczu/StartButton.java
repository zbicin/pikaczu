package in.zbic.pikaczu;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

/**
 * Created by krzysiek on 26.10.17.
 */

class StartButton implements View.OnClickListener{
    private Button button;
    private MainActivity activity;
    StartButton(MainActivity activity) {
        this.activity = activity;
        this.button = (Button)activity.findViewById(R.id.buttonStart);
        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //this.activity.startPikaczService();
    }
}
