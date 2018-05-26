package tech.lapsa.javax.caching;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import tech.lapsa.java.commons.function.MyStrings;

public final class CacheFactory<K, V> {

    private final CacheManager cacheManager;

    private Class<K> keyType;
    private Class<V> valueType;
    private String name;

    private CacheFactory(CacheManager cacheManager) {
	this.cacheManager = cacheManager;
    }

    public static <K, V> CacheFactory<K, V> of(CacheManager cacheManager) {
	return new CacheFactory<>(cacheManager);
    }

    public CacheFactory<K, V> withKeyClass(final Class<K> keyType) {
	this.keyType = keyType;
	return this;
    }

    public CacheFactory<K, V> withValueClass(final Class<V> valueType) {
	this.valueType = valueType;
	return this;
    }

    public CacheFactory<K, V> withName(final String name) {
	this.name = name;
	return this;
    }

    @SuppressWarnings("unchecked")
    public Cache<K, V> buildOrGet() {
	final String cacheName = MyStrings.format("%1$s-%2$s-%3$s", name, keyType.getCanonicalName(),
		valueType.getCanonicalName());
	Cache<K, V> cache = cacheManager.getCache(cacheName);
	if (cache == null) {
	    MutableConfiguration<Object, Object> config = new MutableConfiguration<Object, Object>();
	    config.setTypes(Object.class, Object.class);
	    config.setStoreByValue(true);
	    config.setStatisticsEnabled(true);
	    config.setManagementEnabled(true);
	    config.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
	    synchronized (cacheManager) {
		cache = cacheManager.getCache(cacheName);// another try in locked mode
		if (cache == null)
		    cache = (Cache<K, V>) cacheManager.createCache(cacheName, config);
	    }
	}
	return cache;
    }

}
