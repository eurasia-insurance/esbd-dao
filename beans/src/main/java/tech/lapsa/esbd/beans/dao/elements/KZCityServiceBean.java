package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.kz.country.KZCity;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZCityMapping;
import tech.lapsa.esbd.dao.elements.KZCityService;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceRemote;

@Singleton(name = KZCityService.BEAN_NAME)
public class KZCityServiceBean extends AElementsService<KZCity> implements KZCityServiceLocal, KZCityServiceRemote {

    public KZCityServiceBean() {
        super(KZCityService.class, KZCityMapping.getInstance()::forId, KZCity.class);
    }
}