package su.hm.netsugar_master.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Global is an annotation to monitor the state of network.
 * When network state changes, a method will be invoked which you can write code in it about some operations like saving data.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Global {
}
