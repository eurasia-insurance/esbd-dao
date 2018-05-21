package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.PaymentType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.PaymentTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.PaymentTypeService;
import tech.lapsa.esbd.dao.elements.dict.PaymentTypeService.PaymentTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.PaymentTypeService.PaymentTypeServiceRemote;

@Singleton(name = PaymentTypeService.BEAN_NAME)
public class PaymentTypeServiceBean
	extends ADictElementsService<PaymentType>
	implements PaymentTypeServiceLocal, PaymentTypeServiceRemote {

    public PaymentTypeServiceBean() {
	super(PaymentTypeService.class, PaymentTypeMapping.getInstance()::forId, PaymentType.class);
    }
}
