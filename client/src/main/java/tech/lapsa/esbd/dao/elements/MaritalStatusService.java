package tech.lapsa.esbd.dao.elements;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.lapsa.insurance.elements.MaritalStatus;

public interface MaritalStatusService extends ElementsService<MaritalStatus, Integer> {

    public static final String BEAN_NAME = "MaritalStatusServiceBean";

    @Local
    public interface MaritalStatusServiceLocal
	    extends ElementsServiceLocal<MaritalStatus, Integer>, MaritalStatusService {
    }

    @Remote
    public interface MaritalStatusServiceRemote
	    extends ElementsServiceRemote<MaritalStatus, Integer>, MaritalStatusService {
    }

}
