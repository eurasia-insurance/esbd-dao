package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.elements.mapping.PersonTypeMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.PersonTypeService.PersonTypeServiceLocal;

public class PersonTypeServiceTestCase extends AMappedElementIntTestCase<PersonType> {

    @Inject
    private PersonTypeServiceLocal service;

    public PersonTypeServiceTestCase() {
	super(PersonType.class, PersonTypeMapping.getInstance(), "CLIENT_FORMS", 99999);
    }

    @Override
    ElementsService<PersonType> service() {
	return service;
    }
}
