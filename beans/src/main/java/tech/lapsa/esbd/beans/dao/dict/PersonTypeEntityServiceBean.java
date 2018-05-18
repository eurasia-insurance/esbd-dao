package tech.lapsa.esbd.beans.dao.dict;

import javax.ejb.Singleton;

import tech.lapsa.esbd.dao.dict.PersonTypeEntity;
import tech.lapsa.esbd.dao.dict.PersonTypeEntityService;
import tech.lapsa.esbd.dao.dict.PersonTypeEntityService.PersonTypeEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.PersonTypeEntityService.PersonTypeEntityServiceRemote;

@Singleton(name = PersonTypeEntityService.BEAN_NAME)
public class PersonTypeEntityServiceBean
	extends ADictionaryEntityService<PersonTypeEntity>
	implements PersonTypeEntityServiceLocal, PersonTypeEntityServiceRemote {

    private static final String DICT_NAME = "CLIENT_FORMS";

    public PersonTypeEntityServiceBean() {
	super(PersonTypeEntityService.class, DICT_NAME, PersonTypeEntity::builder);
    }
}
