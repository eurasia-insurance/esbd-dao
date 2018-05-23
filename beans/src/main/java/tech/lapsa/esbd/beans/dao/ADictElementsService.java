package tech.lapsa.esbd.beans.dao;

import java.util.function.Function;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.AMapping;
import tech.lapsa.esbd.dao.IElementsService;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;

public abstract class ADictElementsService<T extends Enum<T>>
	extends AService<T>
	implements IElementsService<T> {

    // finals

    protected final Function<Integer, T> mapperFunction;

    // constructor

    protected ADictElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final Function<Integer, T> mapperFunction) {
	super(serviceClazz, entityClazz);
	assert mapperFunction != null;
	this.mapperFunction = mapperFunction;
    }

    protected ADictElementsService(final Class<?> serviceClazz,
	    final Class<T> entityClazz,
	    final AMapping<Integer, T> mapper) {
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
	MyNumbers.requireNonZero(id, "id");
	return MyOptionals.of(mapperFunction.apply(id)) //
		.orElseThrow(
			MyExceptions.supplier(NotFound::new, "%1$s(%2$s) not found", domainClazz.getSimpleName(), id));
    }
}
