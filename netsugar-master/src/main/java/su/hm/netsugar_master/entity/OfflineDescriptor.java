package su.hm.netsugar_master.entity;

/**
 * Offline descriptor describes some information about {@link su.hm.netsugar_master.annotations.Offline}.
 * <p>
 * Created by hm-su on 2017/2/20.
 */

public class OfflineDescriptor {

    /**
     * a key for pairing another method
     */
    private int pair;

    /**
     * method name which is annotated with NetSugar.
     * As key for ArrayMap.
     */
    private String method;

    // private constructor with builder instance.
    private OfflineDescriptor(Builder builder) {
        pair = builder.pair;
        method = builder.method;
    }

    // use builder to create NetSugarDescription method.
    public static class Builder {
        private int pair;
        private String method;

        public Builder pair(int pair) {
            this.pair = pair;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public OfflineDescriptor build() {
            return new OfflineDescriptor(this);
        }
    }

    @Override
    public String toString() {
        return "OfflineDescriptor{" +
                "method='" + method + '\'' +
                ", pair=" + pair +
                '}';
    }


    public int getPair() {
        return pair;
    }

    public String getMethod() {
        return method;
    }
}
