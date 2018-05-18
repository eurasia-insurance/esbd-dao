package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AMapping<T, E> {

    private final Map<T, E> mapping = new HashMap<>();
    private final Set<T> exceptions = new HashSet<>(0);

    protected final void addMap(final E entity, final T id) {
	if (mapping.containsKey(id))
	    throw new RuntimeException(String.format("Already has mapping for ID = '%s'", id));
	mapping.put(id, entity);
    }

    protected final void addException(final T id) {
	if (exceptions.contains(id))
	    throw new RuntimeException(String.format("Already has exception for ID = '%s'", id));
	exceptions.add(id);
    }

    public final E forId(final T id) {
	return mapping.get(id);
    }

    public final boolean isException(final T id) {
	return exceptions.contains(id);
    }

    public final Set<T> getAllIds() {
	return mapping.keySet();
    }
}
