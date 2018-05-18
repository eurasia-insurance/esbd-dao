package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZEconomicSectorMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceLocal;

public class EconomicsSectorServiceTestCase extends AMappedElementTestCase<KZEconomicSector> {

    @Inject
    private KZEconomicSectorServiceLocal service;

    public EconomicsSectorServiceTestCase() {
	super(KZEconomicSector.class, KZEconomicSectorMapping.getInstance(), "ECONOMICS_SECTORS", 99999);
    }

    @Override
    ElementsService<KZEconomicSector> service() {
	return service;
    }
}