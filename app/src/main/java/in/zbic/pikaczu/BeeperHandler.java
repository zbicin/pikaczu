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
    private BeeperService beeperService;

    public BeeperHandler(BeeperService beeperService, Toast toast, Ringtone ringtone) {
        super(beeperService.getLooper());
        this.beeperService = beeperService;
        this.ringtone = ringtone;
        this.toast = toast;

        this.scheduleTick();
    }

    @Override
    public void handleMessage(Message message) {
        if(message.arg1 == Constants.MESSAGE_DING) {
            this.toast.show();
            this.ringtone.play();
        } else if(message.arg1 == Constants.MESSAGE_TICK) {
            this.beeperService.updateNotification();
            this.scheduleTick();
        }

    }

    public void scheduleDing(long time) {
        Message message = this.obtainMessage();
        message.arg1 = Constants.MESSAGE_DING;
        this.sendMessageDelayed(message, time);
    }

    private void scheduleTick() {
        Message message = this.obtainMessage();
        message.arg1 = Constants.MESSAGE_TICK;
        this.sendMessageDelayed(message, 1000);
    }
}
