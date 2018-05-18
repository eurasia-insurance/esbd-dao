package tech.lapsa.esbd.beans.dao.dict;

import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceRemote;

@Stateless(name = CompanyActivityKindEntityService.BEAN_NAME)
public class CompanyActivityKindEntityServiceBean
	extends ADictionaryEntityService<CompanyActivityKindEntity>
	implements CompanyActivityKindEntityServiceLocal, CompanyActivityKindEntityServiceRemote {

    private static final String DICT_NAME = "ACTIVITY_KINDS";

    public CompanyActivityKindEntityServiceBean() {
	super(CompanyActivityKindEntityService.class, DICT_NAME, CompanyActivityKindEntity::builder);
    }
}
