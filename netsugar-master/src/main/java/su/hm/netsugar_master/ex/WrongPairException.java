package su.hm.netsugar_master.ex;

/**
 * Pair value can't not be -1 or smaller than 0;
 * In the case WrongPairException will be thrown.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class WrongPairException extends IllegalArgumentException {

    public WrongPairException(String message) {
        super(message);

        // I will print logs as soon as possible.
    }
}
