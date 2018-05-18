package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.VehicleClassMapping;
import tech.lapsa.esbd.dao.elements.VehicleClassService;
import tech.lapsa.esbd.dao.elements.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.elements.VehicleClassService.VehicleClassServiceRemote;

@Singleton(name = VehicleClassService.BEAN_NAME)
public class VehicleClassServiceBean
	extends AElementsService<VehicleClass>
	implements VehicleClassServiceLocal, VehicleClassServiceRemote {

    public VehicleClassServiceBean() {
	super(VehicleClassService.class, VehicleClassMapping.getInstance()::forId, VehicleClass.class);
    }
}
