package su.hm.netsugar_master.components;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import static su.hm.netsugar_master.configs.Configs.NETWORK_CHANGE_ACTION;

/**
 * Network Service is designed to keep monitoring network state from application started.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class NetworkService extends Service {

    // network receiver
    private NetworkReceiver receiver;

    // intent filter
    private IntentFilter filter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create a receiver
        receiver = new NetworkReceiver();
        filter = new IntentFilter(NETWORK_CHANGE_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // register
        registerReceiver(receiver, filter);

        // return super.
        return super.onStartCommand(intent, flags, startId);
    }
}
