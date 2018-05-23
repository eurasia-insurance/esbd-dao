package tech.lapsa.esbd.beans.dao;

import static tech.lapsa.esbd.beans.dao.Util.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceLocal;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceRemote;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyNumbers;

public abstract class AOndemandComplexEntitiesService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_TYPE>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    // finals

    protected final BiFunction<Connection, Integer, INTERMEDIATE_TYPE> getByIdFunction;
    protected final Function<INTERMEDIATE_TYPE, List<ESBD>> getListFunction;

    // constructor

    protected AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final BiFunction<Connection, Integer, INTERMEDIATE_TYPE> getByIdFunction,
	    final Function<INTERMEDIATE_TYPE, List<ESBD>> getListFunction) {
	super(serviceClazz, domainClazz);
	assert getByIdFunction != null;
	assert getListFunction != null;
	this.getByIdFunction = getByIdFunction;
	this.getListFunction = getListFunction;
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    return _getById(id);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // private

    private DOMAIN _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");

	final INTERMEDIATE_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = getByIdFunction.apply(con, id);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	final List<ESBD> list = getListFunction.apply(intermediate);

	final ESBD source = requireSingle(list, domainClazz, "ID", id);

	return conversion(source);
    }
}
