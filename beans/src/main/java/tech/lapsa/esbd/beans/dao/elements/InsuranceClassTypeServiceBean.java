package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.elements.dict.ADictElementsService;
import tech.lapsa.esbd.beans.dao.elements.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService.InsuranceClassTypeServiceRemote;

@Singleton(name = InsuranceClassTypeService.BEAN_NAME)
public class InsuranceClassTypeServiceBean
	extends ADictElementsService<InsuranceClassType>
	implements InsuranceClassTypeServiceLocal, InsuranceClassTypeServiceRemote {

    // constructor

    protected InsuranceClassTypeServiceBean() {
	super(InsuranceClassTypeService.class, InsuranceClassType.class, InsuranceClassTypeMapping.getInstance()::forId);
    }
}
