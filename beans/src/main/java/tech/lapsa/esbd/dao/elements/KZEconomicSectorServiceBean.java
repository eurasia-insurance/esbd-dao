package tech.lapsa.esbd.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceRemote;
import tech.lapsa.esbd.dao.elements.mapping.KZEconomicSectorMapping;

@Singleton(name = KZEconomicSectorService.BEAN_NAME)
public class KZEconomicSectorServiceBean
	extends AElementsService<KZEconomicSector>
	implements KZEconomicSectorServiceLocal, KZEconomicSectorServiceRemote {

    public KZEconomicSectorServiceBean() {
	super(KZEconomicSectorService.class, KZEconomicSectorMapping.getInstance()::forId);
    }
}
