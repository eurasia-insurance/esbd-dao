package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.VehicleClassMapping;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceRemote;

@Singleton(name = VehicleClassService.BEAN_NAME)
public class VehicleClassServiceBean
	extends ADictElementsService<VehicleClass>
	implements VehicleClassServiceLocal, VehicleClassServiceRemote {

    public VehicleClassServiceBean() {
	super(VehicleClassService.class, VehicleClassMapping.getInstance()::forId, VehicleClass.class);
    }
}
