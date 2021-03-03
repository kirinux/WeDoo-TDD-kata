package org.craftedsw.kirin.wedoo.distribute.infra.exporter;

import org.craftedsw.kirin.wedoo.distribute.*;
import org.craftedsw.kirin.wedoo.distribute.api.loader.CompaniesData;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class JSONExporterTest {

    @Test
    void should_export_data_from_domain_objects() {
        JSONExporter exporter = new JSONExporter();
        List<Company> companies = List.of(CompanyTestBuilder.newCompany().build(), CompanyTestBuilder.newCompany().build());
        List<Endowment> endowment = List.of(EndowmentTestBuilder.newEndowment().build(), EndowmentTestBuilder.newEndowment().build());

        Distribution distribution1 = Distribution.newDistributionStartToday(Amount.of(10));
        distribution1.register(companies.get(0), endowment.get(0));
        Distribution distribution2 = Distribution.newDistributionStartToday(Amount.of(100));
        distribution2.register(companies.get(0), endowment.get(0));

        List<Distribution> distributions = List.of(distribution1, distribution2);
        CompaniesData data = exporter.convert(companies, endowment, distributions);

        Assertions.assertAll(
                () -> org.assertj.core.api.Assertions.assertThat(data.getCompanies()).hasSize(companies.size()),
                () -> org.assertj.core.api.Assertions.assertThat(data.getEndowments()).hasSize(endowment.size()),
                () -> org.assertj.core.api.Assertions.assertThat(data.getDistributions()).hasSize(distributions.size())
        );


    }

}