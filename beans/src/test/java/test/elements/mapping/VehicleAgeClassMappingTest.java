package test.elements.mapping;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleAgeClassMapping;

public class VehicleAgeClassMappingTest extends AMappingTest<VehicleAgeClass> {

    private static final String DICT = "TF_AGE";

    public VehicleAgeClassMappingTest() {
	super(VehicleAgeClassMapping.getInstance(), DICT, VehicleAgeClass.class);
    }
}
