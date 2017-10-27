package in.zbic.pikaczu;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

/**
 * Created by krzysiek on 26.10.17.
 */

public class AddButton implements View.OnClickListener {
    private Button button;
    private MainActivity activity;
    AddButton(MainActivity activity) {
        this.activity = activity;
        this.button = (Button)activity.findViewById(R.id.buttonAdd);
        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.activity.timeSelector.show();
    }
}
