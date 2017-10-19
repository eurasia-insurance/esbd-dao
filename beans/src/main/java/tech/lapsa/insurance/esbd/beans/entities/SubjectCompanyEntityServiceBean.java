package tech.lapsa.insurance.esbd.beans.entities;

import javax.ejb.Stateless;
import javax.inject.Inject;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.insurance.esbd.NotFound;
import tech.lapsa.insurance.esbd.dict.CompanyActivityKindEntityService;
import tech.lapsa.insurance.esbd.entities.SubjectCompanyEntity;
import tech.lapsa.insurance.esbd.entities.SubjectCompanyEntityService;
import tech.lapsa.insurance.esbd.entities.SubjectEntity;
import tech.lapsa.insurance.esbd.entities.SubjectPersonEntity;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless
public class SubjectCompanyEntityServiceBean extends ASubjectEntityService implements SubjectCompanyEntityService {

    @Inject
    private CompanyActivityKindEntityService companyActivityKindService;

    @Override
    public SubjectCompanyEntity getById(Integer id) throws NotFound {
	MyNumbers.requireNonZero(id, "id");
	try (Connection con = pool.getConnection()) {
	    Client source = con.getClientByID(id.intValue());
	    if (source == null)
		throw new NotFound(SubjectCompanyEntity.class.getSimpleName() + " not found with ID = '" + id + "'");
	    boolean isNotPerson = source.getNaturalPersonBool() == 0;
	    if (!isNotPerson)
		throw new NotFound(SubjectCompanyEntity.class.getSimpleName() + " not found with ID = '" + id
			+ "'. It was a " + SubjectPersonEntity.class.getName());
	    return convert(source);
	}
    }

    @Override
    public SubjectCompanyEntity getByBIN(String bin) throws NotFound {
	MyStrings.requireNonEmpty(bin, "bin");
	Client source = fetchClientByIdNumber(bin, false, true);
	if (source == null)
	    throw new NotFound(
		    SubjectCompanyEntity.class.getSimpleName() + " not found with 'bin' = '" + bin + "'");
	boolean isNotPerson = source.getNaturalPersonBool() == 0;
	if (!isNotPerson)
	    throw new NotFound(SubjectCompanyEntity.class.getSimpleName() + " not found with 'bin' = '" + bin
		    + "'. It was a " + SubjectPersonEntity.class.getName());
	return convert(source);
    }

    protected SubjectCompanyEntity convert(Client source) {
	SubjectCompanyEntity target = new SubjectCompanyEntity();
	fillValues(source, target);
	return target;
    }

    protected void fillValues(Client source, SubjectCompanyEntity target) {
	super.fillValues(source, (SubjectEntity) target);

	// Juridical_Person_Name s:string Наименование (для юр. лица)
	target.setCompanyName(source.getJuridicalPersonName());

	// MAIN_CHIEF s:string Первый руководитель
	target.setHeadName(source.getMAINCHIEF());

	// MAIN_ACCOUNTANT s:string Главный бухгалтер
	target.setAccountantName(source.getMAINACCOUNTANT());

	// ACTIVITY_KIND_ID s:int Вид деятельности (справочник ACTIVITY_KINDS)
	target.setCompanyActivityKindId(new Long(source.getACTIVITYKINDID()));
	// non mandatory field
	if (MyNumbers.nonZero(source.getACTIVITYKINDID()))
	    target.setCompanyActivityKind(
		    companyActivityKindService.optionalById(source.getACTIVITYKINDID()).orElse(null));
    }
}
