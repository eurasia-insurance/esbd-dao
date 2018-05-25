package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.kz.country.KZArea.*;

import com.lapsa.kz.country.KZArea;

public class KZAreaMapping extends AMapping<Integer, KZArea> {

    private static final class SingletonHolder {
	private static final KZAreaMapping HOLDER_INSTANCE = new KZAreaMapping();
    }

    public static final KZAreaMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private KZAreaMapping() {
	addMap(GAST, 16);
	addMap(GALM, 15);
	addMap(GALM, 17);
	addMap(GALM, 18);
	addMap(OAKM, 7);
	addMap(OAKT, 10);
	addMap(OALM, 1);
	addMap(OATY, 13);
	addMap(OZK, 11);
	addMap(OZHM, 9);
	addMap(OKGD, 5);
	addMap(OKST, 4);
	addMap(OKZL, 12);
	addMap(OMNG, 14);
	addMap(OUK, 2);
	addMap(OPVL, 8);
	addMap(OSK, 6);
	addMap(OVK, 3);
    }
}