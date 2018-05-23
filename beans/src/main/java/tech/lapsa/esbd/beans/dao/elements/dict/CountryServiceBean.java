package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.international.country.Country;

import tech.lapsa.esbd.beans.dao.ADictElementsService;
import tech.lapsa.esbd.beans.dao.elements.dict.mapping.CountryMapping;
import tech.lapsa.esbd.dao.elements.dict.CountryService;
import tech.lapsa.esbd.dao.elements.dict.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.CountryService.CountryServiceRemote;

@Singleton(name = CountryService.BEAN_NAME)
public class CountryServiceBean
	extends ADictElementsService<Country>
	implements CountryServiceLocal, CountryServiceRemote {

    // constructor

    protected CountryServiceBean() {
	super(CountryService.class, Country.class, CountryMapping.getInstance()::forId);
    }
}