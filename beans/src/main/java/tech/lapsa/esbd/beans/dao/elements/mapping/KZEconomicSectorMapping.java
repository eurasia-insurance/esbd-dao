package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.kz.economic.KZEconomicSector.*;

import com.lapsa.kz.economic.KZEconomicSector;

public class KZEconomicSectorMapping extends AMapping<Integer, KZEconomicSector> {

    private static final class SingletonHolder {
	private static final KZEconomicSectorMapping HOLDER_INSTANCE = new KZEconomicSectorMapping();
    }

    public static final KZEconomicSectorMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private KZEconomicSectorMapping() {
	addMap(GOVERNMENT, 2); // Правительство Республики Казахстан
	addMap(ADMINISTRATION, 3); // Региональные и местные органы управления
	addMap(CENTRAL_BANK, 4); // Национальный банк РК и Центральные Банки
				 // других стран
	addMap(DEPOSITORY, 5); // Другие депозитные организации
	addMap(FINANCIAL, 6); // Другие финансовые организации
	addMap(GOVERNMENT_NONFINALCIAL, 7); // Государственные нефинансовые
					    // организации
	addMap(NONGOVERNMENT_NONFINALCIAL, 8); // Негосударственные
					       // нефинансовые организации
	addMap(NONPROFIT_FOR_HOUSEHOLDS, 9); // Некоммерческие организации,
					     // обслуживающие домашние
					     // хозяйства
	addMap(HOUSEHOLDS, 10); // Домашние хозяйства
	addMap(INTERNATIONAL_COMPANIES, 11); // Международные организации
    }
}