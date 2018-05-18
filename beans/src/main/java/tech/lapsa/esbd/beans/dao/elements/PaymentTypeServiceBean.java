package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.PaymentType;

import tech.lapsa.esbd.beans.dao.elements.mapping.PaymentTypeMapping;
import tech.lapsa.esbd.dao.elements.PaymentTypeService;
import tech.lapsa.esbd.dao.elements.PaymentTypeService.PaymentTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.PaymentTypeService.PaymentTypeServiceRemote;

@Singleton(name = PaymentTypeService.BEAN_NAME)
public class PaymentTypeServiceBean
	extends AElementsService<PaymentType>
	implements PaymentTypeServiceLocal, PaymentTypeServiceRemote {

    public PaymentTypeServiceBean() {
	super(PaymentTypeService.class, PaymentTypeMapping.getInstance()::forId, PaymentType.class);
    }
}
