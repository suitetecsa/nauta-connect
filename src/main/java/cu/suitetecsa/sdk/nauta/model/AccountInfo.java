package cu.suitetecsa.sdk.nauta.model;

public class AccountInfo {
    String accessAreas;
    String accountStatus;
    String credit;
    String expirationDate;

    public AccountInfo(String accessAreas, String accountStatus, String credit, String expirationDate) {
        this.accessAreas = accessAreas;
        this.accountStatus = accountStatus;
        this.credit = credit;
        this.expirationDate = expirationDate;
    }

    public String getAccessAreas() {
        return accessAreas;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getCredit() {
        return credit;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
