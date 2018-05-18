package tech.lapsa.esbd.beans.dao.elements.dict.mapping;

import static com.lapsa.insurance.elements.CancelationReason.*;

import com.lapsa.insurance.elements.CancelationReason;

public final class CancelationReasonMapping extends AMapping<Integer, CancelationReason> {

    private static final class SingletonHolder {
	private static final CancelationReasonMapping HOLDER_INSTANCE = new CancelationReasonMapping();
    }

    public static final CancelationReasonMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private CancelationReasonMapping() {
	addMap(CANCELATION_AND_RENEW, 1); // Досрочное прекращение договора и
					  // заключение нового
	addMap(CANCELATION, 2); // Досрочное расторжение
	addMap(MADE_INSURANCE_PAYMENT, 3); // Произведена страховая выплата
	addMap(PREMIUM_COST_ERROR, 4); // Корректировка неверно рассчитанной
				       // премии
	addMap(POLICY_LOST, 5); // Утеря
	addMap(ISSUED_DUPLICATE_POLICY, 7); // Выпущен дубликат
	addMap(OTHER, 8); // Другая причина
	addMap(HUMAN_FAILURE, 6); // Ошибка оператора
    }
}
