package su.hm.netsugar_master.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Offline @interface is added to {@link NetSugar}.
 * <p>
 * Created by hm-su on 2017/2/18.
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Offline {
    /**
     * method tells there should exist a method to handle the situation of offline.
     *
     * @return method name which one to handle offline situation.
     */
    String method() default "";
}
