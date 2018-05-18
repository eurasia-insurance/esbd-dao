package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceRemote;

@Singleton(name = VehicleAgeClassService.BEAN_NAME)
public class VehicleAgeClassServiceBean
	extends ADictElementsService<VehicleAgeClass>
	implements VehicleAgeClassServiceLocal, VehicleAgeClassServiceRemote {

    public VehicleAgeClassServiceBean() {
	super(VehicleAgeClassService.class, VehicleAgeClassMapping.getInstance()::forId, VehicleAgeClass.class);
    }
}
