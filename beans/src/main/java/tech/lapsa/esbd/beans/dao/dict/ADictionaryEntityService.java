package tech.lapsa.esbd.beans.dao.dict;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.dict.DictionaryEntityService;
import tech.lapsa.esbd.domain.dict.DictionaryEntity;
import tech.lapsa.esbd.domain.dict.DictionaryEntity.DictionaryEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

public abstract class ADictionaryEntityService<T extends DictionaryEntity>
	implements DictionaryEntityService<T> {

    private final MyLogger logger;
    private final String dictionaryName;
    private final Supplier<DictionaryEntityBuilder<T>> newBuilderSupplier;

    protected ADictionaryEntityService(final Class<?> serviceClazz,
	    final String dictionaryName,
	    final Supplier<DictionaryEntityBuilder<T>> newBuilderSupplier) {
	this.logger = MyLogger.newBuilder() //
		.withNameOf(MyObjects.requireNonNull(serviceClazz, "serviceClazz")) //
		.build();
	this.dictionaryName = MyStrings.requireNonEmpty(dictionaryName, "dictionaryName");
	this.newBuilderSupplier = MyObjects.requireNonNull(newBuilderSupplier, "newBuilderSupplier");
    }

    @EJB
    private ConnectionPool pool;

    private Map<Integer, T> allMap;

    @PostConstruct
    public void loadDictionary() {
	final ArrayOfItem items;
	try (Connection con = pool.getConnection()) {
	    items = con.getItems(dictionaryName);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	this.allMap = MyOptionals.of(items) //
		.map(ArrayOfItem::getItem) //
		.map(List::stream) //
		.orElseGet(Stream::empty) //
		.map(this::convert) //
		.collect(MyCollectors.unmodifiableMap(DictionaryEntity::getId, Function.identity()));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> getAll() {
	try {
	    return allMap.entrySet() //
		    .stream() //
		    .map(Map.Entry::getValue) //
		    .collect(MyCollectors.unmodifiableList());
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T getById(final Integer id) throws IllegalArgument, NotFound {
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
	final T res = allMap.get(id);
	if (res == null)
	    throw new NotFound(String.format("Dictionary entity with id = '%1$s' is not found", id));
	return res;
    }

    protected T convert(final Item source) {
	return newBuilderSupplier.get()
		.withId(MyOptionals.of(source.getID()).orElse(null))
		.withCode(source.getCode())
		.withName(source.getName())
		.build();
    }

}
