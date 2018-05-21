package tech.lapsa.esbd.beans.dao.entities.dict;

import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;

@Stateless(name = InsuranceCompanyEntityService.BEAN_NAME)
public class InsuranceCompanyEntityServiceBean
	extends ADictionaryEntityService<InsuranceCompanyEntity>
	implements InsuranceCompanyEntityServiceLocal, InsuranceCompanyEntityServiceRemote {

    private static final String DICT_NAME = "INSURANCE_COMPANIES";

    public InsuranceCompanyEntityServiceBean() {
	super(InsuranceCompanyEntityService.class, DICT_NAME, InsuranceCompanyEntity::builder);
    }
}
