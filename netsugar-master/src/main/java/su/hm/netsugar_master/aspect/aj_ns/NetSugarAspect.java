package su.hm.netsugar_master.aspect.aj_ns;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import su.hm.netsugar_master.NetworkSugar;
import su.hm.netsugar_master.annotations.NetSugar;
import su.hm.netsugar_master.annotations.Offline;
import su.hm.netsugar_master.configs.Configs;
import su.hm.netsugar_master.entity.MatchResult;
import su.hm.netsugar_master.entity.NetSugarDescriptor;
import su.hm.netsugar_master.entity.NetworkType;
import su.hm.netsugar_master.entity.OfflineDescriptor;
import su.hm.netsugar_master.ex.NoHookException;
import su.hm.netsugar_master.ex.WrongPairException;

import static su.hm.netsugar_master.configs.Configs.DEF_PAIR_VAL;
import static su.hm.netsugar_master.configs.Configs.EXECUTION;
import static su.hm.netsugar_master.configs.Configs.NET_SUGAR_ANNO;
import static su.hm.netsugar_master.configs.Configs.OFFLINE_ANNO;
import static su.hm.netsugar_master.configs.Configs.STR_MOBILE;
import static su.hm.netsugar_master.configs.Configs.STR_WIFI;

/**
 * NetSugarAspect is just the aspect that we are to cut cross.
 * Here we define pointcuts and advices, join points and aspect.
 * <p>
 * Created by hm-su on 2017/2/18.
 */

@Aspect
public class NetSugarAspect {

    // A map to load NetSugar descriptor
    private ArrayMap<String, NetSugarDescriptor> netSugarMap;

    // A map to load Offline descriptor
    private ArrayMap<String, OfflineDescriptor> offlineMap;

    private final String WRONG_PAIR = "Pair value cannot be -1 or smaller than 0.";

    @SuppressWarnings("FieldCanBeLocal")
    private final String NO_HOOK = "Please declares one method annotated with " +
            "@Offline and its value of pair equals value in @NetSugar.";

    /**
     * 0x01: offline
     * 0x10: network type not match
     * 0x0:  default value
     */
    private int matchType;

    /* init */ {
        netSugarMap = new ArrayMap<>();
        offlineMap = new ArrayMap<>();
        matchType = Configs.FLAG_NO_NEED;
    }

    // "execution(@su.hm.netsugar_master.annotations.NetSugar * *(..))"
    // This means that around advice runs when this kind of method executing
    // which return types and name and parameters have no limitation.
    // it is a property-based join point.
    // private static final String POINTCUT = "execution(@su.hm.netsugar_master.annotations.NetSugar * *(..))";
    private static final String NS_POINTCUT = EXECUTION + "(" + NET_SUGAR_ANNO + " * *(..))";
    //private static final String NS_POINTCUT = "execution(* *.)";

    // it is a property-based join point for methods annotated with @Offline
    private static final String OFF_POINTCUT = EXECUTION + "(" + OFFLINE_ANNO + " * *(..))";

    // here defines a point cut with annotation.
    // AspectJ 5 defines a pointcut: pointcut pc() : call(void Foo.m());
    // pc() is the name of pointcut just like the name of 'methodAnnotated'.
    // void Foo.m() is the join point.
    @Pointcut(value = NS_POINTCUT)
    public void netSugarMethod() {
        /* do nothing here. */
    }

    @Pointcut(value = OFF_POINTCUT)
    public void offMethod() {
        /* do nothing here. */
    }

    @Around(value = "offMethod()")
    public void offlineCut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log.i("handleOffline", "method is executed");
        /*
         * Why we are going to block offline hook method execution.
         * Because we want to insert an argument 'matchType' into the method.
         * When I consider that offline and network type not matching are the different situations,
         * matchType is useful to distinguish different situations.
         */
        Object[] argsObjectArray = proceedingJoinPoint.getArgs();
        if (null == argsObjectArray) {
            throw new Throwable("There is must a param 'matchResult' and MatchResult type in this method.");
        }

