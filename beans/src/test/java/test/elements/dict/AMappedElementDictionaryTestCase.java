package test.elements.dict;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.beans.dao.elements.mapping.AElementsMapping;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;
import test.elements.AMappedElementTestCase;

public abstract class AMappedElementDictionaryTestCase<T extends Enum<T>> extends AMappedElementTestCase<T> {

    final String dictionaryName;

    protected AMappedElementDictionaryTestCase(final Class<T> clazz, AElementsMapping<Integer, T> mapper, String dictionaryName,
	    int invalidId) {
	super(clazz, mapper, invalidId);
	this.dictionaryName = dictionaryName;
    }

    @Inject
    protected ConnectionPool pool;

    @Test
    @Override
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
