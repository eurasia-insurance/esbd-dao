package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.PaymentType;

import tech.lapsa.esbd.beans.dao.elements.mapping.PaymentTypeMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.PaymentTypeService.PaymentTypeServiceLocal;

public class PaymentTypeServiceTestCase extends AMappedElementIntTestCase<PaymentType> {

    @Inject
    private PaymentTypeServiceLocal service;

    public PaymentTypeServiceTestCase() {
	super(PaymentType.class, PaymentTypeMapping.getInstance(), "PAYMENT_ORDER_TYPES", 99999);
    }

    @Override
    ElementsService<PaymentType> service() {
	return service;
    }
}
