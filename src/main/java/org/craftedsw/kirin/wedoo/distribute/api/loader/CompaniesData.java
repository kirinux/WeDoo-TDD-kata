package org.craftedsw.kirin.wedoo.distribute.api.loader;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.craftedsw.kirin.wedoo.distribute.*;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"companies", "endowments", "distributions"})
@Getter
public class CompaniesData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompaniesData.class);

    private final List<CompanyDTO> companies;
    @JsonProperty("users")
    private final List<EndowmentDTO> endowments;
    private final List<DistributionDTO> distributions;
    private final List<WalletDTO> wallets;

    @JsonCreator
    public CompaniesData(@JsonProperty("companies") List<CompanyDTO> companies,
                         @JsonProperty("users") List<EndowmentDTO> endowments,
                         @JsonProperty("distributions") List<DistributionDTO> distributions,
                         @JsonProperty("wallets") List<WalletDTO> wallets) {
        this.companies = companies;
        this.endowments = endowments;
        this.distributions = distributions;
        this.wallets = wallets;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CompanyDTO {
        private long id;
        private String name;
        private double balance;

        public static CompanyDTO fromCompany(Company company) {
            return new CompanyDTO(company.getId(), company.getName(), company.getInitialBalance().getValue());
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
        @JsonProperty("balance")
        private List<BalanceDTO> balances;

        public static EndowmentDTO fromEndowment(Endowment endowment, WalletRepository walletRepository) {
            List<BalanceDTO> balanceDTOS = endowment.getBalances().getAll().stream()
                    .filter(balance -> balance.getValue() > 0)
                    .map((WalletBalance walletBalance) -> BalanceDTO.fromBalance(walletBalance, walletRepository))
                    .collect(Collectors.toUnmodifiableList());
            return new EndowmentDTO(endowment.getId(), balanceDTOS);
        }

        public Endowment toEndowment(WalletRepository walletRepository) {
            Map<Wallet.Type, WalletBalance> typeBalanceMap = balances.stream()
                    .map((BalanceDTO balanceDTO) -> balanceDTO.toBalance(walletRepository))
                    .collect(Collectors.toMap(WalletBalance::getType, balance -> balance));

            return new Endowment(id, Balances.initialBalances(typeBalanceMap));
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BalanceDTO {
        @JsonProperty("wallet_id")
        private long id;
        @JsonProperty("amount")
        private double value;

        public static BalanceDTO fromBalance(WalletBalance walletBalance, WalletRepository walletRepository) {
            Wallet wallet = walletRepository.getWallet(walletBalance.getType()).orElseThrow(() -> new UnknownWalletType("Wallet not found with type " + walletBalance.getType()));
            return new BalanceDTO(wallet.getId(), walletBalance.getValue());
        }

        public WalletBalance toBalance(WalletRepository walletRepository) {
            Wallet wallet = walletRepository.getWallet(id).orElseThrow(() -> new UnknownWalletType("Wallet not found with id" + id));
            return WalletBalance.forType(value, wallet.getType());
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
        @JsonProperty("wallet_id")
        private long walletId;

        public static DistributionDTO fromDistribution(Distribution distribution, WalletRepository walletRepository) {
            Wallet wallet = walletRepository.getWallet(distribution.getType()).orElseThrow(() -> new UnknownWalletType("Wallet not found with type " + distribution.getType()));
            return new DistributionDTO(distribution.getId(),
                    distribution.getAmount().getValue(),
                    distribution.getStartDate(),
                    distribution.getEndDate(),
                    distribution.getCompanyId().orElseThrow(),
                    distribution.getEndowmentId().orElseThrow(),
                    wallet.getId());
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WalletDTO {
        private long id;
        private String name;
        private String type;

        public Wallet toWallet() {
            try {
                Wallet.Type type = Wallet.Type.valueOf(this.type);
                return new Wallet(id, name, type);
            } catch (IllegalArgumentException exception) {
                throw new UnknownWalletType(String.format("Input wallet type not found %s", type));
            }
        }
    }
}
