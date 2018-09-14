package test.elements.mapping;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.elements.mapping.CountryMapping;

public class CountryMappingTest extends AMappingTest<Country> {

    private static final String DICT = "COUNTRIES";

    public CountryMappingTest() {
	super(CountryMapping.getInstance(), DICT, Country.class);
    }
}
