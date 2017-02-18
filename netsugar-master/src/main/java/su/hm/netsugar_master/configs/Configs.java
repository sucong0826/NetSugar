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
     * Method not found;
     */
    public static final String EMPTY_METHOD_NAME = "";

    /**
     * Tha name of annotation of {@link NetSugar}
     */
    public static final String NET_SUGAR_ANNO = "su.hm.netsugar_master.annotations.NetSugar";

    /**
     * Tha name of annotation of {@link Online}
     */
    static final String ONLINE_ANNO = "su.hm.netsugar_master.annotations.Online";

    /**
     * Tha name of annotation of {@link Offline}
     */
    static final String OFFLINE_ANNO = "su.hm.netsugar_master.annotations.Offline";
}
