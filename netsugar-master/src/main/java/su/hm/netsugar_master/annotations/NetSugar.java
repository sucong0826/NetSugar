package su.hm.netsugar_master.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import su.hm.netsugar_master.entity.NetworkType;

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
     * -m(v1.0)
     * It depends on whether the method is to check network state.
     * check() is removed.
     * Why?
     * Because I think if a method is annotated with NetSugar, it must need to check network state.
     * If you not, please don't add a @NetSugar to a method.
     */
    // boolean check() default false;

    /**
     * -m(v1.0)
     * online situation
     * deprecated.
     */
    // Online online();

    /**
     * -m(v1.0)
     * offline situation
     * Offline annotation is a single annotation now.
     * It should annotate methods to tell these methods are going to handle offline situation.
     */
    // Offline offline();

    /**
     * -m(v1.0)
     * hey friend type() see you again.
     * You were in @Online.
     * But Online is removed, so type() is back.
     * type indicates which type of network state is allowed.
     * -m(v1.0)
     *
     * @return network type
     */
    NetworkType type() default NetworkType.ALL;

    /**
     * -m(v1.0)
     * {@see Offline pair}
     * -1 means that there is no need to handle offline.
     * -m(v1.0)
     *
     * @return pair key
     */
    int pair() default -1;
}
