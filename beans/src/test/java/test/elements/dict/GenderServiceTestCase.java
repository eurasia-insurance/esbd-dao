package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.mapping.SexMapping;
import tech.lapsa.esbd.dao.elements.dict.GenderService;
import tech.lapsa.esbd.dao.elements.dict.GenderService.GenderServiceLocal;

public class GenderServiceTestCase extends AMappedElementDictionaryTestCase<Sex> {

    @Inject
    private GenderServiceLocal service;

    public GenderServiceTestCase() {
	super(Sex.class, SexMapping.getInstance(), "SEX", 99999);
    }

    @Override
    protected GenderService service() {
	return service;
    }
}
