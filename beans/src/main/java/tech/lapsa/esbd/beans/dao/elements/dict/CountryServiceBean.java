package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.elements.mapping.CountryMapping;
import tech.lapsa.esbd.dao.elements.CountryService;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceRemote;

@Singleton(name = CountryService.BEAN_NAME)
public class CountryServiceBean
	extends AElementsService<Country>
	implements CountryServiceLocal, CountryServiceRemote {

    public CountryServiceBean() {
	super(CountryService.class, CountryMapping.getInstance()::forId, Country.class);
    }
}