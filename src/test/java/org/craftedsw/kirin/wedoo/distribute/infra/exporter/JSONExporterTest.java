package org.craftedsw.kirin.wedoo.distribute.infra.exporter;

import org.craftedsw.kirin.wedoo.distribute.*;
import org.craftedsw.kirin.wedoo.distribute.api.loader.CompaniesData;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryWalletRepository;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class JSONExporterTest {

    @Test
    void should_export_data_from_domain_objects() {
        InMemoryWalletRepository walletRepository = new InMemoryWalletRepository();
        walletRepository.addWallet(new Wallet(1, "gift cards", Wallet.Type.GIFT));
        walletRepository.addWallet(new Wallet(2, "food cards", Wallet.Type.FOOD));
        JSONExporter exporter = new JSONExporter(walletRepository);
        List<Company> companies = List.of(CompanyTestBuilder.newCompany().build(), CompanyTestBuilder.newCompany().build());
        List<Endowment> endowment = List.of(EndowmentTestBuilder.newEndowment().build(), EndowmentTestBuilder.newEndowment().build());

        Distribution giftDistribution1 = Distribution.newGiftDistributionStartToday(Amount.of(10));
        giftDistribution1.register(companies.get(0), endowment.get(0));
        Distribution giftDistribution2 = Distribution.newGiftDistributionStartToday(Amount.of(100));
        giftDistribution2.register(companies.get(0), endowment.get(0));
        Distribution foodDistribution = Distribution.newFoodDistributionStartToday(Amount.of(300));
        foodDistribution.register(companies.get(0), endowment.get(0));

        List<Distribution> distributions = List.of(giftDistribution1, giftDistribution2, foodDistribution);
        CompaniesData data = exporter.convert(companies, endowment, distributions);

        Assertions.assertAll(
                () -> org.assertj.core.api.Assertions.assertThat(data.getCompanies()).hasSize(companies.size()),
                () -> org.assertj.core.api.Assertions.assertThat(data.getEndowments()).hasSize(endowment.size()),
                () -> org.assertj.core.api.Assertions.assertThat(data.getDistributions()).hasSize(distributions.size())
        );


    }

}