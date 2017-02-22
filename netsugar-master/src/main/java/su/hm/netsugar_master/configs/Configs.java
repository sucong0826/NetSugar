package su.hm.netsugar_master.configs;

import su.hm.netsugar_master.annotations.NetSugar;
import su.hm.netsugar_master.annotations.Offline;
import su.hm.netsugar_master.annotations.Online;

/**
 * Just some constants here.
 * <p>
 * Created by hm-su on 2017/2/18.
 */
public final class Configs {

    public static final String EXECUTION = "execution";
    public static final String CALL = "call";
    public static final String STR_MOBILE = "MOBILE";
    public static final String STR_WIFI = "WIFI";

    /**
     * type of offline.
     */
    public static final int FLAG_OFF = 0x01;

    /**
     * if network type is not matching to you set.
     */
    public static final int FLAG_NOT_MATCH = 0x01 << 1;

    /**
     * No need to match.
     */
    public static final int FLAG_NO_NEED = 0x0;

    /**
     * default pair value.
     */
    public static final int DEF_PAIR_VAL = -1;

    /**
     * Method not found;
     */
    public static final String EMPTY_METHOD_NAME = "";

    /**
     * action for network state
     */
    public static final String NETWORK_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * Tha name of annotation of {@link NetSugar}
     */
    public static final String NET_SUGAR_ANNO = "@su.hm.netsugar_master.annotations.NetSugar";

    /**
     * -m(v1.0)
     * Tha name of annotation of {@link Online}
     * Online is deprecated.
     */
    // static final String ONLINE_ANNO = "su.hm.netsugar_master.annotations.Online";

    /**
     * Tha name of annotation of {@link Offline}
     */
    public static final String OFFLINE_ANNO = "@su.hm.netsugar_master.annotations.Offline";
}
