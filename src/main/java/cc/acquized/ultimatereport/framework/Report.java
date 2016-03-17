package cc.acquized.ultimatereport.framework;

public class Report {

    private String reporter;
    private String victim;
    private String reason;

    public Report(String reporter, String victim, String reason) {
        this.reporter = reporter;
        this.victim = victim;
        this.reason = reason;
    }

    public String getReporter() {
        return reporter;
    }

    public String getVictim() {
        return victim;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
