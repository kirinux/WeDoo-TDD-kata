package org.craftedsw.kirin.wedoo.distribute.api.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.craftedsw.kirin.wedoo.distribute.CompanyRepository;
import org.craftedsw.kirin.wedoo.distribute.EndowmentRepository;
import org.craftedsw.kirin.wedoo.distribute.Loader;
import org.craftedsw.kirin.wedoo.distribute.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public final class JSONLoader implements Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONLoader.class);

    private final ObjectMapper mapper;
    private final ObjectReader objectReader;

    public JSONLoader() {
        mapper = new ObjectMapper();
        objectReader = mapper.readerFor(CompaniesData.class);
    }

    @Override
    public void load(InputStream inputJSON,
                     EndowmentRepository endowmentRepository,
                     CompanyRepository companyRepository,
                     WalletRepository walletRepository) throws IOException {
        CompaniesData companiesData = objectReader.readValue(inputJSON);

        companiesData.getWallets().stream()
                .map(CompaniesData.WalletDTO::toWallet)
                .forEach(walletRepository::addWallet);

        companiesData.getEndowments().stream()
                .map((CompaniesData.EndowmentDTO endowmentDTO) -> endowmentDTO.toEndowment(walletRepository))
                .forEach(endowmentRepository::addEndowment);

        companiesData.getCompanies().stream()
                .map(companyDTO -> companyDTO.toCompany(endowmentRepository))
                .forEach(companyRepository::addNewCompany);


        LOGGER.debug("Load done");
    }

}
