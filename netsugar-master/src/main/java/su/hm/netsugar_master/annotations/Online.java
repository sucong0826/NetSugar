package su.hm.netsugar_master.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import su.hm.netsugar_master.entity.NetworkType;

/**
 * Online @interface is used for Element type of Annotation.
 * It is one of {@link NetSugar} consisting annotation.
 * It indicates that that actions which need network and network status.
 * <p>
 * <strong>
 *     NOTE: please DO NOT using this annotation in NetSugar, it is deprecated in V1.0, type is moved to @NetSugar
 *     and @Online says goodbye to NetSugar. For simplifying NetSugar annotation, it is deprecated now.
 *     And it is not be handled by framework any longer.
 * </strong>
 * Created by hm-su on 2017/2/18.
 */

@Deprecated
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Online {
    NetworkType type() default NetworkType.ALL;
}
