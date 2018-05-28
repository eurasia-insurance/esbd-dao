package test.caching;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;
import test.caching.beans.CachableEntity;
import test.caching.beans.CachableEntityServiceBean;

public class CacheSystemTest extends ArquillianBaseTestCase {

    @Inject
    private CachableEntityServiceBean service;

    private static final Integer VALID_ID = 123;

    @Test
    public void testGetByIdUsingCache() throws IllegalArgument {
	try {
	    final CachableEntity fetch = service.getById(VALID_ID);
	    final CachableEntity fromCache = service.getById(VALID_ID);
	    final CachableEntity fetchAgain = service.getByIdBypassCache(VALID_ID);
	    final CachableEntity fromCacheAgain = service.getById(VALID_ID);

	    assertThat(fetch, not(nullValue()));
	    assertThat(fromCache, allOf(not(nullValue()), equalTo(fetch)));
	    assertThat(fetchAgain, allOf(not(nullValue()), not(equalTo(fetch)), not(equalTo(fromCache))));
	    assertThat(fromCacheAgain, allOf(not(nullValue()),
		    not(equalTo(fetch)),
		    not(equalTo(fromCache)),
		    equalTo(fetchAgain)));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

}
