package su.hm.netsugar_master.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network utils.
 * <p>
 * Created by hm-su on 2017/2/18.
 */

public final class NetworkUtils {
    private NetworkUtils() {
        throw new AssertionError("No instance");
    }

    /**
     * Whether the network is connecting now.
     *
     * @return true connected otherwise false
     */
    public static boolean isNetworkConnected(Context context) {
        if (null == context) return false;

        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo.getType();
    }

    /**
     * Whether is mobile network.
     *
     * @param context context
     * @return true mobile network otherwise false
     */
    public static boolean isMobile(Context context) {
        return ConnectivityManager.TYPE_MOBILE == getNetworkType(context);
    }

    /**
     * Whether is wifi network.
     *
     * @param context context
     * @return true mobile network otherwise false
     */
    public static boolean isWifi(Context context) {
        return ConnectivityManager.TYPE_WIFI == getNetworkType(context);
    }
}
