package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.Util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity.SubjectCompanyEntityBuilder;
import tech.lapsa.esbd.domain.complex.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class SubjectCompanyEntityConverterBean
	extends ASubjectEntityEsdbdConverter
	implements AEsbdAttributeConverter<SubjectCompanyEntity, Client> {

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
		MyOptionals.of(source.getJuridicalPersonName())
			.ifPresent(builder::withCompanyName);
	    }

	    {
		// MAIN_CHIEF s:string Первый руководитель
		MyOptionals.of(source.getMAINCHIEF())
			.ifPresent(builder::withHeadName);
	    }

	    {
		// MAIN_ACCOUNTANT s:string Главный бухгалтер
		MyOptionals.of(source.getMAINACCOUNTANT())
			.ifPresent(builder::withAccountantName);
	    }

	    {
		// ACTIVITY_KIND_ID s:int Вид деятельности (справочник
		// ACTIVITY_KINDS)
		optFieldIgnoreFieldNotFound(SubjectCompanyEntity.class,
			id,
			companyActivityKinds::getById,
			"companyActivityKind",
			CompanyActivityKindEntity.class,
			MyOptionals.of(source.getACTIVITYKINDID()))
				.ifPresent(builder::withCompanyActivityKind);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}

    }

}
