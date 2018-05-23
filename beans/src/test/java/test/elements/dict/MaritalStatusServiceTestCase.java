package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.MaritalStatusMapping;
import tech.lapsa.esbd.dao.IElementsService;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService.MaritalStatusServiceLocal;

public class MaritalStatusServiceTestCase extends AMappedElementTestCase<MaritalStatus> {

    @Inject
    private MaritalStatusServiceLocal service;

    public MaritalStatusServiceTestCase() {
	super(MaritalStatus.class, MaritalStatusMapping.getInstance(), "HOUSEHOLD_POSITION", 99999);
    }

    @Override
    IElementsService<MaritalStatus> service() {
	return service;
    }
}
