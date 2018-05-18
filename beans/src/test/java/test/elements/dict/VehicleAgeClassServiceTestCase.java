package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.VehicleAgeClassService.VehicleAgeClassServiceLocal;

public class VehicleAgeClassServiceTestCase extends AMappedElementTestCase<VehicleAgeClass> {

    @Inject
    private VehicleAgeClassServiceLocal service;

    public VehicleAgeClassServiceTestCase() {
	super(VehicleAgeClass.class, VehicleAgeClassMapping.getInstance(), "TF_AGE", 99999);
    }

    @Override
    ElementsService<VehicleAgeClass> service() {
	return service;
    }
}
