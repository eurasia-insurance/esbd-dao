package tech.lapsa.esbd.beans.dao.entities;

import static tech.lapsa.esbd.beans.dao.Util.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.ICachableEntitiesService.ICachableEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.ICachableEntitiesService.ICachableEntityServiceRemote;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStreams;

public abstract class AOndemandLoadedEntitiesService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	extends AEntitiesService<DOMAIN, ESBD>
	implements ICachableEntityServiceLocal<DOMAIN>, ICachableEntityServiceRemote<DOMAIN> {

    @FunctionalInterface
    public interface DomainEntitySupplier<DOMAIN extends AEntity> {
	DOMAIN supplyById(Integer t) throws NotFound;
    }

    @FunctionalInterface
    public interface ESBDEntityLookupFunction<T> {
	T fetchFromESBDById(Connection con, Integer id);
    }

    @FunctionalInterface
    public interface ESBDEntityStoreFunction<T> {
	T storeToESBD(Connection con, T e);
    }

    public static abstract class AOndemandComplexViaIntermediateArrayService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	    extends AOndemandLoadedEntitiesService<DOMAIN, ESBD, INTERMEDIATE_ARRAY> {

	// finals

	protected final ESBDEntityLookupFunction<INTERMEDIATE_ARRAY> lookupEsbd;

	// constructor

	protected AOndemandComplexViaIntermediateArrayService(final Class<?> serviceClazz,
		final Class<DOMAIN> domainClazz,
		final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter,
		final ESBDEntityLookupFunction<INTERMEDIATE_ARRAY> lookupEsbd,
		final ESBDEntityStoreFunction<ESBD> storeEsbd) {
	    super(serviceClazz, domainClazz, intermediateArrayToListConverter, storeEsbd);
	    assert lookupEsbd != null;
	    this.lookupEsbd = lookupEsbd;
	}

	// public

	// private & protected

	@Override
	protected final DomainEntitySupplier<DOMAIN> getEntitySupplier() {
	    return x -> singleFromIntermediateArray(con -> lookupEsbd.fetchFromESBDById(con, x));
	}
    }

    @EJB
    protected CacheControlBean cacheControl;

    public static abstract class AOndemandComplexViaSingleEntityService<DOMAIN extends AEntity, ESBD, INTERMEDIATE_ARRAY>
	    extends AOndemandLoadedEntitiesService<DOMAIN, ESBD, INTERMEDIATE_ARRAY> {

	// finals

	protected final ESBDEntityLookupFunction<ESBD> lookupEsbd;

	// constructor

	protected AOndemandComplexViaSingleEntityService(final Class<?> serviceClazz,
		final Class<DOMAIN> domainClazz,
		final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter,
		final ESBDEntityLookupFunction<ESBD> lookupEsbd,
		final ESBDEntityStoreFunction<ESBD> storeEsbd) {
	    super(serviceClazz, domainClazz, intermediateArrayToListConverter, storeEsbd);
	    assert lookupEsbd != null;
	    this.lookupEsbd = lookupEsbd;
	}

	// public

	// private & protected

	@Override
	protected DomainEntitySupplier<DOMAIN> getEntitySupplier() {
	    return x -> singleFromSingle(con -> lookupEsbd.fetchFromESBDById(con, x));
	}
    }

    // finals

    protected final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter;

    protected final ESBDEntityStoreFunction<ESBD> storeEsbd;

    // constructor

    private AOndemandLoadedEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<INTERMEDIATE_ARRAY, List<ESBD>> intermediateArrayToListConverter,
	    final ESBDEntityStoreFunction<ESBD> storeEsbd) {
	super(serviceClazz, domainClazz);
	assert intermediateArrayToListConverter != null;
	this.intermediateArrayToListConverter = intermediateArrayToListConverter;
	assert storeEsbd != null;
	this.storeEsbd = storeEsbd;
    }

    // public

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DOMAIN getById(final Integer id) throws NotFound, IllegalArgument {
	MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	return cacheControl.getOrSupply(domainClazz, id, getEntitySupplier());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DOMAIN getByIdBypassCache(final Integer id) throws NotFound, IllegalArgument {
	MyNumbers.requireNonZero(IllegalArgument::new, id, "id");
	return cacheControl.supplyAndPut(domainClazz, id, getEntitySupplier());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DOMAIN save(final DOMAIN source) throws IllegalArgument {
	return _save(source);
    }

    // private & protected

    private DOMAIN _save(final DOMAIN source) throws IllegalArgument {
	MyObjects.requireNonNull(IllegalArgument::new, source, "source");
	if (source.getId() != null)
	    // в "нашей" сущности задан ID, поэтому нужно сравнить "нашу"
	    // сущность с "базовой"
	    try {
		final DOMAIN fetched = getByIdBypassCache(source.getId());
		if (source.equals(fetched)) {
		    // если "базовая" не отличается от "нашей" то
		    // пересохранения в базу не делать
		    return fetched;
		}
	    } catch (Exception e) {
		// в БД нет такой сущности
	    }

	final ESBD esbdSource = conversion(source);

	final ESBD esbdTarget;
	try (Connection con = pool.getConnection()) {
	    esbdTarget = storeEsbd.storeToESBD(con, esbdSource);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	final DOMAIN target = conversion(esbdTarget);
	cacheControl.put(domainClazz, target);
	return target;
    }

    protected abstract DomainEntitySupplier<DOMAIN> getEntitySupplier();

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

    protected DOMAIN singleFromStream(final Function<Connection, Stream<ESBD>> criteriaFunction)
	    throws IllegalStateException, NotFound {
	assert criteriaFunction != null;
	try {
	    final Stream<ESBD> stream;
	    try (Connection con = pool.getConnection()) {
		stream = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(stream, s -> s.collect(Collectors.toList()));
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

    protected DOMAIN firstFromStream(final Function<Connection, Stream<ESBD>> criteriaFunction)
	    throws NotFound {
	assert criteriaFunction != null;
	try {
	    final Stream<ESBD> stream;
	    try (Connection con = pool.getConnection()) {
		stream = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    final List<ESBD> list = MyObjects.nullOrGet(stream, s -> s.collect(Collectors.toList()));
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

    protected List<DOMAIN> manyFromStream(final Function<Connection, Stream<ESBD>> criteriaFunction) {
	assert criteriaFunction != null;
	try {
	    final Stream<ESBD> stream;
	    try (Connection con = pool.getConnection()) {
		stream = criteriaFunction.apply(con);
	    } catch (ConnectionException e) {
		throw new IllegalStateException(e.getMessage());
	    }
	    return MyOptionals.of(stream)
		    .orElseGet(Stream::empty)
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
