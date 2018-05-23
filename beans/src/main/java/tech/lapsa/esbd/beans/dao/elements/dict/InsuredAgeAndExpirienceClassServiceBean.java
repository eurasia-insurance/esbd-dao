package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

import tech.lapsa.esbd.beans.dao.ADictElementsService;
import tech.lapsa.esbd.beans.dao.elements.dict.mapping.InsuredAgeAndExpirienceClassMapping;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceRemote;

@Singleton(name = InsuredAgeAndExpirienceClassService.BEAN_NAME)
public class InsuredAgeAndExpirienceClassServiceBean
	extends ADictElementsService<InsuredAgeAndExpirienceClass>
	implements InsuredAgeAndExpirienceClassServiceLocal, InsuredAgeAndExpirienceClassServiceRemote {

    // constructor

    protected InsuredAgeAndExpirienceClassServiceBean() {
	super(InsuredAgeAndExpirienceClassService.class, InsuredAgeAndExpirienceClass.class,
		InsuredAgeAndExpirienceClassMapping.getInstance()::forId);
    }
}
