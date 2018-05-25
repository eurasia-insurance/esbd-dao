package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceRemote;

@Singleton(name = VehicleClassService.BEAN_NAME)
public class VehicleClassServiceBean
	extends ADictElementsService<VehicleClass>
	implements VehicleClassServiceLocal, VehicleClassServiceRemote {

    // constructor

    protected VehicleClassServiceBean() {
	super(VehicleClassService.class, VehicleClass.class, VehicleClassMapping.getInstance()::forId);
    }
}
