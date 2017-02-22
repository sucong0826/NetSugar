package su.hm.netsugar_master.entity;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import su.hm.netsugar_master.listener.GlobalCallback;
import su.hm.netsugar_master.listener.IUpdateListener;
import su.hm.netsugar_master.utils.NetworkUtils;
import su.hm.netsugar_master.utils.Utils;

/**
 * NetSugar central manager.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class NetSugarManager implements IUpdateListener {

    /**
     * observers of activity list.
     */
    private List<GlobalCallback> observers;

    private NetSugarManager() {
        observers = new LinkedList<>();
    }

    /**
     * Get the single instance of NetSugarManager.
     *
     * @return a NetSugarManager instance
     */
    public static NetSugarManager getInstance() {
        return SingletonHolder.instance;
    }


    private static class SingletonHolder {
        static NetSugarManager instance = new NetSugarManager();
    }

    /**
     * Attach an an observer into a list.
     *
     * @param observer target activity
     */
    public void attach(GlobalCallback observer) {
        if (null == observers)
            return;

        boolean hasElement = Utils.hasElement(observers, observer);
        if (!hasElement) {
            observers.add(observer);
        }
    }

    /**
     * Remove an observer from list.
     *
     * @param observer target activity
     */
    public void detach(GlobalCallback observer) {
        if (null == observers)
            return;

        boolean hasElement = Utils.hasElement(observers, observer);
        if (hasElement) {
            observers.remove(observer);
        }
    }

    public List<GlobalCallback> getObservers() {
        return observers;
    }

    @Override
    public void updateObservers() {
        if (!Utils.isEmptyList(getObservers())) {

            /* here we just update the last observer in the target list. */
            GlobalCallback lastObserver = getObservers().get(getObservers().size());
            int type = NetworkUtils.getNetworkType((Context) lastObserver);
            lastObserver.stateChanged(type);
        }
    }
}
