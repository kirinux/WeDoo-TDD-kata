package org.craftedsw.kirin.wedoo.distribute.loader;

import org.craftedsw.kirin.wedoo.distribute.api.loader.JSONLoader;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryCompanyRepository;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryEndowmentRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class JSONLoaderTest {

    @Test
    void should_load_data_and_populate_repositories_from_existing_json_file() throws IOException {
        JSONLoader loader = new JSONLoader();
        InMemoryEndowmentRepository endowmentRepository = new InMemoryEndowmentRepository();
        InMemoryCompanyRepository companyRepository = new InMemoryCompanyRepository();
        loader.load(JSONLoader.class.getResourceAsStream("/Level1/data/input.json"), endowmentRepository, companyRepository);

        assertThat(endowmentRepository.getEndowment(1)).isPresent();
        assertThat(companyRepository.getCompany("Wedoogift")).isPresent();
    }

}