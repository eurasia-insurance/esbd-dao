package tech.lapsa.esbd.beans.dao.entities.converter;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntity;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntity.SubjectCompanyEntityBuilder;
import tech.lapsa.esbd.dao.entities.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class SubjectCompanyEntityConverter
	extends ASubjectEntityEsdbdConverter
	implements EsbdAttributeConverter<SubjectCompanyEntity, Client> {

    @EJB
    private CompanyActivityKindEntityServiceLocal companyActivityKinds;

    @Override
    public Client convertToEsbdValue(SubjectCompanyEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SubjectCompanyEntity convertToEntityAttribute(Client source) throws EsbdConversionException {
	if (source.getNaturalPersonBool() != 0)
	    throw MyExceptions.format(EsbdConversionException::new, "Client is not a legal person");
	final SubjectCompanyEntityBuilder builder = SubjectCompanyEntity.builder();

	try {
	    final int id = source.getID();

	    fillValues(source, (SubjectEntityBuilder<?, ?>) builder);

	    {
		// Juridical_Person_Name s:string Наименование (для юр. лица)
		builder.withCompanyName(source.getJuridicalPersonName());
	    }

	    {
		// MAIN_CHIEF s:string Первый руководитель
		builder.withHeadName(source.getMAINCHIEF());
	    }

	    {
		// MAIN_ACCOUNTANT s:string Главный бухгалтер
		builder.withAccountantName(source.getMAINACCOUNTANT());
	    }

	    {
		// ACTIVITY_KIND_ID s:int Вид деятельности (справочник
		// ACTIVITY_KINDS)
		builder.withCompanyActivityKind(Util.optFieldIgnoreFieldNotFound(SubjectCompanyEntity.class,
			id,
			companyActivityKinds::getById,
			"companyActivityKind",
			CompanyActivityKindEntity.class,
			MyOptionals.of(source.getACTIVITYKINDID())));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}

    }

}
