package org.craftedsw.kirin.wedoo.distribute.infra;

import org.craftedsw.kirin.wedoo.distribute.Wallet;
import org.craftedsw.kirin.wedoo.distribute.WalletRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryWalletRepository implements WalletRepository {

    private final Map<Long, Wallet> wallets = new HashMap<>();

    @Override
    public Optional<Wallet> getWallet(long id) {
        return Optional.ofNullable(wallets.get(id));
    }

    @Override
    public void addWallet(Wallet wallet) {
        wallets.put(wallet.getId(), wallet);
    }

    @Override
    public Optional<Wallet> getWallet(String name) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Wallet> getWallet(Wallet.Type type) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getType().equals(type))
                .findFirst();
    }
}
