package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.CancelationReason;

import tech.lapsa.esbd.beans.dao.elements.mapping.CancelationReasonMapping;
import tech.lapsa.esbd.dao.elements.CancelationReasonService.CancelationReasonServiceLocal;
import tech.lapsa.esbd.dao.elements.ElementsService;

public class CancelationReasonServiceTestCase extends AMappedElementTestCase<CancelationReason> {

    @Inject
    private CancelationReasonServiceLocal service;

    public CancelationReasonServiceTestCase() {
	super(CancelationReason.class, CancelationReasonMapping.getInstance(), "RESCINDING_REASONS", 99999);
    }

    @Override
    ElementsService<CancelationReason> service() {
	return service;
    }
}
