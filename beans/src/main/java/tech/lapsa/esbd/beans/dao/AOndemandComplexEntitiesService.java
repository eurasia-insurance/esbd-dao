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

public abstract class AOndemandComplexEntitiesService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY_TYPE>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    // finals

    protected final Function<INTERMEDIATE_ARRAY_TYPE, List<ESBD>> intermediateArrayToListConverter;
    protected final boolean isGetByIdAsIntermediateArray;

    protected final BiFunction<Connection, Integer, INTERMEDIATE_ARRAY_TYPE> getIntermediateArrayById;
    protected final BiFunction<Connection, Integer, ESBD> getSingleById;

    // constructor

    private AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<INTERMEDIATE_ARRAY_TYPE, List<ESBD>> intermediateArrayToListConverter,
	    boolean isGetByIdAsIntermediateArray,
	    final BiFunction<Connection, Integer, INTERMEDIATE_ARRAY_TYPE> getIntermediateArrayById,
	    final BiFunction<Connection, Integer, ESBD> getSingleById) {
	super(serviceClazz, domainClazz);

	assert intermediateArrayToListConverter != null;
	this.intermediateArrayToListConverter = intermediateArrayToListConverter;

	if (isGetByIdAsIntermediateArray)
	    assert getIntermediateArrayById != null;
	else
	    assert getSingleById != null;

	this.isGetByIdAsIntermediateArray = isGetByIdAsIntermediateArray;
	this.getIntermediateArrayById = getIntermediateArrayById;
	this.getSingleById = getSingleById;
    }

    protected AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final BiFunction<Connection, Integer, ESBD> getSingleById,
	    final Function<INTERMEDIATE_ARRAY_TYPE, List<ESBD>> intermediateArrayToListConverter) {
	this(serviceClazz, domainClazz, intermediateArrayToListConverter, false, null, getSingleById);
    }

    protected AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<INTERMEDIATE_ARRAY_TYPE, List<ESBD>> intermediateArrayToListConverter,
	    final BiFunction<Connection, Integer, INTERMEDIATE_ARRAY_TYPE> getIntermediateArrayById) {
	this(serviceClazz, domainClazz, intermediateArrayToListConverter, true, getIntermediateArrayById, null);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	try {
	    if (isGetByIdAsIntermediateArray)
		return getOneFromIntermediateList(con -> getIntermediateArrayById.apply(con, id));
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

    protected DOMAIN getOneFromIntermediateList(final Function<Connection, INTERMEDIATE_ARRAY_TYPE> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	final INTERMEDIATE_ARRAY_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(intermediate, intermediateArrayToListConverter::apply);
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

    protected List<DOMAIN> getManyFromIntermediate(
	    final Function<Connection, INTERMEDIATE_ARRAY_TYPE> criteriaFunction) {
	assert criteriaFunction != null;
	final INTERMEDIATE_ARRAY_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(intermediate, intermediateArrayToListConverter::apply);
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

    protected DOMAIN getFirstFromIntermediate(final Function<Connection, INTERMEDIATE_ARRAY_TYPE> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	final INTERMEDIATE_ARRAY_TYPE intermediate;
	try (Connection con = pool.getConnection()) {
	    intermediate = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<ESBD> list = MyObjects.nullOrGet(intermediate, intermediateArrayToListConverter::apply);
	return MyStreams.orEmptyOf(list)
		.findFirst()
		.map(this::conversion)
		.orElseThrow(NotFound::new);
    }

    protected DOMAIN getFirstFromList(final Function<Connection, List<ESBD>> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	final List<ESBD> list;
	try (Connection con = pool.getConnection()) {
	    list = criteriaFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyStreams.orEmptyOf(list)
		.findFirst()
		.map(this::conversion)
		.orElseThrow(NotFound::new);
    }
}
