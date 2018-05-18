package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.InsuredAgeAndExpirienceClassMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;

public class InsuranceAgeExpirienceServiceTestCase extends AMappedElementTestCase<InsuredAgeAndExpirienceClass> {

    @Inject
    private InsuredAgeAndExpirienceClassServiceLocal service;

    public InsuranceAgeExpirienceServiceTestCase() {
	super(InsuredAgeAndExpirienceClass.class, InsuredAgeAndExpirienceClassMapping.getInstance(), "AGE_EXPERIENCE",
		99999);
    }

    @Override
    ElementsService<InsuredAgeAndExpirienceClass> service() {
	return service;
    }
}
