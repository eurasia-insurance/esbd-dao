package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.MaritalStatusMapping;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService.MaritalStatusServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService.MaritalStatusServiceRemote;

@Singleton(name = MaritalStatusService.BEAN_NAME)
public class MaritalStatusServiceBean
	extends ADictElementsService<MaritalStatus>
	implements MaritalStatusServiceLocal, MaritalStatusServiceRemote {

    public MaritalStatusServiceBean() {
	super(MaritalStatusService.class, MaritalStatusMapping.getInstance()::forId, MaritalStatus.class);
    }
}
