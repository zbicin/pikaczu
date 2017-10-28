package in.zbic.pikaczu;

import android.content.Context;
import android.media.Ringtone;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by krzysiek on 27.10.17.
 */

public class BeeperHandler extends Handler {

    private Toast toast;
    private Ringtone ringtone;

    public BeeperHandler(Looper looper, Toast toast, Ringtone ringtone) {
        super(looper);
        this.ringtone = ringtone;
        this.toast = toast;
    }

    @Override
    public void handleMessage(Message message) {
        this.toast.show();
        this.ringtone.play();
    }
}
