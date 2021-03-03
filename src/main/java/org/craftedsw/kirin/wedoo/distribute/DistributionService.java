package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.distribute.api.loader.JSONLoader;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryCompanyRepository;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryEndowmentRepository;
import org.craftedsw.kirin.wedoo.distribute.infra.exporter.JSONExporter;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DistributionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributionService.class);

    private final EndowmentRepository endowmentRepository;
    private final CompanyRepository companyRepository;


    public DistributionService(EndowmentRepository endowmentRepository, CompanyRepository companyRepository) {
        this.endowmentRepository = endowmentRepository;
        this.companyRepository = companyRepository;
    }

    public static void main(String[] args) throws IOException {
        EndowmentRepository endowmentRepository = new InMemoryEndowmentRepository();
        CompanyRepository companyRepository = new InMemoryCompanyRepository();

        DistributionService service = new DistributionService(endowmentRepository, companyRepository);
        JSONLoader loader = new JSONLoader();
        service.load(loader, DistributionService.class.getResourceAsStream("/Level1/data/input.json"));

        service.distribute(Distribution.newDistribution(Amount.of(50), LocalDate.of(2020, 9, 16)), "Wedoogift", 1);
        service.distribute(Distribution.newDistribution(Amount.of(100), LocalDate.of(2020, 8, 1)), "Wedoogift", 2);
        service.distribute(Distribution.newDistribution(Amount.of(1000), LocalDate.of(2020, 5, 1)), "Wedoofood", 3);

        JSONExporter exporter = new JSONExporter();
        service.export(exporter, Paths.get("exports/output-level1.json"));


    }

    public void load(Loader loader, InputStream inputStream) throws IOException {
        loader.load(inputStream, endowmentRepository, companyRepository);
    }

    public Optional<Company> getCompany(String name) {
        return companyRepository.getCompany(name);
    }

    public void distribute(Distribution distribution, String companyName, long endowmentId) {
        LOGGER.info("Distribute {} from company {} to endowment {} ", distribution, companyName, endowmentId);
        Company company = getCompany(companyName)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Company with name %s not found", companyName)));

        Endowment endowment = endowmentRepository.getEndowment(endowmentId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Endowment with id %s not found", endowmentId)));

        company.distribute(endowment, distribution);
    }

    public void export(Exporter exporter, Path path) throws IOException {
        List<Company> allCompanies = companyRepository.getAll();
        List<Distribution> distributions = allCompanies.stream()
                .flatMap(company -> company.getDistributions().stream())
                .collect(Collectors.toUnmodifiableList());

        exporter.export(new FileWriter(path.toFile()),
                allCompanies,
                endowmentRepository.getAll(),
                distributions);
    }


}
