package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.KZAreaMapping;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService.KZAreaServiceLocal;

public class CountryRegionServiceTestCase extends AMappedElementDictionaryTestCase<KZArea> {

    @Inject
    private KZAreaServiceLocal service;

    public CountryRegionServiceTestCase() {
	super(KZArea.class, KZAreaMapping.getInstance(), "REGIONS", 99999);
    }

    @Override
    protected KZAreaService service() {
	return service;
    }
}
