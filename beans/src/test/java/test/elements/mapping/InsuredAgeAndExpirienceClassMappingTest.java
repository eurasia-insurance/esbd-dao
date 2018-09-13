package test.elements.mapping;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.InsuredAgeAndExpirienceClassMapping;

public class InsuredAgeAndExpirienceClassMappingTest extends AMappingTest<InsuredAgeAndExpirienceClass> {

    private static final String DICT = "AGE_EXPERIENCE";

    public InsuredAgeAndExpirienceClassMappingTest() {
	super(InsuredAgeAndExpirienceClassMapping.getInstance(), DICT, InsuredAgeAndExpirienceClass.class);
    }
}
