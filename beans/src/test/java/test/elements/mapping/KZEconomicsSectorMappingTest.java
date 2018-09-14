package test.elements.mapping;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZEconomicSectorMapping;

public class KZEconomicsSectorMappingTest extends AMappingTest<KZEconomicSector> {

    private static final String DICT = "ECONOMICS_SECTORS";

    public KZEconomicsSectorMappingTest() {
	super(KZEconomicSectorMapping.getInstance(), DICT, KZEconomicSector.class);
    }
}
