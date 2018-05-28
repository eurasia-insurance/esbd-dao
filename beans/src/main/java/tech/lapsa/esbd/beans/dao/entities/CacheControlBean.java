package tech.lapsa.esbd.beans.dao.entities;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.FetcheDomainEntityByIdFunction;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.domain.AEntity;
import tech.lapsa.javax.caching.CacheFactory;

@Singleton
@Startup
@LocalBean
public class CachedEntitiesBean {

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> T getOrFetchById(final Class<T> entityClazz,
	    final Integer id,
	    final FetcheDomainEntityByIdFunction<Integer, T> fetcher) throws NotFound {
	final Cache<Integer, T> cache = idCache(entityClazz);
	{
	    final T entity = cache.get(id);
	    if (entity != null)
		return entity;
	}
	final T entity = fetcher.fetch(id);
	cache.put(id, entity);
	return entity;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T extends AEntity> T fetchAndPutById(final Class<T> entityClazz,
	    final Integer id,
	    final FetcheDomainEntityByIdFunction<Integer, T> fetcher) throws NotFound {
	final T entity = fetcher.fetch(id);
	final Cache<Integer, T> cache = idCache(entityClazz);
	cache.put(id, entity);
	return entity;
    }

    // private

    private CacheManager manager;

    @PostConstruct
    public void initCacheManager() {
	manager = Caching.getCachingProvider().getCacheManager();
    }

    private <T extends AEntity> Cache<Integer, T> idCache(Class<T> entityClazz) {
	return CacheFactory.<Integer, T> of(manager)
		.withKeyClass(Integer.class)
		.withValueClass(entityClazz)
		.withName("idCache")
		.buildOrGet();
    }
}
