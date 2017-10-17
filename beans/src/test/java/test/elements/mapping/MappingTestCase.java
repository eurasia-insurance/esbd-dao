package test.elements.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.lapsa.esbd.jaxws.client.ArrayOfItem;
import com.lapsa.esbd.jaxws.client.Item;
import com.lapsa.insurance.elements.CancelationReason;
import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.MaritalStatus;
import com.lapsa.insurance.elements.Sex;
import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.CancelationReasonMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.CountryMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.IdentityCardTypeMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.InsuredAgeAndExpirienceClassMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.KZAreaMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.KZCityMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.KZEconomicSectorMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.MaritalStatusMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.SexMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.VehicleAgeClassMapping;
import com.lapsa.insurance.esbd.services.impl.elements.mapping.VehicleClassMapping;
import com.lapsa.international.country.Country;
import com.lapsa.kz.country.KZArea;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

public class MappingTestCase extends BaseTestCase {

    private static final String DICT_CANCELATION_REASON = "RESCINDING_REASONS";
    private static final String DICT_COUNTRY = "COUNTRIES";
    private static final String DICT_IDENTITY_CARD_TYPE = "DOCUMENTS_TYPES";
    private static final String DICT_AGE_AND_EXPIRIENCE_CLASS = "AGE_EXPERIENCE";
    private static final String DICT_KZ_AREA = "REGIONS";
    private static final String DICT_KZ_CITY = "SETTLEMENTS";
    private static final String DICT_KZ_ECONOMIC_SECTOR = "ECONOMICS_SECTORS";
    private static final String DICT_MARITAL_STATUS = "HOUSEHOLD_POSITION";
    private static final String DICT_SEX = "SEX";
    private static final String DICT_VEHICLE_AGE_CLASS = "TF_AGE";
    private static final String DICT_VEHICLE_CLASS = "TF_TYPES";

    @Test
    public void testCancelationReasonMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_CANCELATION_REASON);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    CancelationReason dict = CancelationReasonMapping.getInstance().forId(item.getID());
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

    @Test
    public void testCountryMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_COUNTRY);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    Country dict = CountryMapping.getInstance().forId(item.getID());
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

    @Test
    public void testIdentityCardTypeMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_IDENTITY_CARD_TYPE);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    IdentityCardType dict = IdentityCardTypeMapping.getInstance().forId(item.getID());
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

    @Test
    public void testInsuredAgeAndExpirienceClassMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_AGE_AND_EXPIRIENCE_CLASS);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    InsuredAgeAndExpirienceClass dict = InsuredAgeAndExpirienceClassMapping.getInstance().forId(item.getID());
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

    @Test
    public void testKZAreaMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_AREA);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    KZArea dict = KZAreaMapping.getInstance().forId(item.getID());
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

    @Test
    public void testKZCityMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_CITY);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    KZCity dict = KZCityMapping.getInstance().forId(item.getID());
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

    @Test
    public void testKZEconomicsSectorMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_KZ_ECONOMIC_SECTOR);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    KZEconomicSector dict = KZEconomicSectorMapping.getInstance().forId(item.getID());
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

    @Test
    public void testMaritalStatusMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_MARITAL_STATUS);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    MaritalStatus dict = MaritalStatusMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_MARITAL_STATUS, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    MaritalStatus.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    @Test
    public void testSexMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_SEX);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    Sex dict = SexMapping.getInstance().forId(item.getID());
	    assertThat(String.format(
		    "ESBD  dictionary '%1$s' name = '%2$s' with code = '%3$s' and id = '%4$s' present, but %5$s enum variable is missing",
		    DICT_SEX, // 1
		    item.getName(), // 2
		    item.getCode(), // 3
		    item.getID(), // 4
		    Sex.class.getSimpleName() // 5
	    ), dict, not(nullValue()));
	}
    }

    @Test
    public void testVehicleAgeClassMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_VEHICLE_AGE_CLASS);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    VehicleAgeClass dict = VehicleAgeClassMapping.getInstance().forId(item.getID());
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

    @Test
    public void testVehicleClassMapping() {
	ArrayOfItem items = getSoap().getItems(getSessionId(), DICT_VEHICLE_CLASS);
	assertThat(items, not(nullValue()));
	Iterator<Item> i = items.getItem().iterator();
	while (i.hasNext()) {
	    Item item = i.next();
	    VehicleClass dict = VehicleClassMapping.getInstance().forId(item.getID());
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
