package org.craftedsw.kirin.wedoo.distribute;

import com.github.javafaker.Faker;
import org.craftedsw.kirin.wedoo.distribute.api.loader.JSONLoader;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryCompanyRepository;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryEndowmentRepository;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryWalletRepository;
import org.craftedsw.kirin.wedoo.domain.Balance;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DistributionServiceTestBuilder {

    private final Faker faker = new Faker();
    private CompanyRepository companyRepository = mock(CompanyRepository.class);
    private EndowmentRepository endowmentRepository = mock(EndowmentRepository.class);
    private WalletRepository walletRepository = mock(WalletRepository.class);

    public static DistributionServiceTestBuilder newDistributionService() {
        return new DistributionServiceTestBuilder();
    }

    public DistributionServiceTestBuilder withUnknownCompany() {
        when(companyRepository.getCompany(anyString()))
                .thenThrow(new ObjectNotFoundException("company not found"));

        return this;
    }

    public DistributionService build() {
        return new DistributionService(endowmentRepository, companyRepository, walletRepository);
    }

    public DistributionServiceTestBuilder withCompanyWithBalance(double balance) {
        return withCompanyWithBalance(balance, faker.company().name());
    }

    public DistributionServiceTestBuilder withCompanyWithBalance(double balance, String companyName) {
        Company company = new Company(faker.number().randomNumber(2, false), companyName, Balance.of(balance), this.endowmentRepository);
        when(companyRepository.getCompany(anyString()))
                .thenReturn(Optional.of(company));
        return this;
    }

    public DistributionService buildWithData(String path) throws IOException {
        DistributionService service = new DistributionService(new InMemoryEndowmentRepository(), new InMemoryCompanyRepository(), new InMemoryWalletRepository());
        JSONLoader loader = new JSONLoader();
        service.load(loader, DistributionService.class.getResourceAsStream(path));
        return service;
    }

    public DistributionServiceTestBuilder withEndowment(long endowmentId, double initialBalanceForAll) {
        when(endowmentRepository.getEndowment(endowmentId))
                .thenReturn(Optional.of(new Endowment(endowmentId, CompanyTestBuilder.balances(initialBalanceForAll, initialBalanceForAll))));
        return this;
    }

    public DistributionServiceTestBuilder withHugeBalanceCompany(String companyName) {
        return withCompanyWithBalance(Double.MAX_VALUE, companyName);
    }

    public DistributionServiceTestBuilder withAllWallets() {
        InMemoryWalletRepository walletRepository = new InMemoryWalletRepository();
        walletRepository.addWallet(new Wallet(1, "gift cards", Wallet.Type.GIFT));
        walletRepository.addWallet(new Wallet(2, "food cards", Wallet.Type.FOOD));
        this.walletRepository = walletRepository;
        return this;
    }
}
