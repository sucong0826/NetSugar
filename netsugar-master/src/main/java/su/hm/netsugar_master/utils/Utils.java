package su.hm.netsugar_master.utils;

import java.util.List;

/**
 * Some utilities.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public final class Utils {

    private Utils() {
        throw new AssertionError("No instance");
    }

    /**
     * Check is empty list.
     *
     * @param list target list
     * @return true empty list, otherwise false
     */
    public static boolean isEmptyList(List<?> list) {
        return null == list || list.isEmpty();
    }

    /**
     * Check whether list has element T;
     *
     * @param list target list
     * @param t    target element
     * @param <T>  type t
     * @return true has false otherwise
     */
    public static <T> boolean hasElement(List<T> list, T t) {
        return !(null == list || list.isEmpty()) && list.contains(t);
    }
}
