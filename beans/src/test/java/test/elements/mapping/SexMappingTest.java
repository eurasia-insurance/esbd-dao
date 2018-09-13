package test.elements.mapping;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.mapping.SexMapping;

public class SexMappingTest extends AMappingTest<Sex> {

    private static final String DICT = "SEX";

    public SexMappingTest() {
	super(SexMapping.getInstance(), DICT, Sex.class);
    }
}
