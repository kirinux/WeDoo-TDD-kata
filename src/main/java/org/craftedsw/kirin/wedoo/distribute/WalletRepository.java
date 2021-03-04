package org.craftedsw.kirin.wedoo.distribute;

import java.util.Optional;

public interface WalletRepository {

    /**
     * @param id wallet id
     * @return
     */
    Optional<Wallet> getWallet(long id);

    void addWallet(Wallet wallet);

    /**
     * @param name wallet name
     * @return
     */
    Optional<Wallet> getWallet(String name);

    Optional<Wallet> getWallet(Wallet.Type type);
}
