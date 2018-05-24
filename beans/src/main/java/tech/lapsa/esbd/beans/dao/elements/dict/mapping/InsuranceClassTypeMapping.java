package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.insurance.elements.InsuranceClassType.*;

import com.lapsa.insurance.elements.InsuranceClassType;

public final class InsuranceClassTypeMapping extends AMapping<Integer, InsuranceClassType> {

    private static final class SingletonHolder {
	private static final InsuranceClassTypeMapping HOLDER_INSTANCE = new InsuranceClassTypeMapping();
    }

    public static final InsuranceClassTypeMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private InsuranceClassTypeMapping() {
	addMap(CLASS_M, 1);
	addMap(CLASS_0, 2);
	addMap(CLASS_1, 3);
	addMap(CLASS_2, 4);
	addMap(CLASS_3, 5);
	addMap(CLASS_4, 6);
	addMap(CLASS_5, 7);
	addMap(CLASS_6, 8);
	addMap(CLASS_7, 9);
	addMap(CLASS_8, 10);
	addMap(CLASS_9, 11);
	addMap(CLASS_10, 12);
	addMap(CLASS_11, 13);
	addMap(CLASS_12, 14);
	addMap(CLASS_13, 15);
    }
}
