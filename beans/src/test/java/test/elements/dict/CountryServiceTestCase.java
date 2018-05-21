package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.CountryMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.dict.CountryService.CountryServiceLocal;

public class CountryServiceTestCase extends AMappedElementTestCase<Country> {

    @Inject
    private CountryServiceLocal service;

    public CountryServiceTestCase() {
	super(Country.class, CountryMapping.getInstance(), "COUNTRIES", 99999);
    }

    @Override
    ElementsService<Country> service() {
	return service;
    }
}
