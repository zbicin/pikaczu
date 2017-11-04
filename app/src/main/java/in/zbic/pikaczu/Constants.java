package in.zbic.pikaczu;

/**
 * Created by krzysiek on 28.10.17.
 */

public class Constants {
    public static final int UNKNOWN_INDEX = -1;
    public static final String MAIN_SERVICE_ACTION = "in.zbic.service.main";
    public static final String START_SERVICE_ACTION = "in.zbic.service.start";
    public static final String PLAY_SERVICE_ACTION = "in.zbic.service.play";
    public static final String PAUSE_SERVICE_ACTION = "in.zbic.service.pause";
    public static final String NEXT_SERVICE_ACTION = "in.zbic.service.next";
    public static final String PREVIOUS_SERVICE_ACTION = "in.zbic.service.previous";
    public static final String STOP_SERVICE_ACTION = "in.zbic.service.stop";
    public static final int SERVICE_ID = 101;
    public static final String STRINGIFIED_CHOSEN_PERIODS_KEY = "in.zbic.stringifiedChosenPeriods";
    public static final String SEPARATOR = ",";
    public static final String PREFERENCES_FILE = "pikaczu";
    public static final String CHOSEN_PERIODS = "in.zbic.chosenPeriods";
    public static final Integer[] AVAILABLE_PERIODS = {1, 2, 3, 5, 10, 30, 60, 120, 180, 300, 600, 900, 1200, 1500, 1800, 2400, 3000, 3600};
    public static final Integer MESSAGE_DING = 0;
    public static final Integer MESSAGE_TICK = 1;
}
