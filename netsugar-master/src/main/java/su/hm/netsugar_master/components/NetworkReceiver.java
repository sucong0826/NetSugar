package su.hm.netsugar_master.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import su.hm.netsugar_master.entity.NetSugarManager;

/**
 * A receiver is used for getting network changing state.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("NetworkReceiver", "I am receiving now..." + context.toString());

        // notify
        NetSugarManager.getInstance().updateObservers();
    }
}
