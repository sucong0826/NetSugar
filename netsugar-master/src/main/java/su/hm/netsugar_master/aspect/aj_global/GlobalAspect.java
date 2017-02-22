package su.hm.netsugar_master.aspect.aj_global;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Type;

import su.hm.netsugar_master.components.NetworkService;

/**
 * Aspect of global is aimed to cut cross Application method 'onCreate()'
 * Created by hm-su on 2017/2/21.
 */

@Aspect
public class GlobalAspect {

    private static final String ON_CREATE = "execution(* su.hm.netsugar.*.onCreate())";

    @Pointcut(value = ON_CREATE)
    public void onCreateCut() {
        // do nothing
    }

    @Around(value = "onCreateCut()")
    public void handleApplicationOnCreate(ProceedingJoinPoint joinPoint) throws Throwable {

        // test
        Log.i("ApplicationCut", "running to here");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> applicationCls = signature.getMethod().getDeclaringClass();

        /* is application class is Application child or a child class which is extends Application */
        Type type = applicationCls.getGenericSuperclass();
        boolean isAssignable = ((Class<?>) type).isAssignableFrom(Application.class);
        if (isAssignable) {
            // create a service
            Intent service = new Intent((Context) joinPoint.getTarget(), NetworkService.class);
            ((Context) joinPoint.getTarget()).startService(service);
        }

        // proceed
        joinPoint.proceed();
    }
}
