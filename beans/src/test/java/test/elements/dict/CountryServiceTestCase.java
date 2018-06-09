package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.elements.mapping.CountryMapping;
import tech.lapsa.esbd.dao.elements.dict.CountryService;
import tech.lapsa.esbd.dao.elements.dict.CountryService.CountryServiceLocal;

public class CountryServiceTestCase extends AMappedElementDictionaryTestCase<Country> {

    @Inject
    private CountryServiceLocal service;

    public CountryServiceTestCase() {
	super(Country.class, CountryMapping.getInstance(), "COUNTRIES", 99999);
    }

    @Override
    protected CountryService service() {
	return service;
    }
}
