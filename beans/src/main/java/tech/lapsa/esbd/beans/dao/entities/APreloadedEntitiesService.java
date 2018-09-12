package tech.lapsa.esbd.beans.dao.entities;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.IPreloadedEntitiesService.IPreloadedEntitiesServiceLocal;
import tech.lapsa.esbd.dao.entities.IPreloadedEntitiesService.IPreloadedEntitiesServiceRemote;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;

public abstract class APreloadedEntitiesService<DOMAIN extends AEntity, ESBD>
	extends AEntitiesService<DOMAIN, ESBD>
	implements IPreloadedEntitiesServiceLocal<DOMAIN>, IPreloadedEntitiesServiceRemote<DOMAIN> {

    // finals

    protected final Function<Connection, List<ESBD>> getAllFunction;
    protected final Function<ESBD, Integer> entityToIdFunction;

    // constructor

    protected APreloadedEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz,
	    final Function<Connection, List<ESBD>> getAllFunction,
	    final Function<ESBD, Integer> entityToIdFunction) {
	super(serviceClazz, domainClazz);
	assert getAllFunction != null;
	assert entityToIdFunction != null;
	this.getAllFunction = getAllFunction;
	this.entityToIdFunction = entityToIdFunction;
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<DOMAIN> getAll() {
	try {
	    return _getAll();
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DOMAIN save(DOMAIN entity) throws IllegalArgument {
	try {
	    return _save(entity);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @PostConstruct
    @Schedule(dayOfWeek = "*")
    public void reload() {
	_reload();
    }

    // private

    private class Holder {
	private final ESBD esbd;
	private DOMAIN fetched;

	private Holder(final ESBD esbd) {
	    this.esbd = esbd;
	}
    }

    private Map<Integer, Holder> all;

    private void _reload() {
	final List<ESBD> list;

	try (Connection con = pool.getConnection()) {
	    list = getAllFunction.apply(con);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	all = MyOptionals.of(list) //
		.map(List::stream) //
		.orElseGet(Stream::empty) //
		.collect(MyCollectors.unmodifiableMap(entityToIdFunction, Holder::new));
    }

    private DOMAIN _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final Holder holder = all.get(id);
	if (holder == null)
	    throw MyExceptions.format(NotFound::new, "%1$s(%2$s) not found", domainClazz.getSimpleName(), id);
	synchronized (holder) {
	    if (holder.fetched == null)
		holder.fetched = conversion(holder.esbd);
	}
	return holder.fetched;
    }

    private List<DOMAIN> _getAll() {
	try {
	    return all.entrySet()
		    .stream()
		    .map(Map.Entry::getValue)
		    .filter(MyObjects::nonNull)
		    .map(holder -> {
			synchronized (holder) {
			    if (holder.fetched == null)
				holder.fetched = conversion(holder.esbd);
			}
			return holder.fetched;
		    })
		    .collect(MyCollectors.unmodifiableList());
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }
}
