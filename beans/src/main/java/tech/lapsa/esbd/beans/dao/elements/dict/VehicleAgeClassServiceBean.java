package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleAgeClass;

import tech.lapsa.esbd.beans.dao.ADictElementsService;
import tech.lapsa.esbd.beans.dao.elements.dict.mapping.VehicleAgeClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceRemote;

@Singleton(name = VehicleAgeClassService.BEAN_NAME)
public class VehicleAgeClassServiceBean
	extends ADictElementsService<VehicleAgeClass>
	implements VehicleAgeClassServiceLocal, VehicleAgeClassServiceRemote {

    // constructor

    protected VehicleAgeClassServiceBean() {
	super(VehicleAgeClassService.class, VehicleAgeClass.class, VehicleAgeClassMapping.getInstance()::forId);
    }
}
