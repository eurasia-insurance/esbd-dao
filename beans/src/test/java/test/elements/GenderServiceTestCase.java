package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.mapping.SexMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceLocal;

public class GenderServiceTestCase extends AMappedElementIntTestCase<Sex> {

    @Inject
    private GenderServiceLocal service;

    public GenderServiceTestCase() {
	super(Sex.class, SexMapping.getInstance(), "SEX", 99999);
    }

    @Override
    ElementsService<Sex> service() {
	return service;
    }
}
