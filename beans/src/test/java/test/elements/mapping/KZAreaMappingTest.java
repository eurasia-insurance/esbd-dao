package test.elements.mapping;

import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZAreaMapping;

public class KZAreaMappingTest extends AMappingTest<KZArea> {

    private static final String DICT = "REGIONS";

    public KZAreaMappingTest() {
	super(KZAreaMapping.getInstance(), DICT, KZArea.class);
    }
}
