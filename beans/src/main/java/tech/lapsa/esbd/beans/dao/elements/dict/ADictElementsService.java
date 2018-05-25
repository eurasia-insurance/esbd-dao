package tech.lapsa.esbd.beans.dao.elements.dict;

import java.util.function.Function;

import tech.lapsa.esbd.beans.dao.elements.AElementsService;
import tech.lapsa.esbd.beans.dao.elements.mapping.AMapping;
import tech.lapsa.esbd.dao.elements.dict.IDictElementsService;

public abstract class ADictElementsService<T extends Enum<T>>
	extends AElementsService<T>
	implements IDictElementsService<T> {

    // constructor

    protected ADictElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final Function<Integer, T> mapperFunction) {
	super(serviceClazz, entityClazz, mapperFunction);
    }

    protected ADictElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final AMapping<Integer, T> mapper) {
	super(serviceClazz, entityClazz, mapper);
    }
}
