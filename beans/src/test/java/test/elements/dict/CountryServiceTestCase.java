package test.elements;

import javax.inject.Inject;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.elements.mapping.CountryMapping;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.ElementsService;

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
