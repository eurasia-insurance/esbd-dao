package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.insurance.elements.InsuranceClassType.*;

import com.lapsa.insurance.elements.InsuranceClassType;

public final class InsuranceClassTypeMapping extends AMapping<String, InsuranceClassType> {

    private static final class SingletonHolder {
	private static final InsuranceClassTypeMapping HOLDER_INSTANCE = new InsuranceClassTypeMapping();
    }

    public static final InsuranceClassTypeMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private InsuranceClassTypeMapping() {
	addMap(CLASS_0, "0");
	addMap(CLASS_1, "1");
	addMap(CLASS_2, "2");
	addMap(CLASS_3, "3");
	addMap(CLASS_4, "4");
	addMap(CLASS_5, "5");
	addMap(CLASS_6, "6");
	addMap(CLASS_7, "7");
	addMap(CLASS_8, "8");
	addMap(CLASS_9, "9");
	addMap(CLASS_10, "10");
	addMap(CLASS_11, "11");
	addMap(CLASS_12, "12");
	addMap(CLASS_13, "13");
	addMap(CLASS_M, "M");
    }
}
