package tech.lapsa.esbd.beans.dao.elements;

import java.util.function.Function;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AService;
import tech.lapsa.esbd.beans.dao.elements.mapping.AElementsMapping;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.IElementsService;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;

public abstract class AElementsService<T extends Enum<T>>
	extends AService<T>
	implements IElementsService<T> {

    // finals

    protected final Function<Integer, T> mapperFunction;

    // constructor

    protected AElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final Function<Integer, T> mapperFunction) {
	super(serviceClazz, entityClazz);
	assert mapperFunction != null;
	this.mapperFunction = mapperFunction;
    }

    protected AElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final AElementsMapping<Integer, T> mapper) {
	super(serviceClazz, entityClazz);
	assert mapper != null;
	this.mapperFunction = mapper::forId;
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    return _getById(id);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // private

    private T _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyObjects.requireNonNull(id, "id");
	return MyOptionals.of(mapperFunction.apply(id)) //
		.orElseThrow(
			MyExceptions.supplier(NotFound::new, "%1$s(%2$s) not found", domainClazz.getSimpleName(), id));
    }
}
