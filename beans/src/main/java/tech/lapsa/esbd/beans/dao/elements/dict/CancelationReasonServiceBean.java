package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.CancelationReason;

import tech.lapsa.esbd.beans.dao.elements.mapping.CancelationReasonMapping;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService.CancelationReasonServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService.CancelationReasonServiceRemote;

@Singleton(name = CancelationReasonService.BEAN_NAME)
public class CancelationReasonServiceBean
	extends ADictElementsService<CancelationReason>
	implements CancelationReasonServiceLocal, CancelationReasonServiceRemote {

    // constructor

    protected CancelationReasonServiceBean() {
	super(CancelationReasonService.class, CancelationReason.class, CancelationReasonMapping.getInstance()::forId);
    }
}
