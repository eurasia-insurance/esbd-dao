package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.IdentityCardType.*;

import com.lapsa.insurance.elements.IdentityCardType;

public final class IdentityCardTypeMapping extends ElementsMapping<Integer, IdentityCardType> {

    private static final class SingletonHolder {
	private static final IdentityCardTypeMapping HOLDER_INSTANCE = new IdentityCardTypeMapping();
    }

    public static final IdentityCardTypeMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private IdentityCardTypeMapping() {
	addMap(ID_CARD, 1); // удостоверение личности
	addMap(PASSPORT, 2); // паспорт
	addMap(BIRTH_CERTIFICATE, 3); // свидетельство о рождении
	addMap(RESIDENCE_PERMIT, 4); // вид на жительство иностранца
	addMap(DIPLOMATIC_PASSPORT, 5); // дипломатический паспорт
    }
}
