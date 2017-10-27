package in.zbic.pikaczu;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by krzysiek on 27.10.17.
 */

public class Pikacz {
    public HandlerThread handlerThread;
    public Handler handler;
    public Pikacz(MainActivity activity, Ringtone ringtone, Toast toast) {
        this.handlerThread = new HandlerThread("Pikacz");
        this.handlerThread.start();
        this.handler = new PikaczHandler(this.handlerThread.getLooper(), ringtone, toast);
    }

    public void start(Integer[] periods) {
        int delay = 0;
        for(int i = 0; i<periods.length; i++) {
            Message message = this.handler.obtainMessage();
            delay += periods[i];
            this.handler.sendMessageDelayed(message, delay * 1000);
        }

    }

    public void stop() {
        this.handlerThread.quit();
    }
}