        Object firstArg = argsObjectArray[0];

        // check type
        if (!(firstArg instanceof MatchResult)) {
            throw new Throwable("The param named 'matchType' should be a instance of MatchResult.");
        }

        // execute method.
        proceedingJoinPoint.proceed(argsObjectArray);
    }

    /**
     * -m(v1.0)
     * method name is changed to handleNetSugars().
     * The entire logical is:
     * 1.check network state actually(that is isConnected() firstly).
     * 2 - 1. if network is connected : get the type value and check what it is.
     * 2 - 2. if type is identified successfully, proceed.
     * 2 - 3. else check do we provide pair.
     */
    @Around(value = "netSugarMethod()")
    public void netSugarsCut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // get descriptor
        NetSugarDescriptor descriptor = getNetSugarDescriptor(proceedingJoinPoint);

        boolean isConnected = NetworkSugar.isNetworkConnected();
        if (isConnected) {
            /* if connected, check type */
            NetworkType type = descriptor.getNetworkType();

            if (STR_MOBILE.equals(type.name())) {
                // if type is mobile, handles it
                handleMobile(NetworkSugar.isMobileNow(), descriptor, proceedingJoinPoint);

            } else if (STR_WIFI.equals(type.name())) {
                // if type is wifi, handles it.
                handleWifi(NetworkSugar.isWifiNow(), descriptor, proceedingJoinPoint);

            } else {
                // type is all
                proceedingJoinPoint.proceed();
            }
        } else {
            // set offline flag
            matchType = Configs.FLAG_OFF;

            // handle offline
            handleOffline(descriptor, proceedingJoinPoint);
        }
    }

    /**
     * Encapsulates @NetSugar information into {@link NetSugarDescriptor};
     * It only obtains information from NetSugar.
     *
     * @param joinPoint join point
     * @return descriptor encapsulates some information about NetSugar
     */
    private NetSugarDescriptor getNetSugarDescriptor(JoinPoint joinPoint) throws NoHookException {

        // first: obtain the signature of method.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // sec: get the method object through signature.
        // !! methodName is a full path of Class name and method name.
        Method methodAnnotated = signature.getMethod();
        String methodName = methodAnnotated.getDeclaringClass().getSimpleName()
                .concat("$" + methodAnnotated.getName());

        /* create or get */
        NetSugarDescriptor descriptor;
        boolean hasDescriptor = netSugarMap.containsKey(methodName);
        if (!hasDescriptor) {
            // get annotation of NetSugar
            NetSugar netSugar = methodAnnotated.getAnnotation(NetSugar.class);

            /* 4th: get values */
            NetworkType type = netSugar.type();
            int pairKey = netSugar.pair();

            // wrong pair value will throw exception.
            boolean isNotAllowed = DEF_PAIR_VAL == pairKey || pairKey < 0;
            if (isNotAllowed) {
                throw new WrongPairException(WRONG_PAIR);
            }

            // get the hook method annotated with @Offline
            Class<?> enclosingCls = methodAnnotated.getDeclaringClass();
            List<Method> hookMethodList = pickOutOfflineMethodInTargetClassWithPair(enclosingCls, pairKey);
            Method hookM = hookMethodList.get(0);

            // final set values
            descriptor = new NetSugarDescriptor.Builder()
                    .type(type)
                    .pair(pairKey)
                    .method(methodName)
                    .hook(hookM)
                    .build();
        } else {
            descriptor = netSugarMap.get(methodName);
        }

        return descriptor;
    }

    /**
     * Encapsulates @Offline information into {@link Offline};
     * It only obtains information from Offline.
     *
     * @param joinPoint join point
     * @return descriptor encapsulates some information about Offline
     */
    @SuppressWarnings("unused")
    private OfflineDescriptor getOfflineDescriptor(JoinPoint joinPoint) {

        // first: obtain the signature of method.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // sec: get the method object through signature.
        // !! methodName is a full path of Class name and method name.
        Method methodAnnotated = signature.getMethod();
        String methodName = methodAnnotated.getDeclaringClass().getSimpleName()
                .concat("$" + methodAnnotated.getName());

        OfflineDescriptor descriptor;
        boolean hasDescriptor = offlineMap.containsKey(methodName);
        if (!hasDescriptor) {
            // get annotation of NetSugar
            Offline netSugar = methodAnnotated.getAnnotation(Offline.class);

            /* 4th: get values */
            int pairKey = netSugar.pair();

            // final set values
            descriptor = new OfflineDescriptor.Builder()
                    .pair(pairKey)
                    .method(methodName)
                    .build();
        } else {
            descriptor = offlineMap.get(methodName);
        }

        return descriptor;
    }

    /**
     * Is all network allowed.
     *
     * @param type network type
     * @return true allowed otherwise false
     */
    @SuppressWarnings("unused")
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
        else {
            // set not match flag
            matchType = Configs.FLAG_NOT_MATCH;

            // handle offline
            handleOffline(descriptor, joinPoint);
        }
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
            // set not match flag
            matchType = Configs.FLAG_NOT_MATCH;

            // handle offline
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
    @SuppressWarnings("unused")
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
     * <p>
     * <strong>
     * Once hook method is invoked, another @Around of ApsectJ will be executed.
     * At that moment, we will block the execution of hook method.
     * We can do everything, it's everything. ^.^
     * </strong>
     */
    private void handleOffline(NetSugarDescriptor descriptor, ProceedingJoinPoint point)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method hook = descriptor.getHookMethod();

        // get parameter list for args object array.
        String hookGenString = hook.toGenericString();
        int ltBracketIndex = hookGenString.indexOf("(");
        int rtBracketIndex = hookGenString.lastIndexOf(")");
        String[] argsList = hookGenString.substring(ltBracketIndex + 1, rtBracketIndex).split(",");

        // set accessible
        if (!hook.isAccessible()) hook.setAccessible(true);

        // invoke the offline method.
        Log.i("Who Is Target", point.getTarget().toString());
        Object[] args = new Object[argsList.length];

        // build a matching result
        MatchResult matchResult = new MatchResult.Builder()
                .match(matchType)
                .reason()
                .type(descriptor.getNetworkType())
                .build();

        // as the first param pass to target method.
        args[0] = matchResult;

        // invoke method
        hook.invoke(point.getTarget(), args);
    }

    /**
     * This method wants to pick out a method that has the same pair with @NetSugar.
     * But only one method can be matched.
     *
     * @param mEnclosingClass enclosing class of method annotated with Offline
     * @param netSugarPair    key pair of net sugar providing
     * @return a list
     * @throws NoHookException no such a method
     */
    private List<Method> pickOutOfflineMethodInTargetClassWithPair(Class<?> mEnclosingClass, int netSugarPair)
            throws NoHookException {

        if (null == mEnclosingClass) {
            return Collections.emptyList();
        }

        Method hookMethod = null;
        Method[] methods = mEnclosingClass.getDeclaredMethods();
        for (Method m : methods) {
            // whether a method is annotated with Offline
            if (m.isAnnotationPresent(Offline.class)) {
                /* if true */
                Offline offline = m.getAnnotation(Offline.class);
                int pairKey = offline.pair();

                // throw exception when value is -1 or smaller than 0;
                boolean isNotAllowed = DEF_PAIR_VAL == pairKey || pairKey < 0;
                if (isNotAllowed) {
                    throw new WrongPairException(WRONG_PAIR);
                }

                /* pair equals net sugar pair, break loop. only one method can be matched. */
                if (pairKey == netSugarPair) {
                    hookMethod = m;
                    break;
                }
            }
        }

        /* if no method found, throw exception to notify dev. */
        // boolean isEmptyList = Utils.isEmptyList(annotatedMethods);
        if (null == hookMethod) {
            throw new NoHookException(NO_HOOK);
        }

        return Collections.singletonList(hookMethod);
    }
}
