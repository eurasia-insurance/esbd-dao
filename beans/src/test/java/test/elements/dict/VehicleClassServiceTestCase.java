package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.VehicleClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;

public class VehicleClassServiceTestCase extends AMappedElementDictionaryTestCase<VehicleClass> {

    @Inject
    private VehicleClassServiceLocal service;

    public VehicleClassServiceTestCase() {
	super(VehicleClass.class, VehicleClassMapping.getInstance(), "TF_TYPES", 99999);
    }

    @Override
    protected VehicleClassService service() {
	return service;
    }
}
