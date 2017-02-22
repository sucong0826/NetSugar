package su.hm.netsugar_master;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import su.hm.netsugar_master.utils.NetworkUtils;

/**
 * NetworkSugar provides hooking function.
 * Uses it to inject context and provides some utilities for other classes.
 * <p>
 * Created by hm-su on 2017/2/12.
 */
public final class NetworkSugar {

    private static WeakReference<Context> contextWeakReference;

    private NetworkSugar() {
        throw new AssertionError("no instance");
    }

    public static void inject(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }


    /**
     * Whether the network is connecting now.
     *
     * @return true connected otherwise false
     */
    public static boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(contextWeakReference.get());
    }

    /**
     * Whether the network is mobile network.
     *
     * @return true mobile otherwise false
     */
    public static boolean isMobileNow() {
        return null != contextWeakReference.get() && NetworkUtils.isMobile(contextWeakReference.get());
    }

    /**
     * Whether the network is wifi network.
     *
     * @return true wifi otherwise false
     */
    public static boolean isWifiNow() {
        return null != contextWeakReference.get() && NetworkUtils.isWifi(contextWeakReference.get());
    }

    /**
     * Create a default toast.
     *
     * @param toast text to show
     */
    @SuppressWarnings("unused")
    public static void showToast(String toast) {
        Toast.makeText(contextWeakReference.get(), toast, Toast.LENGTH_SHORT).show();
    }

    // no more useful
    @SuppressWarnings("unused")
    public static void printLog(String log) {
        Log.i("NetSugar", log);
    }
}