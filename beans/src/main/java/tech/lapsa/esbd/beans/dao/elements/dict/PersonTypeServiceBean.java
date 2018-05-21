package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.PersonTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService.PersonTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService.PersonTypeServiceRemote;

@Singleton(name = PersonTypeService.BEAN_NAME)
public class PersonTypeServiceBean
	extends ADictElementsService<PersonType>
	implements PersonTypeServiceLocal, PersonTypeServiceRemote {

    public PersonTypeServiceBean() {
	super(PersonTypeService.class, PersonTypeMapping.getInstance()::forId, PersonType.class);
    }
}
