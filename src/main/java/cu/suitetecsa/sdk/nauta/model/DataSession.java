package cu.suitetecsa.sdk.nauta.model;

public class DataSession {
    private final String username;
    private final String csrfHw;
    private final String wlanUserIp;
    private final String attributeUUID;

    public DataSession(String username, String csrfHw, String wlanUserIp, String attributeUUID) {
        this.username = username;
        this.csrfHw = csrfHw;
        this.wlanUserIp = wlanUserIp;
        this.attributeUUID = attributeUUID;
    }

    public String getUsername() {
        return username;
    }

    public String getCsrfHw() {
        return csrfHw;
    }

    public String getWlanUserIp() {
        return wlanUserIp;
    }

    public String getAttributeUUID() {
        return attributeUUID;
    }
}
