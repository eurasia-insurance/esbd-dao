package test.elements.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.esbd.beans.dao.elements.mapping.AElementsMapping;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.IICWebService;
import tech.lapsa.esbd.jaxws.wsimport.IICWebServiceSoap;
import tech.lapsa.esbd.jaxws.wsimport.Item;
import tech.lapsa.esbd.jaxws.wsimport.User;

public abstract class AMappingTest<T extends Enum<T>> {

    private static final String ESBD_WS_USER_NAME = System.getenv("ASB_USER");
    private static final String ESBD_WS_USER_PASSWORD = System.getenv("ASB_PASSWORD");

    static IICWebServiceSoap soap;
    static String sessionId;

    @BeforeClass
    public static void createSession() {
	final IICWebService service = new IICWebService();
	soap = service.getIICWebServiceSoap();
	final User user = soap.authenticateUser(ESBD_WS_USER_NAME, ESBD_WS_USER_PASSWORD);
	sessionId = user.getSessionID();
    }

    final AElementsMapping<Integer, T> mapping;
    final String dictionary;
    final Class<T> entityClazz;

    protected AMappingTest(AElementsMapping<Integer, T> mapping, String dictionary, Class<T> entityClazz) {
	this.mapping = mapping;
	this.dictionary = dictionary;
	this.entityClazz = entityClazz;
    }

    @Test
    public void testAllMapped() {
	final ArrayOfItem items = soap.getItems(sessionId, dictionary);
	assertThat(items, not(nullValue()));

	items.getItem().stream().map(x -> x.getID() + " " + x.getCode() + " " + x.getName())
		.forEach(System.out::println);

	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final T dict = mapping.forId(item.getID());

	    if (mapping.isException(item.getID())) {
		if (dict != null)
		    fail(String.format(
			    "ESBD dictionary '%1$s' name = '%2$s' with code '%3$s' and id = '%4$s' exceptions vs real values exception",
			    dictionary, // 1
			    item.getName(), // 2
			    item.getCode(), // 3
			    item.getID() // 4
		    ));
		continue;
	    }

	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    dictionary, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    entityClazz.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }
}
