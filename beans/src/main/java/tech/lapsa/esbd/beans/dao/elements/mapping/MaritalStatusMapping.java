package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.MaritalStatus.*;

import com.lapsa.insurance.elements.MaritalStatus;

public final class MaritalStatusMapping extends ElementsMapping<Integer, MaritalStatus> {

    private static final class SingletonHolder {
        private static final MaritalStatusMapping HOLDER_INSTANCE = new MaritalStatusMapping();
    }

    public static final MaritalStatusMapping getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private MaritalStatusMapping() {
        addMap(MARRIED, 1); // женат / замужем
        addMap(SINGLE, 2); // холост / незамужем
    }
}