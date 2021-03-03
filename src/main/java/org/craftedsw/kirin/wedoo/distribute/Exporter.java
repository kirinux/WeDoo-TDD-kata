package org.craftedsw.kirin.wedoo.distribute;

import java.io.Writer;
import java.util.List;

public interface Exporter {

    void export(Writer writer, List<Company> companies, List<Endowment> endowments, List<Distribution> distributions);
}
