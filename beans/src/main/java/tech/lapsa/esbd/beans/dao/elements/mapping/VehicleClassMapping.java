package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.insurance.elements.VehicleClass.*;

import com.lapsa.insurance.elements.VehicleClass;

public final class VehicleClassMapping extends AMapping<Integer, VehicleClass> {

    private static final class SingletonHolder {
	private static final VehicleClassMapping HOLDER_INSTANCE = new VehicleClassMapping();
    }

    public static final VehicleClassMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private VehicleClassMapping() {
	addMap(BUS16, 5); // Автобусы до 16 пассажирских мест включительно
	addMap(BUSOVR16, 11); // Автобусы свыше 16 пассажирских мест
	addMap(CARGO, 6); // Грузовые
	addMap(CAR, 4); // Легковые
	addMap(MOTO, 8); // Мототранспорт
	addMap(TRAILER, 10); // Прицепы (полуприцепы)

	addMap(TRAM, 7); // Троллейбусы, трамваи
	addMap(SEA, 13); // SEA Морское
	addMap(AIRCRAFT, 12); // AIRCRAFT Воздушный транспорт
	addMap(RAILWAY, 14); // RAILWAY Железнодорожный транспорт
	addMap(SPECIAL, 15); // SPECIAL Спец.техника
    }
}
