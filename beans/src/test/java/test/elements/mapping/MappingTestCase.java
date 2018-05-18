package test.elements.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleClassMapping;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;

public class MappingTestCase extends BaseTestCase {

    private static final String DICT_VEHICLE_AGE_CLASS = "TF_AGE";

    @Test
    public void testVehicleAgeClassMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_VEHICLE_AGE_CLASS);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final VehicleAgeClass dict = VehicleAgeClassMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_VEHICLE_AGE_CLASS, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    VehicleAgeClass.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_VEHICLE_CLASS = "TF_TYPES";

    @Test
    public void testVehicleClassMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_VEHICLE_CLASS);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final VehicleClass dict = VehicleClassMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_VEHICLE_CLASS, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    VehicleClass.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

}
