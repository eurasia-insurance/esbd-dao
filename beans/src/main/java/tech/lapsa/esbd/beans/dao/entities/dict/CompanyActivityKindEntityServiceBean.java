package tech.lapsa.esbd.beans.dao.entities.dict;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import tech.lapsa.esbd.beans.dao.ADictEntitiesService;
import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService;
import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity.CompanyActivityKindEntityBuilder;

@Singleton(name = CompanyActivityKindEntityService.BEAN_NAME)
@Startup
public class CompanyActivityKindEntityServiceBean
	extends ADictEntitiesService<CompanyActivityKindEntity, CompanyActivityKindEntityBuilder>
	implements CompanyActivityKindEntityServiceLocal, CompanyActivityKindEntityServiceRemote {

    // static finals

    private static final String DICT_NAME = "ACTIVITY_KINDS";

    // constructor

    public CompanyActivityKindEntityServiceBean() {
	super(null, CompanyActivityKindEntity.class, DICT_NAME, CompanyActivityKindEntity::builder);
    }
}
