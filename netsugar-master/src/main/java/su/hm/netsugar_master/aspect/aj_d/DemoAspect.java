package su.hm.netsugar_master.aspect.aj_d;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

// @Aspect   //必须使用@AspectJ标注，这样class DemoAspect就等同于 aspect DemoAspect了
@Aspect
public class DemoAspect {

    static final String TAG = "DemoAspect";

    /*
    @Pointcut：pointcut也变成了一个注解，这个注解是针对一个函数的，比如此处的logForActivity()
    其实它代表了这个pointcut的名字。如果是带参数的pointcut，则把参数类型和名字放到
    代表pointcut名字的logForActivity中，然后在@Pointcut注解中使用参数名。
    基本和以前一样，只是写起来比较奇特一点。后面我们会介绍带参数的例子
    */
    @Pointcut("execution(* su.hm.netsugar.MainActivity.onCreate(..))")
    public void logForActivity() {
    }

    /*
    @Before：这就是Before的advice，对于after，after -returning，和after-throwing。对于的注解格式为
    @After，@AfterReturning，@AfterThrowing。Before后面跟的是pointcut名字，然后其代码块由一个函数来实现。比如此处的log。
    */
    @Around("logForActivity()")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        //对于使用Annotation的AspectJ而言，JoinPoint就不能直接在代码里得到多了，而需要通过
        //参数传递进来。
        joinPoint.proceed();
        Log.e(TAG, joinPoint.toShortString());
    }
}
