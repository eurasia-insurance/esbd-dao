package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZEconomicSectorMapping;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceLocal;

public class EconomicsSectorServiceTestCase extends AMappedElementDictionaryTestCase<KZEconomicSector> {

    @Inject
    private KZEconomicSectorServiceLocal service;

    public EconomicsSectorServiceTestCase() {
	super(KZEconomicSector.class, KZEconomicSectorMapping.getInstance(), "ECONOMICS_SECTORS", 99999);
    }

    @Override
    protected KZEconomicSectorService service() {
	return service;
    }
}