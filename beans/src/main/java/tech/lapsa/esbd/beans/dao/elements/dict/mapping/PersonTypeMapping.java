package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.insurance.elements.PersonType.*;

import com.lapsa.insurance.elements.PersonType;

public final class PersonTypeMapping extends AMapping<Integer, PersonType> {

    private static final class SingletonHolder {
	private static final PersonTypeMapping HOLDER_INSTANCE = new PersonTypeMapping();
    }

    public static final PersonTypeMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private PersonTypeMapping() {
	addMap(INDIVIDUAL, 1); // Физическое лицо
	addMap(INDIVIDUAL_ENTREPRENEUR, 2); // Индивидуальный
					    // предприниматель/Крестьянское
					    // (фермерское) хозяйство
    }
}
