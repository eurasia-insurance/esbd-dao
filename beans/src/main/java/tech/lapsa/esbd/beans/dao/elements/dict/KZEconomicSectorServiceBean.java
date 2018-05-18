package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.KZEconomicSectorMapping;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZEconomicSectorService.KZEconomicSectorServiceRemote;

@Singleton(name = KZEconomicSectorService.BEAN_NAME)
public class KZEconomicSectorServiceBean
	extends ADictElementsService<KZEconomicSector>
	implements KZEconomicSectorServiceLocal, KZEconomicSectorServiceRemote {

    public KZEconomicSectorServiceBean() {
	super(KZEconomicSectorService.class, KZEconomicSectorMapping.getInstance()::forId, KZEconomicSector.class);
    }
}
