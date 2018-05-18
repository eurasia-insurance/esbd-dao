package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.elements.mapping.MaritalStatusMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.MaritalStatusService.MaritalStatusServiceLocal;

public class MaritalStatusServiceTestCase extends AMappedElementTestCase<MaritalStatus> {

    @Inject
    private MaritalStatusServiceLocal service;

    public MaritalStatusServiceTestCase() {
	super(MaritalStatus.class, MaritalStatusMapping.getInstance(), "HOUSEHOLD_POSITION", 99999);
    }

    @Override
    ElementsService<MaritalStatus> service() {
	return service;
    }
}
