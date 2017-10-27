package in.zbic.pikaczu;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by krzysiek on 26.10.17.
 */

public class PeriodList {

    private PeriodAdapter adapter;

    PeriodList(MainActivity activity) {
        ListView listView = (ListView)activity.findViewById(R.id.listPeriods);
        ArrayList<Integer> items = new ArrayList<Integer>();
        items.add(1);
        items.add(2);
        this.adapter = new PeriodAdapter(activity, items);
        listView.setAdapter(adapter);
    }

    public void add(int number) {
        this.adapter.add(number);
    }
}
