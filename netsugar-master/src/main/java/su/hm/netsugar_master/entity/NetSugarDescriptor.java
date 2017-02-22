package su.hm.netsugar_master.entity;


import java.lang.reflect.Method;

import su.hm.netsugar_master.annotations.NetSugar;

/**
 * This class is an entity to describe a {@link NetSugar} annotation.
 * What items an annotation has.
 * Use builder pattern to build an instance of it;
 * <p>
 * Created by hm-su on 2017/2/18.
 */

public class NetSugarDescriptor {

    /**
     * to @Online network type method
     */
    private NetworkType type;

    /**
     * a key for pairing another method
     */
    private int pair;

    /**
     * method name which is annotated with NetSugar.
     * As key for ArrayMap.
     */
    private String method;

    /**
     * One method annotated with @NetSugar
     * is associated with one method annotated with @Offline.
     */
    private Method hookMethod;

    // private constructor with builder instance.
    private NetSugarDescriptor(Builder builder) {
        type = builder.type;
        pair = builder.pair;
        method = builder.method;
        hookMethod = builder.hookMethod;
    }


    public NetworkType getNetworkType() {
        return this.type;
    }

    // use builder to create NetSugarDescription method.
    public static class Builder {
        private NetworkType type;
        private int pair;
        private String method;
        private Method hookMethod;

        public Builder type(NetworkType type) {
            this.type = type;
            return this;
        }

        public Builder pair(int pair) {
            this.pair = pair;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder hook(Method method) {
            this.hookMethod = method;
            return this;
        }

        public NetSugarDescriptor build() {
            return new NetSugarDescriptor(this);
        }
    }

    @Override
    public String toString() {
        return "NetSugarDescriptor{" +
                "type=" + type +
                ", pair=" + pair +
                ", method=" + method +
                ", hook=" + hookMethod +
                '}';
    }

    public NetworkType getType() {
        return type;
    }

    public int getPair() {
        return pair;
    }

    public String getMethod() {
        return method;
    }

    public Method getHookMethod() {
        return hookMethod;
    }
}
