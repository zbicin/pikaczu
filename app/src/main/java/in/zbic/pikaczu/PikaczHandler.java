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

public class PikaczHandler extends Handler {
    private Toast toast;
    private Ringtone ringtone;

    public PikaczHandler(Looper looper, Ringtone ringtone, Toast toast) {
        super(looper);
        this.ringtone = ringtone;
        this.toast = toast;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            this.ringtone.play();
            this.toast.show();
        } catch(Exception e) {}
    }
}
