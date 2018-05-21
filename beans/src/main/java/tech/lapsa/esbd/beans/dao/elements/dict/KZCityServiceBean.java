package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.kz.country.KZCity;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.KZCityMapping;
import tech.lapsa.esbd.dao.elements.dict.KZCityService;
import tech.lapsa.esbd.dao.elements.dict.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZCityService.KZCityServiceRemote;

@Singleton(name = KZCityService.BEAN_NAME)
public class KZCityServiceBean
	extends ADictElementsService<KZCity>
	implements KZCityServiceLocal, KZCityServiceRemote {

    public KZCityServiceBean() {
	super(KZCityService.class, KZCityMapping.getInstance()::forId, KZCity.class);
    }
}