package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass.*;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

public final class InsuredAgeAndExpirienceClassMapping extends ElementsMapping<Integer, InsuredAgeAndExpirienceClass> {

    private static final class SingletonHolder {
        private static final InsuredAgeAndExpirienceClassMapping HOLDER_INSTANCE = new InsuredAgeAndExpirienceClassMapping();
    }

    public static final InsuredAgeAndExpirienceClassMapping getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private InsuredAgeAndExpirienceClassMapping() {
        addMap(OVER25_MORE2, 4);
        addMap(UNDER25_LESS2, 1);
        addMap(UNDER25_MORE2, 2);
        addMap(OVER25_LESS2, 3);
    }
}
