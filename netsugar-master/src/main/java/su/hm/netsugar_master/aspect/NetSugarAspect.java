package su.hm.netsugar_master.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import su.hm.netsugar_master.NetworkSugar;
import su.hm.netsugar_master.annotations.NetSugar;
import su.hm.netsugar_master.entity.NetSugarDescriptor;
import su.hm.netsugar_master.entity.NetworkType;

import static su.hm.netsugar_master.configs.Configs.EMPTY_METHOD_NAME;
import static su.hm.netsugar_master.configs.Configs.STR_MOBILE;

/**
 * NetSugarAspect is just the aspect that we are to cut cross.
 * Here we define pointcuts and advices, join points and aspect.
 * <p>
 * Created by hm-su on 2017/2/18.
 */

@Aspect
public class NetSugarAspect {

    // private Map<String, NetSugarDescriptor> descriptorMap = new HashMap<>();

    // "execution(@su.hm.netsugar_master.annotations.NetSugar * *(..))"
    // This means that around advice runs when this kind of method executing
    // which return types and name and parameters have no limitation.
    // it is a property-based join point.
    

    // private static final String POINTCUT = "execution(@su.hm.netsugar_master.annotations.NetSugar * *(..))";
    private static final String POINTCUT = EXECUTION + "(@" + NET_SUGAR_ANNO + " * *(..))";
	
    // here defines a point cut with annotation.
    // AspectJ 5 defines a pointcut: pointcut pc() : call(void Foo.m());
    // pc() is the name of pointcut just like the name of 'methodAnnotated'.
    // void Foo.m() is the join point.
    @Pointcut(value = POINTCUT)
    public void method() {
    }

    // used for test
//    @Around(value = "method()")
//    public void handle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        proceedingJoinPoint.proceed();
//        NetworkSugar.createToast("hello");
//    }

    @Around(value = "method()")
    public void handleNetwork(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        NetSugarDescriptor descriptor = getAnnotationParameter(proceedingJoinPoint);
        boolean isCheck = descriptor.isCheck();         // prevent inline ^.^
        if (!isCheck) {
            // if there is no need for a method to check network, we release and make it continue.
            proceedingJoinPoint.proceed();
        } else {
            // otherwise, to handle it;
            boolean isNetworkConnected = NetworkSugar.isNetworkConnected();

            /* if network is not connected, it is a complicated issue. */
            if (isNetworkConnected) {
                // the type is developers set
                NetworkType type = descriptor.getNetworkType();
//                if (isAllNetworkType(type)) {
//                    // checking pass, proceed.
//                    proceedingJoinPoint.proceed();
//                }

                if (STR_MOBILE.equals(type.name()))
                    // handles
                    handleMobile(NetworkSugar.isMobileNow(), descriptor, proceedingJoinPoint);

                else if (STR_MOBILE.equals(type.name())) {
                    // handles
                    handleWifi(NetworkSugar.isWifiNow(), descriptor, proceedingJoinPoint);

                } else
                    // only Network type ALL is remaining
                    // so directly proceed, nothing for us to do.
                    proceedingJoinPoint.proceed();

            } else {
                // handle offline
                handleOffline(descriptor, proceedingJoinPoint);
            }
        }
    }

    private NetSugarDescriptor getAnnotationParameter(JoinPoint joinPoint) {
        // first: obtain the signature of method.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // sec: get the method object through signature.
        Method methodAnnotated = signature.getMethod();

        // trd: get annotation of NetSugar
        NetSugar netSugar = methodAnnotated.getAnnotation(NetSugar.class);

        /* 4th: get values */
        boolean isMethodNeedCheck = netSugar.check();
        String offlineMethodName = isMethodNeedCheck ? netSugar.offline().method() : EMPTY_METHOD_NAME;
        NetworkType networkType = isMethodNeedCheck ? netSugar.online().type() : null;

        // final set values
        NetSugarDescriptor descriptor = new NetSugarDescriptor.Builder()
                .annotatedMethod(methodAnnotated.getName())
                .isCheck(isMethodNeedCheck)
                .networkType(networkType)
                .offlineMethod(offlineMethodName)
                .build();

        // note : I don't know whether should put descriptor into a map.
        // will this operation causes memory leak?
        // boolean isInMap = descriptorMap.containsKey(methodAnnotated.getName());
        // if (!isInMap) {
        //     descriptorMap.put(methodAnnotated.getName(), descriptor);
        // }

        return descriptor;
    }

