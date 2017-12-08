package tech.lapsa.insurance.esbd.elements;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.lapsa.international.country.Country;

public interface CountryService extends ElementsService<Country, Integer> {

    @Local
    public interface CountryServiceLocal extends CountryService {
    }

    @Remote
    public interface CountryServiceRemote extends CountryService {
    }
}
