package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.kz.country.KZArea.*;

import com.lapsa.kz.country.KZArea;

public class KZAreaMapping extends AElementsMapping<Integer, KZArea> {

    private static final class SingletonHolder {
	private static final KZAreaMapping HOLDER_INSTANCE = new KZAreaMapping();
    }

    public static final KZAreaMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private KZAreaMapping() {
	addMap(OALM, 1);
	addMap(OTRK, 20);
	addMap(OTRK, 2);
	addMap(OVK, 3);
	addMap(OKST, 4);
	addMap(OKGD, 5);
	addMap(OSK, 6);
	addMap(OAKM, 7);
	addMap(OPVL, 8);
	addMap(OZHM, 9);
	addMap(OAKT, 10);
	addMap(OZK, 11);
	addMap(OKZL, 12);
	addMap(OATY, 13);
	addMap(OMNG, 14);
	addMap(GALM, 15);
	addMap(GAST, 16);

	addMap(GALM, 17);
	addMap(GALM, 18);
	addMap(GSHM, 19);
    }
}