package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.CancelationReason;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.CancelationReasonMapping;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService.CancelationReasonServiceLocal;

public class CancelationReasonServiceTestCase extends AMappedElementDictionaryTestCase<CancelationReason> {

    @Inject
    private CancelationReasonServiceLocal service;

    public CancelationReasonServiceTestCase() {
	super(CancelationReason.class, CancelationReasonMapping.getInstance(), "RESCINDING_REASONS", 99999);
    }

    @Override
    protected CancelationReasonService service() {
	return service;
    }
}
