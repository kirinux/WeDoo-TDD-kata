package org.craftedsw.kirin.wedoo.distribute;

import java.io.IOException;
import java.io.InputStream;

public interface Loader {

    void load(InputStream inputJSON, EndowmentRepository endowmentRepository, CompanyRepository companyRepository, WalletRepository walletRepository) throws IOException;
}
