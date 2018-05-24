package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.PersonTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService.PersonTypeServiceLocal;

public class PersonTypeServiceTestCase extends AMappedElementDictionaryTestCase<PersonType> {

    @Inject
    private PersonTypeServiceLocal service;

    public PersonTypeServiceTestCase() {
	super(PersonType.class, PersonTypeMapping.getInstance(), "CLIENT_FORMS", 99999);
    }

    @Override
    protected PersonTypeService service() {
	return service;
    }
}
