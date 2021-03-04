package org.craftedsw.kirin.wedoo.distribute;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.craftedsw.kirin.wedoo.domain.Amount;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WalletBalance {

    private final double value;
    private final Wallet.Type type;

    public static WalletBalance forGift(double amount) {
        return new WalletBalance(amount, Wallet.Type.GIFT);
    }

    public static WalletBalance forFood(double amount) {
        return new WalletBalance(amount, Wallet.Type.FOOD);
    }

    public static WalletBalance forType(double amount, Wallet.Type walletType) {
        return new WalletBalance(amount, walletType);
    }

    public WalletBalance minus(Amount amount) {
        return new WalletBalance(value - amount.getValue(), type);
    }

    public WalletBalance plus(Amount amount) {
        return new WalletBalance(value + amount.getValue(), type);
    }
}
