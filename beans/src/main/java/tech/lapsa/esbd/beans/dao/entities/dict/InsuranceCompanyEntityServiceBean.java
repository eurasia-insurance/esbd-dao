package tech.lapsa.esbd.beans.dao.entities.dict;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import tech.lapsa.esbd.beans.dao.ADictEntitiesService;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity.InsuranceCompanyEntityBuilder;

@Singleton(name = InsuranceCompanyEntityService.BEAN_NAME)
@Startup
public class InsuranceCompanyEntityServiceBean
	extends ADictEntitiesService<InsuranceCompanyEntity, InsuranceCompanyEntityBuilder>
	implements InsuranceCompanyEntityServiceLocal, InsuranceCompanyEntityServiceRemote {

    // static finals

    private static final String DICT_NAME = "INSURANCE_COMPANIES";

    // constructor

    public InsuranceCompanyEntityServiceBean() {
	super(null, InsuranceCompanyEntity.class, DICT_NAME, InsuranceCompanyEntity::builder);
    }
}
