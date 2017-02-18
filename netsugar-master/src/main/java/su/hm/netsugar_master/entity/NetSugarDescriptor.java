package su.hm.netsugar_master.entity;


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
     * name of method which is annotated with {@link NetSugar}
     * as a key in a ArrayMap
     */
    private String annotatedMethodName;

    /**
     * to @NetSugar check method
     */
    private boolean isCheck;

    /**
     * to @Online network type method
     */
    private NetworkType networkType;

    /**
     * to @Offline method
     */
    private String offlineMethodName;

    private NetSugarDescriptor() {
        // do not provide this constructor
    }

    // private constructor with builder instance.
    private NetSugarDescriptor(Builder builder) {
        annotatedMethodName = builder.annotatedMethodName;
        isCheck = builder.isCheck;
        networkType = builder.networkType;
        offlineMethodName = builder.offlineMethodName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public String getOfflineMethodName() {
        return offlineMethodName;
    }

    public String getAnnotatedMethodName() {
        return annotatedMethodName;
    }

    // use builder to create NetSugarDescription method.
    public static class Builder {
        private String annotatedMethodName;
        private boolean isCheck;
        private NetworkType networkType;
        private String offlineMethodName;

        public Builder annotatedMethod(String methodName) {
            this.annotatedMethodName = methodName;
            return this;
        }

        public Builder isCheck(boolean isCheck) {
            this.isCheck = isCheck;
            return this;
        }

        public Builder networkType(NetworkType networkType) {
            this.networkType = networkType;
            return this;
        }

        public Builder offlineMethod(String methodName) {
            this.offlineMethodName = methodName;
            return this;
        }

        public NetSugarDescriptor build() {
            return new NetSugarDescriptor(this);
        }
    }

    @Override
    public String toString() {
        return "NetSugarDescriptor{" +
                "isCheck=" + isCheck +
                ", networkType=" + networkType +
                ", offlineMethodName='" + offlineMethodName + '\'' +
                '}';
    }
}
