package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.insurance.elements.PaymentType.*;

import com.lapsa.insurance.elements.PaymentType;

public final class PaymentTypeMapping extends AMapping<Integer, PaymentType> {

    private static final class SingletonHolder {
	private static final PaymentTypeMapping HOLDER_INSTANCE = new PaymentTypeMapping();
    }

    public static final PaymentTypeMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private PaymentTypeMapping() {
	addMap(IMMEDIATELY, 1); // Единовременно
	addMap(BY_INSTALLMENTS, 2); // В рассрочку
    }
}
