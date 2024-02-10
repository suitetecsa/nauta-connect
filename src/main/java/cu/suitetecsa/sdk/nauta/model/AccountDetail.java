package cu.suitetecsa.sdk.nauta.model;

public record NautaUser(
        String username,
        long blockingDate,
        long dateOfElimination,
        String accountType,
        String serviceType,
        float credit,
        long time,
        String mailAccount,
        String offer
) {}
