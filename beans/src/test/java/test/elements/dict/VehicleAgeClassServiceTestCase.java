package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceLocal;

public class VehicleAgeClassServiceTestCase extends AMappedElementDictionaryTestCase<VehicleAgeClass> {

    @Inject
    private VehicleAgeClassServiceLocal service;

    public VehicleAgeClassServiceTestCase() {
	super(VehicleAgeClass.class, VehicleAgeClassMapping.getInstance(), "TF_AGE", 99999);
    }

    @Override
    protected VehicleAgeClassService service() {
	return service;
    }
}
