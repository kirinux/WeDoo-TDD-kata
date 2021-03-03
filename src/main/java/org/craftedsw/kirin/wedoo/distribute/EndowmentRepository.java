package org.craftedsw.kirin.wedoo.distribute;

import java.util.List;
import java.util.Optional;

public interface EndowmentRepository {

    Optional<Endowment> getEndowment(long endowmentId);

    void addEndowment(Endowment endowment);

    boolean endowmentExists(long endowmentId);

    List<Endowment> getAll(); //could be problematic with huge list
}