    /**
     * Is all network allowed.
     *
     * @param type network type
     * @return true allowed otherwise false
     */
    private boolean isAllNetworkType(NetworkType type) {
        return null != type && "ALL".equals(type.name());
    }

    /**
     * Handles the situation network type is mobile.
     *
     * @param isMobile  is mobile network
     * @param joinPoint join point
     * @throws Throwable def throwable
     */
    private void handleMobile(boolean isMobile, NetSugarDescriptor descriptor, ProceedingJoinPoint joinPoint)
            throws Throwable {
        // if is mobile, it should proceed.
        if (isMobile)
            joinPoint.proceed();
        else
            handleOffline(descriptor, joinPoint);
    }

    /**
     * Handles the situation network type is wifi.
     *
     * @param isWifi    is mobile network
     * @param joinPoint join point
     * @throws Throwable def throwable
     */
    private void handleWifi(boolean isWifi, NetSugarDescriptor descriptor, ProceedingJoinPoint joinPoint)
            throws Throwable {
        // if is mobile, it should proceed.
        if (isWifi) joinPoint.proceed();
        else {
            handleOffline(descriptor, joinPoint);
        }
    }

    /**
     * Handle the situation of offline
     *
     * @param offlineMethodName method of offline defines
     * @param enclosingClass    enclosing class of method
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private void handleOffline(String offlineMethodName, Class<?> enclosingClass, Object methodRec)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // has the method
        // boolean isMethodExist = false;

        Method m = enclosingClass.getMethod(offlineMethodName, (Class<?>) null);
        if (m != null) {
            // TODO: 2017/2/18 There is obvious issue, When there are two methods having the same name but different args.
            // the two are all captured. However in annotation, we don't provide argument item for dev to set.
            // So, I assume that only one method will be coded and captured in target clz.

            // set accessible
            if (!m.isAccessible()) m.setAccessible(true);

            // invoke the offline method.
            m.invoke(methodRec, (Object[]) null);
        } else {
            throw new NoSuchMethodException("There is no such a method you define in @offline!");
        }
    }

    /**
     * handles offline.
     * We can define sth. here.
     */
    private void handleOffline(NetSugarDescriptor descriptor, ProceedingJoinPoint point)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        boolean isMethodExist = false;

        // get offline method name;
        String offlineMethodName = descriptor.getOfflineMethodName();

        // get the method signature;
        MethodSignature signature = (MethodSignature) point.getSignature();

        // reflect method
        Method methodAnnotated = signature.getMethod();

        // get enclosing class
        Class<?> enclosingClass = methodAnnotated.getDeclaringClass();

        for (Method m : enclosingClass.getDeclaredMethods()) {
            if (!offlineMethodName.equals(m.getName())) {
                isMethodExist = false;
            } else {
                // TODO: 2017/2/18 There is obvious issue, When there are two methods having the same name but different args.
                // the two are all captured. However in annotation, we don't provide argument item for dev to set.
                // So, I assume that only one method will be coded and captured in target clz.

                // set accessible
                if (!m.isAccessible()) m.setAccessible(true);

                // invoke the offline method.
                m.invoke(point.getTarget(), (Object[]) null);

                isMethodExist = true;
                break;
            }
        }

        if (!isMethodExist) {
            throw new NoSuchMethodException("There is no such a method you define in @offline!");
        }
    }
}
