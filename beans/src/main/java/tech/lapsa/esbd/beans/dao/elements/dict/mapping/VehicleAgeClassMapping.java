package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.VehicleAgeClass.*;

import com.lapsa.insurance.elements.VehicleAgeClass;

public final class VehicleAgeClassMapping extends AbsMapping<Integer, VehicleAgeClass> {

    private static final class SingletonHolder {
	private static final VehicleAgeClassMapping HOLDER_INSTANCE = new VehicleAgeClassMapping();
    }

    public static final VehicleAgeClassMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private VehicleAgeClassMapping() {
	addMap(UNDER7, 1); // До 7 лет включительно
	addMap(OVER7, 2); // Свыше 7 лет
    }
}
