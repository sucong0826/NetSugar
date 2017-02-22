package su.hm.netsugar_master.listener;

import su.hm.netsugar_master.entity.NetworkType;

/**
 * When network state changes, this callback will receive the notification.
 * Therefore, each class annotated with {@link su.hm.netsugar_master.annotations.Global} is enforced to implement this
 * callback so as to do your own operations.
 * <strong>If you won't implement this call but you annotate a class with Global, it is useless.</strong>
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public interface GlobalCallback {
    /**
     * callback for network state changed.
     *
     * @param currentType current network type
     */
    void stateChanged(int currentType);
}
