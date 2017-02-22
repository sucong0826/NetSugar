package su.hm.netsugar_master.entity;

import su.hm.netsugar_master.configs.Configs;

/**
 * Match result is a class designed for information of matching.
 * <p>
 * Created by hm-su on 2017/2/21.
 */
public class MatchResult {
    /**
     * match type means the matching result.
     * result is a primitive int.
     * <p>
     * Three values:
     * offline matching : {@link Configs} FLAG_OFF(0x01)
     * type not matching : {@link Configs} FLAG_NOT_MATCH(0x01 << 1)
     * no need to match : {@link Configs} FLAG_NO_NEED(0x0)
     */
    private int matchType;

    /**
     * details for describing matching.
     */
    private String reason;

    /**
     * The current network is what.
     */
    private NetworkType currentNetworkType;

    {
        // init reason
        reason = "";
    }

    private MatchResult() {
        // private cons
    }

    private MatchResult(Builder builder) {
        this.matchType = builder.matchType;
        this.reason = builder.reason;
        this.currentNetworkType = builder.type;
    }

    public static class Builder {
        private int matchType;
        private String reason;
        private NetworkType type;

        public Builder match(int matchType) {
            this.matchType = matchType;
            return this;
        }

        public Builder reason() {
            reason = reasonComposer();
            return this;
        }

        public Builder type(NetworkType networkType) {
            this.type = networkType;
            return this;
        }

        public MatchResult build() {
            return new MatchResult(this);
        }

        private String reasonComposer() {
            if (Configs.FLAG_NO_NEED == matchType)
                return "No need to match";
            else if (Configs.FLAG_NOT_MATCH == matchType)
                return "Network type is not matched with your setting";
            else if (Configs.FLAG_OFF == matchType)
                return "Sorry, your network is disconnected or a captive portal maybe.";
            else
                return ".....";
        }
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "currentNetworkType=" + currentNetworkType +
                ", reason='" + reason + '\'' +
                ", matchType=" + matchType +
                '}';
    }

    public int getMatchType() {
        return matchType;
    }

    public String getReason() {
        return reason;
    }

    public NetworkType getCurrentNetworkType() {
        return currentNetworkType;
    }
}
