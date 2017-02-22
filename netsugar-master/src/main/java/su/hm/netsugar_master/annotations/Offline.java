package su.hm.netsugar_master.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Offline @interface is added to {@link NetSugar}.
 * -m(v1.0)
 * <strong>
 * <code>@Target</code> is changed.
 * Offline is going to annotate method not annotation.
 * </strong>
 * <p>
 * Created by hm-su on 2017/2/18.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Offline {
    /**
     * method tells there should exist a method to handle the situation of offline.
     * <p>
     * -m(v1.0): method is changed into pair().
     * it means that corresponds to pair the value in {@link NetSugar}.
     *
     * @return method name which one to handle offline situation.
     */
    int pair() default -1;
}
