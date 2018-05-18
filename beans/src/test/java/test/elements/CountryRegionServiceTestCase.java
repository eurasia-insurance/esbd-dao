package test.elements;

import javax.inject.Inject;

import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZAreaMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.KZAreaService.KZAreaServiceLocal;

public class CountryRegionServiceTestCase extends AMappedElementIntTestCase<KZArea> {

    @Inject
    private KZAreaServiceLocal service;

    public CountryRegionServiceTestCase() {
	super(KZArea.class, KZAreaMapping.getInstance(), "REGIONS", 99999);
    }

    @Override
    ElementsService<KZArea> service() {
	return service;
    }
}
