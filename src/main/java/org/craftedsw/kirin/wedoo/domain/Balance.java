package org.craftedsw.kirin.wedoo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Balance {

    private final double value;

    public static Balance of(double amount) {
        return new Balance(amount);
    }

    public Balance minus(Amount amount) {
        return new Balance(value - amount.getValue());
    }

    public Balance plus(Amount amount) {
        return new Balance(value + amount.getValue());
    }
}
