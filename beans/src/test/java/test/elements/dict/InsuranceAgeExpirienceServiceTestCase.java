package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.InsuredAgeAndExpirienceClassMapping;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;

public class InsuranceAgeExpirienceServiceTestCase
	extends AMappedElementDictionaryTestCase<InsuredAgeAndExpirienceClass> {

    @Inject
    private InsuredAgeAndExpirienceClassServiceLocal service;

    public InsuranceAgeExpirienceServiceTestCase() {
	super(InsuredAgeAndExpirienceClass.class, InsuredAgeAndExpirienceClassMapping.getInstance(), "AGE_EXPERIENCE",
		99999);
    }

    @Override
    protected InsuredAgeAndExpirienceClassService service() {
	return service;
    }
}
