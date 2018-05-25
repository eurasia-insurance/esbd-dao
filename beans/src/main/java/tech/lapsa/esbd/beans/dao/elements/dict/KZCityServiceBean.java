package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.kz.country.KZCity;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZCityMapping;
import tech.lapsa.esbd.dao.elements.dict.KZCityService;
import tech.lapsa.esbd.dao.elements.dict.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZCityService.KZCityServiceRemote;

@Singleton(name = KZCityService.BEAN_NAME)
public class KZCityServiceBean
	extends ADictElementsService<KZCity>
	implements KZCityServiceLocal, KZCityServiceRemote {

    // constructor

    protected KZCityServiceBean() {
	super(KZCityService.class, KZCity.class, KZCityMapping.getInstance()::forId);
    }
}