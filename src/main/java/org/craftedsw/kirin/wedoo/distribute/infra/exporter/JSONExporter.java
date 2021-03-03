package org.craftedsw.kirin.wedoo.distribute.infra.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.craftedsw.kirin.wedoo.distribute.Company;
import org.craftedsw.kirin.wedoo.distribute.Distribution;
import org.craftedsw.kirin.wedoo.distribute.Endowment;
import org.craftedsw.kirin.wedoo.distribute.Exporter;
import org.craftedsw.kirin.wedoo.distribute.api.loader.CompaniesData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class JSONExporter implements Exporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONExporter.class);

    private final ObjectMapper mapper;
    private final ObjectWriter writer;

    public JSONExporter() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        writer = mapper.writerFor(CompaniesData.class);
    }

    @Override
    @SneakyThrows
    public void export(Writer outputWriter, List<Company> companies, List<Endowment> endowments, List<Distribution> distributions) {
        CompaniesData datas = convert(companies, endowments, distributions);
        writer.writeValue(outputWriter, datas);
    }

    CompaniesData convert(List<Company> companies, List<Endowment> endowments, List<Distribution> distributions) {
        List<CompaniesData.CompanyDTO> companyDTOS = companies.stream()
                .map(CompaniesData.CompanyDTO::fromCompany)
                .collect(Collectors.toUnmodifiableList());

        List<CompaniesData.EndowmentDTO> endowmentDTOS = endowments.stream()
                .map(CompaniesData.EndowmentDTO::fromEndowment)
                .collect(Collectors.toUnmodifiableList());

        List<CompaniesData.DistributionDTO> distributionDTOS = distributions.stream()
                .map(CompaniesData.DistributionDTO::fromDistribution)
                .collect(Collectors.toUnmodifiableList());
        LOGGER.debug("Export {} companies, {} endowment, {} distribution ", companyDTOS.size(), endowmentDTOS.size(), distributionDTOS.size());
        return new CompaniesData(companyDTOS, endowmentDTOS, distributionDTOS);
    }
}
