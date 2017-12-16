package tech.lapsa.insurance.esbd.elements;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.lapsa.kz.economic.KZEconomicSector;

public interface KZEconomicSectorService extends ElementsService<KZEconomicSector, Integer> {

    public static final String BEAN_NAME = "KZEconomicSectorServiceBean";

    @Local
    public interface KZEconomicSectorServiceLocal extends KZEconomicSectorService {
    }

    @Remote
    public interface KZEconomicSectorServiceRemote extends KZEconomicSectorService {
    }
}
