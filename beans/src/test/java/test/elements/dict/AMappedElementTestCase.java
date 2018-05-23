package test.elements.dict;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.AMapping;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.IElementsService;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public abstract class AMappedElementTestCase<T extends Enum<T>> extends ArquillianBaseTestCase {

    final Class<T> clazz;
    final AMapping<Integer, T> mapper;
    final String dictionaryName;
    final int invalidId;

    abstract IElementsService<T> service();

    public AMappedElementTestCase(final Class<T> clazz, AMapping<Integer, T> mapper, String dictionaryName,
	    int invalidId) {
	this.clazz = clazz;
	this.mapper = mapper;
	this.dictionaryName = dictionaryName;
	this.invalidId = invalidId;
    }

    @Test
    public void testGetById() throws IllegalArgument {
	for (final int id : mapper.getAllIds())
	    try {
		final T res = service().getById(id);
		assertThat(res, allOf(not(nullValue()), equalTo(mapper.forId(id))));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service().getById(invalidId);
    }

    @Inject
    ConnectionPool pool;

    @Test
    public void testEveryElementValueMappedToDictionary() throws ConnectionException {
	final ArrayOfItem items;
	try (Connection con = pool.getConnection()) {
	    items = con.getItems(dictionaryName);
	}

	assertThat(items, not(nullValue()));
	items.getItem().stream().map(i -> i.getID() + " '" + i.getName() + "'").forEach(System.out::println);
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final T dict = mapper.forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    dictionaryName, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    clazz.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }
}
