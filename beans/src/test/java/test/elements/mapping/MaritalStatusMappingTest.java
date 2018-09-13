package test.elements.mapping;

import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.elements.mapping.MaritalStatusMapping;

public class MaritalStatusMappingTest extends AMappingTest<MaritalStatus> {

    private static final String DICT = "HOUSEHOLD_POSITION";

    public MaritalStatusMappingTest() {
	super(MaritalStatusMapping.getInstance(), DICT, MaritalStatus.class);
    }
}
