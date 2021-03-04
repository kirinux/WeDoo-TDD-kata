package org.craftedsw.kirin.wedoo.distribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Balances {

    private final List<Distribution> distributions;
    private final Map<Wallet.Type, WalletBalance> balances;

    private Balances(Map<Wallet.Type, WalletBalance> balances) {
        this.balances = balances;
        this.distributions = new ArrayList<>();
    }

    public static Balances initialBalances() {
        return initialBalances(Map.of(
                Wallet.Type.GIFT, WalletBalance.forGift(0),
                Wallet.Type.FOOD, WalletBalance.forFood(0)
        ));
    }

    public static Balances initialBalances(Map<Wallet.Type, WalletBalance> balances) {
        return new Balances(balances);
    }

    public WalletBalance giftBalance() {
        return getBalance(Wallet.Type.GIFT);
    }

    public WalletBalance foodBalance() {
        return getBalance(Wallet.Type.FOOD);
    }

    public void add(Distribution distribution) {
        this.distributions.add(distribution);
    }

    public List<Distribution> getDistributionsByType(Wallet.Type walletType) {
        return distributions.stream()
                .filter(distribution -> distribution.getType().equals(walletType))
                .collect(Collectors.toUnmodifiableList());
    }

    private WalletBalance getBalance(Wallet.Type walletType) {
        double sum = distributions.stream()
                .filter(distribution -> distribution.getType().equals(walletType))
                .filter(Distribution::isValid)
                .mapToDouble(distribution -> distribution.getAmount().getValue())
                .sum();
        WalletBalance walletBalance = balances.getOrDefault(walletType, WalletBalance.forType(0, walletType));
        return WalletBalance.forType(sum + walletBalance.getValue(), walletType);
    }

    public List<WalletBalance> getAll() {
        return Stream.of(giftBalance(), foodBalance())
                .filter(balance -> balance != null)
                .collect(Collectors.toUnmodifiableList());
    }
}
