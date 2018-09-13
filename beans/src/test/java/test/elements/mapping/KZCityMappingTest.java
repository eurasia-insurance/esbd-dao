package test.elements.mapping;

import com.lapsa.kz.country.KZCity;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZCityMapping;

public class KZCityMappingTest extends AMappingTest<KZCity> {

    private static final String DICT = "SETTLEMENTS";

    public KZCityMappingTest() {
	super(KZCityMapping.getInstance(), DICT, KZCity.class);
    }
}
