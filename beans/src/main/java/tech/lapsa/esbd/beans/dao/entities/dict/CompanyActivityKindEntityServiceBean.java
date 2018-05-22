package tech.lapsa.esbd.beans.dao.entities.dict;

import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService;
import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity.CompanyActivityKindEntityBuilder;

@Stateless(name = CompanyActivityKindEntityService.BEAN_NAME)
public class CompanyActivityKindEntityServiceBean
	extends ADictionaryEntityService<CompanyActivityKindEntity, CompanyActivityKindEntityBuilder>
	implements CompanyActivityKindEntityServiceLocal, CompanyActivityKindEntityServiceRemote {

    private static final String DICT_NAME = "ACTIVITY_KINDS";

    public CompanyActivityKindEntityServiceBean() {
	super(CompanyActivityKindEntityService.class, DICT_NAME, CompanyActivityKindEntity::builder);
    }
}
