package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.Sex.*;

import com.lapsa.insurance.elements.Sex;

public final class SexMapping extends AbsMapping<Integer, Sex> {

    private static final class SingletonHolder {
	private static final SexMapping HOLDER_INSTANCE = new SexMapping();
    }

    public static final SexMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private SexMapping() {
	addMap(MALE, 1); // мужской пол
	addMap(FEMALE, 2); // женский пол
    }
}
