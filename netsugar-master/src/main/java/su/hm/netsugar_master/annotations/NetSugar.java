package su.hm.netsugar_master.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NetSugar is designed to check network status.
 * Sometimes when we write codes about methods which are needed to check network status, codes like this:
 * <code>
 * public void playVideo() {
 * boolean hasNetwork = NetworkUtils.hasNetwork(context);
 * if (hasNetwork) {
 * int status = NetworkUtils.getStatus(context);
 * switch (status) {
 * ...
 * // do according to status.
 * }
 * } else {
 * // toast or dialog to show sth.
 * }
 * }
 * </code>
 * </p>
 * But the solution what I give is an annotation to handle this.
 * <code>
 * <code>@NetSugar(type = WIFI, offline = "playVideoWithoutNetwork")</code>
 * public void playVideo() {
 * <p>
 * }
 * </code>
 * <p>
 * The type is an ENUM which has MOBILE, WIFI, ALL.
 * The offline represents a method name, this method will cope with the situation without network.
 * AnnotationProcessor
 * Created by hm-su on 2017/2/12.
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetSugar {
    /**
     * It depends on whether the method is to check network state.
     */
    boolean check() default false;

    /**
     * online situation
     */
    Online online();

    /**
     * offline situation
     */
    Offline offline();
}
