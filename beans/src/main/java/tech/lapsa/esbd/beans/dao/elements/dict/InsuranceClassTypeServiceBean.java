package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.ADictElementsService;
import tech.lapsa.esbd.beans.dao.elements.dict.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService.InsuranceClassTypeServiceRemote;

@Singleton(name = InsuranceClassTypeService.BEAN_NAME)
public class InsuranceClassTypeServiceBean
	extends ADictElementsService<InsuranceClassType>
	implements InsuranceClassTypeServiceLocal, InsuranceClassTypeServiceRemote {

    // constructor

    protected InsuranceClassTypeServiceBean() {
	super(InsuranceClassTypeService.class, InsuranceClassType.class, InsuranceClassTypeMapping.getInstance()::forId);
    }
}
