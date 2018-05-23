package tech.lapsa.esbd.beans.dao;

import static tech.lapsa.esbd.beans.dao.Util.*;

import java.util.Arrays;
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
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStreams;

public abstract class AOndemandComplexEntitiesService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_TYPE>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    // finals

    protected final Function<INTERMEDIATE_TYPE, List<ESBD>> intermediateToListConverter;
    protected final boolean isIdByIntermediate;

    protected final BiFunction<Connection, Integer, INTERMEDIATE_TYPE> getIntermediateById;
    protected final BiFunction<Connection, Integer, ESBD> getSingleById;

    // constructor

    protected AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<INTERMEDIATE_TYPE, List<ESBD>> intermediateToListConverter,
	    boolean isIdByIntermediate,
	    final BiFunction<Connection, Integer, INTERMEDIATE_TYPE> getIntermediateById,
	    final BiFunction<Connection, Integer, ESBD> getSingleById) {
	super(serviceClazz, domainClazz);

	assert intermediateToListConverter != null;
	this.intermediateToListConverter = intermediateToListConverter;

	if (isIdByIntermediate)
	    assert getIntermediateById != null;
	else
	    assert getSingleById != null;

	this.isIdByIntermediate = isIdByIntermediate;
	this.getIntermediateById = getIntermediateById;
	this.getSingleById = getSingleById;
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	    if (isIdByIntermediate)
		return getOneFromIntermediate(con -> getIntermediateById.apply(con, id));
	    else
		return getOneFromSingle(con -> getSingleById.apply(con, id));
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // private & protected

    protected DOMAIN getOneFromIntermediate(final Function<Connection, INTERMEDIATE_TYPE> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	final INTERMEDIATE_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(intermediate, intermediateToListConverter::apply);
	final ESBD source = requireSingle(list, domainClazz);
	return conversion(source);
    }

    protected DOMAIN getOneFromList(final Function<Connection, List<ESBD>> criteriaFunction)
	    throws IllegalStateException, NotFound {
	assert criteriaFunction != null;
	final List<ESBD> list;
	try (Connection con = pool.getConnection()) {
	    list = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final ESBD source = requireSingle(list, domainClazz);
	return conversion(source);
    }

    protected DOMAIN getOneFromSingle(final Function<Connection, ESBD> criteriaFunction)
	    throws IllegalStateException, NotFound {
	assert criteriaFunction != null;
	final ESBD single;
	try (Connection con = pool.getConnection()) {
	    single = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(single, Arrays::asList);
	final ESBD source = requireSingle(list, domainClazz);
	return conversion(source);
    }

    protected List<DOMAIN> getManyFromIntermediate(final Function<Connection, INTERMEDIATE_TYPE> criteriaFunction) {
	assert criteriaFunction != null;
	final INTERMEDIATE_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(intermediate, intermediateToListConverter::apply);
	return MyStreams.orEmptyOf(list)
		.map(this::conversion)
		.collect(MyCollectors.unmodifiableList());
    }

    protected List<DOMAIN> getManyFromList(final Function<Connection, List<ESBD>> criteriaFunction) {
	assert criteriaFunction != null;
	final List<ESBD> list;
	try (Connection con = pool.getConnection()) {
	    list = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyStreams.orEmptyOf(list)
		.map(this::conversion)
		.collect(MyCollectors.unmodifiableList());
    }
}
