package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.VehicleClassMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;

public class VehicleClassServiceTestCase extends AMappedElementTestCase<VehicleClass> {

    @Inject
    private VehicleClassServiceLocal service;

    public VehicleClassServiceTestCase() {
	super(VehicleClass.class, VehicleClassMapping.getInstance(), "TF_TYPES", 99999);
    }

    @Override
    ElementsService<VehicleClass> service() {
	return service;
    }
}
