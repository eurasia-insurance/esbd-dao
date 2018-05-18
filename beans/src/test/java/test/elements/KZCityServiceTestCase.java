package test.elements;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.kz.country.KZCity;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZCityMapping;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;

public class KZCityServiceTestCase extends AMappedElementIntTestCase<KZCity> {

    @Inject
    private KZCityServiceLocal service;

    public KZCityServiceTestCase() {
	super(KZCity.class, KZCityMapping.getInstance(), "SETTLEMENTS", 99999);
    }

    @Override
    ElementsService<KZCity> service() {
	return service;
    }

    @Override
    @Test
    public void testEveryElementValueMappedToDictionary() throws ConnectionException {
	final ArrayOfItem items;

	try (Connection con = pool.getConnection()) {
	    items = con.getItems(dictionaryName);
	}

	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final KZCity dict = mapper.forId(item.getID());
	    if (mapper.isException(item.getID())) {
		if (dict != null)
		    fail(String.format(
			    "ESBD dictionary '%1$s' name = '%2$s' with code '%3$s' and id = '%4$s' exceptions vs real values exception",
			    dictionaryName, // 1
			    item.getName(), // 2
			    item.getCode(), // 3
			    item.getID() // 4
		    ));
		continue;
	    }
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    dictionaryName, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    KZCity.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }
}
