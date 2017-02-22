package su.hm.netsugar_master.ex;

/**
 * If no method was found annotated with {@link su.hm.netsugar_master.annotations.Offline},
 * NoHookException will be thrown.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class NoHookException extends Exception {
    public NoHookException(String msg) {
        super(msg);
    }
}
