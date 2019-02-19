package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.elements.mapping.MaritalStatusMapping;
import tech.lapsa.esbd.dao.elements.MaritalStatusService;
import tech.lapsa.esbd.dao.elements.MaritalStatusService.MaritalStatusServiceLocal;
import tech.lapsa.esbd.dao.elements.MaritalStatusService.MaritalStatusServiceRemote;

@Singleton(name = MaritalStatusService.BEAN_NAME)
public class MaritalStatusServiceBean extends AElementsService<MaritalStatus>
        implements MaritalStatusServiceLocal, MaritalStatusServiceRemote {

	public MaritalStatusServiceBean() {
		super(MaritalStatusService.class, MaritalStatusMapping.getInstance()::forId, MaritalStatus.class);
	}
}
