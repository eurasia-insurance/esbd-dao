package test.elements.mapping;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleClassMapping;

public class VehicleClassMappingTest extends AMappingTest<VehicleClass> {

    private static final String DICT = "TF_TYPES";

    public VehicleClassMappingTest() {
	super(VehicleClassMapping.getInstance(), DICT, VehicleClass.class);
    }
}
