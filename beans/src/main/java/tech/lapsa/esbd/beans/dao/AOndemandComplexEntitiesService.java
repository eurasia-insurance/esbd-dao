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

public abstract class AOndemandComplexEntitiesService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    public static abstract class AOndemandComplexIdByIntermediateService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	    extends AOndemandComplexEntitiesService<DOMAIN, ESBD, INTERMEDIATE_ARRAY>
	    implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

	// finals

	protected final BiFunction<Connection, Integer, INTERMEDIATE_ARRAY> getIntermediateArrayById;

	// constructor

	protected AOndemandComplexIdByIntermediateService(final Class<?> serviceClazz,
		final Class<DOMAIN> domainClazz,
		final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter,
		final BiFunction<Connection, Integer, INTERMEDIATE_ARRAY> getIntermediateArrayById) {
	    super(serviceClazz, domainClazz, intermediateArrayToListConverter);
	    assert getIntermediateArrayById != null;
	    this.getIntermediateArrayById = getIntermediateArrayById;
	}

	// public

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	    MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	    return singleFromIntermediateArray(con -> getIntermediateArrayById.apply(con, id));
	}

    }

    public static abstract class AOndemandComplexIdBySingleService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	    extends AOndemandComplexEntitiesService<DOMAIN, ESBD, INTERMEDIATE_ARRAY>
	    implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

	// finals

	protected final BiFunction<Connection, Integer, ESBD> getSingleById;

	// constructor

	protected AOndemandComplexIdBySingleService(final Class<?> serviceClazz,
		final Class<DOMAIN> domainClazz,
		final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter,
		final BiFunction<Connection, Integer, ESBD> getSingleById) {
	    super(serviceClazz, domainClazz, intermediateArrayToListConverter);
	    assert getSingleById != null;
	    this.getSingleById = getSingleById;
	}

	// public

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	    MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	    return singleFromSingle(con -> getSingleById.apply(con, id));
	}

    }

    // finals

    protected final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter;

    // constructor

    private AOndemandComplexEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter) {
	super(serviceClazz, domainClazz);
	assert intermediateArrayToListConverter != null;
	this.intermediateArrayToListConverter = intermediateArrayToListConverter;
    }

    // private & protected

    // signle

    protected DOMAIN singleFromIntermediateArray(final Function<Connection, INTERMEDIATE_ARRAY> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	try {
	    final INTERMEDIATE_ARRAY intermediateArray;
	    try (Connection con = pool.getConnection()) {
		intermediateArray = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(intermediateArray, intermediateArrayToListConverter::apply);
	    final ESBD source = requireSingle(list, domainClazz);
	    return conversion(source);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}

    }

    protected DOMAIN singleFromList(final Function<Connection, List<ESBD>> criteriaFunction)
	    throws IllegalStateException, NotFound {
	assert criteriaFunction != null;
	try {
	    final List<ESBD> list;
	    try (Connection con = pool.getConnection()) {
		list = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final ESBD source = requireSingle(list, domainClazz);
	    return conversion(source);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    protected DOMAIN singleFromSingle(final Function<Connection, ESBD> criteriaFunction)
	    throws IllegalStateException, NotFound {
	assert criteriaFunction != null;
	try {
	    final ESBD single;
	    try (Connection con = pool.getConnection()) {
		single = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(single, Arrays::asList);
	    final ESBD source = requireSingle(list, domainClazz);
	    return conversion(source);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // first

    protected DOMAIN firstFromIntermediateArray(final Function<Connection, INTERMEDIATE_ARRAY> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	try {
	    final INTERMEDIATE_ARRAY intermediateArray;
	    try (Connection con = pool.getConnection()) {
		intermediateArray = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(intermediateArray, intermediateArrayToListConverter::apply);
	    final ESBD source = requireFirst(list, domainClazz);
	    return conversion(source);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    protected DOMAIN firstFromList(final Function<Connection, List<ESBD>> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	try {
	    final List<ESBD> list;
	    try (Connection con = pool.getConnection()) {
		list = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final ESBD source = requireFirst(list, domainClazz);
	    return conversion(source);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // many

    protected List<DOMAIN> manyFromIntermediateArray(final Function<Connection, INTERMEDIATE_ARRAY> criteriaFunction) {
	assert criteriaFunction != null;
	try {
	    final INTERMEDIATE_ARRAY intermediateArray;
	    try (Connection con = pool.getConnection()) {
		intermediateArray = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(intermediateArray, intermediateArrayToListConverter::apply);
	    return MyStreams.orEmptyOf(list)
		    .map(this::conversion)
		    .collect(MyCollectors.unmodifiableList());
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    protected List<DOMAIN> manyFromList(final Function<Connection, List<ESBD>> criteriaFunction) {
	assert criteriaFunction != null;
	try {
	    final List<ESBD> list;
	    try (Connection con = pool.getConnection()) {
		list = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    return MyStreams.orEmptyOf(list)
		    .map(this::conversion)
		    .collect(MyCollectors.unmodifiableList());
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }
}
