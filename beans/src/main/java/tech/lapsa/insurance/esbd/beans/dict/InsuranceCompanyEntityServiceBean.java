package tech.lapsa.insurance.esbd.beans.dict;

import javax.ejb.Stateless;

import tech.lapsa.esbd.jaxws.wsimport.Item;
import tech.lapsa.insurance.esbd.dict.InsuranceCompanyEntity;
import tech.lapsa.insurance.esbd.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.insurance.esbd.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceRemote;

@Stateless
public class InsuranceCompanyEntityServiceBean extends ADictionaryEntityService<InsuranceCompanyEntity, Integer>
	implements InsuranceCompanyEntityServiceLocal, InsuranceCompanyEntityServiceRemote {

    private static final String DICT_NAME = "INSURANCE_COMPANIES";

    public InsuranceCompanyEntityServiceBean() {
	super(DICT_NAME, InsuranceCompanyEntityServiceBean::convert);
    }

    private static InsuranceCompanyEntity convert(Item source) {
	InsuranceCompanyEntity target = new InsuranceCompanyEntity();
	target.setId(source.getID());
	target.setCode(source.getCode());
	target.setName(source.getName());
	return target;
    }
}
