package tech.lapsa.esbd.beans.dao.entities;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.DomainEntitySupplier;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStreams;
import tech.lapsa.javax.caching.CacheFactory;

@Singleton
@Startup
@LocalBean
public class CacheControlBean {

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> T getOrSupply(final Class<T> entityClazz,
	    final Integer id,
	    final DomainEntitySupplier<T> supplier) throws NotFound {
	MyObjects.requireNonNull(entityClazz, "entityClazz");
	MyObjects.requireNonNull(id, "id");
	MyObjects.requireNonNull(supplier, "supplier");
	try (Cache<Integer, T> cache = idCache(entityClazz)) {
	    {
		final T entity = cache.get(id);
		if (entity != null)
		    // returns copy of the cached entity
		    return entity.clone(entityClazz);
	    }
	    final T entity = supplier.supplyById(id);
	    // puting copy to the cache
	    putCopyToCache(entityClazz, cache, entity);
	    return entity;
	}
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> T supplyAndPut(final Class<T> entityClazz,
	    final Integer id,
	    final DomainEntitySupplier<T> supplier) throws NotFound {
	MyObjects.requireNonNull(entityClazz, "entityClazz");
	MyObjects.requireNonNull(id, "id");
	MyObjects.requireNonNull(supplier, "supplier");
	final T entity = supplier.supplyById(id);
	try (final Cache<Integer, T> cache = idCache(entityClazz)) {
	    // puting copy to the cache
	    putCopyToCache(entityClazz, cache, entity);
	}
	return entity;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> T put(final Class<T> entityClazz, final T entity) {
	MyObjects.requireNonNull(entityClazz, "entityClazz");
	MyObjects.requireNonNull(entity, "entity");
	try (final Cache<Integer, T> cache = idCache(entityClazz)) {
	    // puting copy to the cache
	    putCopyToCache(entityClazz, cache, entity);
	}
	return entity;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> List<T> put(final Class<T> entityClazz,
	    final List<T> entitiesList) {
	MyObjects.requireNonNull(entityClazz, "entityClazz");
	try (Cache<Integer, T> cache = idCache(entityClazz)) {
	    MyStreams.orEmptyOf(entitiesList)
		    // puting copy to the cache
		    .forEach(x -> putCopyToCache(entityClazz, cache, x));
	}
	return entitiesList;
    }

    // private

    private CacheManager manager;

    @PostConstruct
    public void initCacheManager() {
	manager = Caching.getCachingProvider().getCacheManager();
    }

    @PreDestroy
    public void closeCacheManager() {
	if (manager != null && !manager.isClosed())
	    manager.close();
    }

    private <T extends AEntity> Cache<Integer, T> idCache(Class<T> entityClazz) {
	assert entityClazz != null;
	return CacheFactory.<Integer, T> of(manager)
		.withKeyClass(Integer.class)
		.withValueClass(entityClazz)
		.withName("idCache")
		.buildOrGet();
    }

    private <T extends AEntity> void putCopyToCache(final Class<T> entityClazz, final Cache<Integer, T> cache,
	    final T entity) {
	assert entityClazz != null && entity.getId() != null;
	assert entity != null;
	assert cache != null;
	cache.put(entity.getId(), entity.clone(entityClazz));
    }
}
