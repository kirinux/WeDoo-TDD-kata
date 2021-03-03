package org.craftedsw.kirin.wedoo.distribute.api.loader;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.craftedsw.kirin.wedoo.distribute.Company;
import org.craftedsw.kirin.wedoo.distribute.Distribution;
import org.craftedsw.kirin.wedoo.distribute.Endowment;
import org.craftedsw.kirin.wedoo.distribute.EndowmentRepository;
import org.craftedsw.kirin.wedoo.domain.Balance;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"companies", "endowments", "distributions"})
@Getter
public class CompaniesData {
    private final List<CompanyDTO> companies;
    @JsonProperty("users")
    private final List<EndowmentDTO> endowments;
    private final List<DistributionDTO> distributions;

    @JsonCreator
    public CompaniesData(@JsonProperty("companies") List<CompanyDTO> companies,
                         @JsonProperty("users") List<EndowmentDTO> endowments,
                         @JsonProperty("distributions") List<DistributionDTO> distributions) {
        this.companies = companies;
        this.endowments = endowments;
        this.distributions = distributions;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CompanyDTO {
        private long id;
        private String name;
        private double balance;

        public static CompanyDTO fromCompany(Company company) {
            return new CompanyDTO(company.getId(), company.getName(), company.getBalance().getValue());
        }

        public Company toCompany(EndowmentRepository endowmentRepository) {
            return new Company(id, name, Balance.of(balance), endowmentRepository);
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EndowmentDTO {
        private long id;
        private double balance;

        public static EndowmentDTO fromEndowment(Endowment endowment) {
            return new EndowmentDTO(endowment.getId(), endowment.getBalance().getValue());
        }

        public Endowment toEndowment() {
            return new Endowment(id, Balance.of(balance));
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DistributionDTO {
        private long id;
        private double amount;
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd")
        private LocalDate startDate;
        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd")
        private LocalDate endDate;
        @JsonProperty("company_id")
        private long companyId;
        @JsonProperty("user_id")
        private long endowmentId;

        public static DistributionDTO fromDistribution(Distribution distribution) {
            return new DistributionDTO(distribution.getId(),
                    distribution.getAmount().getValue(),
                    distribution.getStartDate(),
                    distribution.getEndDate(),
                    distribution.getCompanyId().orElseThrow(),
                    distribution.getEndowmentId().orElseThrow());
        }
    }
}
