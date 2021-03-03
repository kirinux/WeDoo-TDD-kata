package org.craftedsw.kirin.wedoo.distribute.infra;

import org.craftedsw.kirin.wedoo.distribute.Endowment;
import org.craftedsw.kirin.wedoo.distribute.EndowmentAlreadyExistsException;
import org.craftedsw.kirin.wedoo.distribute.EndowmentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryEndowmentRepository implements EndowmentRepository {

    private final Map<Long, Endowment> endowments = new HashMap<>();

    @Override
    public Optional<Endowment> getEndowment(long endowmentId) {
        return Optional.ofNullable(endowments.get(endowmentId));
    }

    @Override
    public void addEndowment(Endowment endowment) {
        if (endowmentExists(endowment.getId())) {
            throw new EndowmentAlreadyExistsException(String.format("Endowment with id %s already exists", endowment.getId()));
        }
        endowments.put(endowment.getId(), endowment);
    }

    @Override
    public boolean endowmentExists(long endowmentId) {
        return endowments.containsKey(endowmentId);
    }

    @Override
    public List<Endowment> getAll() {
        return endowments.values().stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
