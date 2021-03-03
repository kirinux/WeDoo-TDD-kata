package org.craftedsw.kirin.wedoo.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Amount {

    private final double value;

    private Amount(double value) {
        this.value = value;
    }

    /*
    We could use cache here
     */
    public static Amount of(double amount) {
        return new Amount(amount);
    }

    public boolean isGreaterThan(double value) {
        return (value - this.value) < 0;
    }
}
