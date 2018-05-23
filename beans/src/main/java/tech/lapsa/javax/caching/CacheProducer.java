package tech.lapsa.javax.caching;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import tech.lapsa.java.commons.function.MyStrings;

public class CacheProducer {

    @Inject
    private CacheManager mgr;

    @Produces
    @NamedCache
    public Cache<Object, Object> createCache(InjectionPoint injectionPoint) {

	final NamedCache annotation = injectionPoint.getAnnotated().getAnnotation(NamedCache.class);
	final String name = MyStrings.format("%1$s-%2$s-%3$s", annotation.name(),
		annotation.keyType().getCanonicalName(),
		annotation.valueType().getCanonicalName());

	Cache<Object, Object> cache = mgr.getCache(name);
	if (cache == null) {
	    MutableConfiguration<Object, Object> config = new MutableConfiguration<Object, Object>();
	    config.setTypes(Object.class, Object.class);
	    config.setStoreByValue(true);
	    config.setStatisticsEnabled(true);
	    config.setManagementEnabled(true);
	    config.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
	    cache = mgr.createCache(name, config);
	}

	return cache;
    }

}
