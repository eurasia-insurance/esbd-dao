package test.elements.mapping;

import com.lapsa.insurance.elements.CancelationReason;

import tech.lapsa.esbd.beans.dao.elements.mapping.CancelationReasonMapping;

public class CancelationReasonMappingTest extends AMappingTest<CancelationReason> {

    private static final String DICT = "RESCINDING_REASONS";

    public CancelationReasonMappingTest() {
	super(CancelationReasonMapping.getInstance(), DICT, CancelationReason.class);
    }
}
