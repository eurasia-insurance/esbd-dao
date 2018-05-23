package tech.lapsa.esbd.beans.dao;

import java.util.List;
import java.util.function.BiFunction;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.Util;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceLocal;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceRemote;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;

public abstract class AOndemandComplexEntitiesService<DOMAIN extends AEntity, ESBD>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    // finals

    protected final BiFunction<Connection, Integer, List<ESBD>> getByIdFunction;

    // constructor

    protected AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final BiFunction<Connection, Integer, List<ESBD>> getByIdFunction) {
	super(serviceClazz, domainClazz);
	assert getByIdFunction != null;
	this.getByIdFunction = getByIdFunction;
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

	final ESBD source;
	final List<ESBD> list;

	try (Connection con = pool.getConnection()) {
	    list = getByIdFunction.apply(con, id);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	MyOptionals.of(list)
		.filter(MyCollections::nonEmpty)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with ID = '%2$s'",
			domainClazz.getSimpleName(), id))
		.stream()
		.findFirst()
		.get();

	source = Util.requireSingle(list, domainClazz, "ID", id);

	return conversion(source);
    }
}
