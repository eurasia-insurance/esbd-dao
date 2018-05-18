package test.elements.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.lapsa.insurance.elements.CancelationReason;
import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;
import com.lapsa.international.country.Country;
import com.lapsa.kz.country.KZArea;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.mapping.CancelationReasonMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.CountryMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.IdentityCardTypeMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.InsuredAgeAndExpirienceClassMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.KZAreaMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.KZCityMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.KZEconomicSectorMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleClassMapping;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfItem;
import tech.lapsa.esbd.jaxws.wsimport.Item;

public class MappingTestCase extends BaseTestCase {

    private static final String DICT_CANCELATION_REASON = "RESCINDING_REASONS";

    @Test
    public void testCancelationReasonMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_CANCELATION_REASON);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final CancelationReason dict = CancelationReasonMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_CANCELATION_REASON, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    CancelationReason.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_COUNTRY = "COUNTRIES";

    @Test
    public void testCountryMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_COUNTRY);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final Country dict = CountryMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_COUNTRY, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    Country.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_IDENTITY_CARD_TYPE = "DOCUMENTS_TYPES";

    @Test
    public void testIdentityCardTypeMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_IDENTITY_CARD_TYPE);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final IdentityCardType dict = IdentityCardTypeMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_IDENTITY_CARD_TYPE, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    IdentityCardType.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_AGE_AND_EXPIRIENCE_CLASS = "AGE_EXPERIENCE";

    @Test
    public void testInsuredAgeAndExpirienceClassMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_AGE_AND_EXPIRIENCE_CLASS);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final InsuredAgeAndExpirienceClass dict = InsuredAgeAndExpirienceClassMapping.getInstance()
		    .forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_AGE_AND_EXPIRIENCE_CLASS, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    InsuredAgeAndExpirienceClass.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_KZ_AREA = "REGIONS";

    @Test
    public void testKZAreaMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_AREA);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final KZArea dict = KZAreaMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_KZ_AREA, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    KZArea.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_KZ_CITY = "SETTLEMENTS";

    @Test
    public void testKZCityMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_CITY);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final KZCity dict = KZCityMapping.getInstance().forId(item.getID());
	    if (KZCityMapping.getInstance().isException(item.getID())) {
		if (dict != null)
		    fail(String.format(
			    "ESBD dictionary '%1$s' name = '%2$s' with code '%3$s' and id = '%4$s' exceptions vs real values exception",
			    DICT_KZ_CITY, // 1
			    item.getName(), // 2
			    item.getCode(), // 3
			    item.getID() // 4
		    ));
		continue;
	    }
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_KZ_CITY, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    KZCity.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    private static final String DICT_KZ_ECONOMIC_SECTOR = "ECONOMICS_SECTORS";

    @Test
    public void testKZEconomicsSectorMapping() {
	final ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_ECONOMIC_SECTOR);
	assertThat(items, not(nullValue()));
	final Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    final Item item = i.next();
	    final KZEconomicSector dict = KZEconomicSectorMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_KZ_ECONOMIC_SECTOR, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    KZEconomicSector.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

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
