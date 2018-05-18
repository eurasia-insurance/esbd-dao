package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.elements.mapping.PersonTypeMapping;
import tech.lapsa.esbd.dao.elements.PersonTypeService;
import tech.lapsa.esbd.dao.elements.PersonTypeService.PersonTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.PersonTypeService.PersonTypeServiceRemote;

@Singleton(name = PersonTypeService.BEAN_NAME)
public class PersonTypeServiceBean
	extends AElementsService<PersonType>
	implements PersonTypeServiceLocal, PersonTypeServiceRemote {

    public PersonTypeServiceBean() {
	super(PersonTypeService.class, PersonTypeMapping.getInstance()::forId, PersonType.class);
    }
}
