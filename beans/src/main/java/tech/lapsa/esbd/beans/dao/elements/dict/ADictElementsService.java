package tech.lapsa.esbd.beans.dao.elements.dict;

import java.util.function.Function;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.logging.MyLogger;

public abstract class ADictElementsService<T extends Enum<T>> implements ElementsService<T> {

    private final MyLogger logger;
    private final Function<Integer, T> converter;
    private final Class<T> entityClazz;

    ADictElementsService(final Class<?> serviceClazz, final Function<Integer, T> converter,
	    final Class<T> entityClazz) {
	this.logger = MyLogger.newBuilder() //
		.withNameOf(MyObjects.requireNonNull(serviceClazz, "serviceClazz")) //
		.build();
	this.entityClazz = MyObjects.requireNonNull(entityClazz, "entityClazz");
	this.converter = MyObjects.requireNonNull(converter, "converter");
    }

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

    // PRIVATE

    private T _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	return MyOptionals.of(converter.apply(id)) //
		.orElseThrow(
			MyExceptions.supplier(NotFound::new, "%1$s(%2$s) not found", entityClazz.getSimpleName(), id));
    }
}
