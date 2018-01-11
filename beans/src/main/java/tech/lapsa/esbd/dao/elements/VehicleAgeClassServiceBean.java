package tech.lapsa.esbd.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.dao.elements.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.VehicleAgeClassService.VehicleAgeClassServiceRemote;
import tech.lapsa.esbd.dao.elements.mapping.VehicleAgeClassMapping;

@Singleton(name = VehicleAgeClassService.BEAN_NAME)
public class VehicleAgeClassServiceBean
	extends AElementsService<VehicleAgeClass>
	implements VehicleAgeClassServiceLocal, VehicleAgeClassServiceRemote {

    public VehicleAgeClassServiceBean() {
	super(VehicleAgeClassService.class, VehicleAgeClassMapping.getInstance()::forId, VehicleAgeClass.class);
    }
}
