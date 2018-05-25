package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZEconomicSectorMapping;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceRemote;

@Singleton(name = KZEconomicSectorService.BEAN_NAME)
public class KZEconomicSectorServiceBean
	extends ADictElementsService<KZEconomicSector>
	implements KZEconomicSectorServiceLocal, KZEconomicSectorServiceRemote {

    // constructor

    protected KZEconomicSectorServiceBean() {
	super(KZEconomicSectorService.class, KZEconomicSector.class, KZEconomicSectorMapping.getInstance()::forId);
    }
}
