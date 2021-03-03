package org.craftedsw.kirin.wedoo.distribute;

import com.github.javafaker.Faker;
import org.craftedsw.kirin.wedoo.distribute.infra.InMemoryEndowmentRepository;
import org.craftedsw.kirin.wedoo.domain.Balance;

import java.util.HashSet;
import java.util.Set;

public final class CompanyTestBuilder {

    private final Set<Endowment> endownments = new HashSet<>();
    private final Faker faker = new Faker();
    private EndowmentRepository endowmentRepository = new InMemoryEndowmentRepository();
    private Balance initialBalance = Balance.of(0);

    private CompanyTestBuilder() {
    }

    public static CompanyTestBuilder newCompany() {
        return new CompanyTestBuilder();
    }

    public CompanyTestBuilder withEndowment(long id, Balance initialBalance) {
        this.endownments.add(new Endowment(id, initialBalance));
        return this;
    }

    public CompanyTestBuilder withEndowment(long id) {
        return withEndowment(id, Balance.of(0));
    }

    public CompanyTestBuilder withBalance(Balance balance) {
        this.initialBalance = balance;
        return this;
    }

    public CompanyTestBuilder withHugeBalance() {
        return withBalance(Balance.of(Double.MAX_VALUE));
    }

    public Company build() {
        Company company = new Company(faker.number().randomNumber(2, false), faker.company().name(), initialBalance, endowmentRepository);
        endownments.forEach(company::addEndowment);
        return company;
    }
}
