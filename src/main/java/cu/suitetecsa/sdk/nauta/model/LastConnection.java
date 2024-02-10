package cu.suitetecsa.sdk.nauta.model;

public class LastConnection {
    String from;
    String time;
    String to;

    public LastConnection(String from, String time, String to) {
        this.from = from;
        this.time = time;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }
}
