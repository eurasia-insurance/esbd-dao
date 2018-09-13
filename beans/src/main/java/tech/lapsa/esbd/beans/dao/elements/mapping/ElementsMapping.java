package tech.lapsa.esbd.beans.dao.elements.mapping;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

public abstract class ElementsMapping<ID extends Comparable<ID>, T extends Enum<T>> {

    private final Map<ID, T> mapping = new TreeMap<>();
    private final Set<ID> exceptions = new TreeSet<>();

    @SafeVarargs
    final void addMap(final T entity, final ID id, final ID... ids) {
	Stream.concat(Stream.of(id), Stream.of(ids))
		.forEach(x -> {
		    if (mapping.containsKey(x) || exceptions.contains(x))
			throw new RuntimeException(String.format("Already mapped ID = '%s'", x));
		    if (mapping.containsKey(x))
			throw new RuntimeException(String.format("Already has mapping for ID = '%s'", x));
		    mapping.put(x, entity);
		});
    }

    @SafeVarargs
    final void addException(final ID id, final ID... ids) {
	Stream.concat(Stream.of(id), Stream.of(ids))
		.forEach(x -> {
		    if (mapping.containsKey(x) || exceptions.contains(x))
			throw new RuntimeException(String.format("Already mapped ID = '%s'", x));
		    if (exceptions.contains(x))
			throw new RuntimeException(String.format("Already has exception for ID = '%s'", x));
		    exceptions.add(x);
		});
    }

    public final T forId(final ID id) {
	return mapping.get(id);
    }

    public final boolean isException(final ID id) {
	return exceptions.contains(id);
    }

    public final Set<ID> getAllIds() {
	return mapping.keySet();
    }
}
