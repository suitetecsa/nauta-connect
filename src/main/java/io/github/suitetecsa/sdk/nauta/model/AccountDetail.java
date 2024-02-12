package io.github.suitetecsa.sdk.nauta.model;

public record AccountDetail(
        String username,
        long blockDate,
        long deletionDate,
        String accountType,
        String serviceType,
        double availableBalance,
        long accountTime,
        String email,
        String offer,
        Double monthlyFee,
        String downloadSpeed,
        String uploadSpeed,
        String phoneNumber,
        String linkIdentifiers,
        String linkStatus,
        Long activationDate,
        Long adslBlockDate,
        Long adslDeletionDate,
        Double feeBalance,
        Double bonus,
        Double debt
) {}
