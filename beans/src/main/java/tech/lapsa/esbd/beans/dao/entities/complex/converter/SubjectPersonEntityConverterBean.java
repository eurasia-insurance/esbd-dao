package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.entities.complex.Util;
import tech.lapsa.esbd.dao.elements.dict.GenderService.GenderServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService.IdentityCardTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity.SubjectPersonEntityBuilder;
import tech.lapsa.esbd.dao.entities.embeded.IdentityCardInfo;
import tech.lapsa.esbd.dao.entities.embeded.PersonalInfo;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class SubjectPersonEntityConverterBean
	extends ASubjectEntityEsdbdConverter
	implements AEsbdAttributeConverter<SubjectPersonEntity, Client> {

    @EJB
    private GenderServiceLocal genders;

    @EJB
    private IdentityCardTypeServiceLocal identityCardTypes;

    @Override
    public Client convertToEsbdValue(SubjectPersonEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SubjectPersonEntity convertToEntityAttribute(Client source) throws EsbdConversionException {
	if (source.getNaturalPersonBool() != 1)
	    throw MyExceptions.format(EsbdConversionException::new, "Client is not a natural person");
	final SubjectPersonEntityBuilder builder = SubjectPersonEntity.builder();

	try {
	    final int id = source.getID();

	    fillValues(source, (SubjectEntityBuilder<?, ?>) builder);

	    {
		// First_Name s:string Имя (для физ. лица)
		// Last_Name s:string Фамилия (для физ. лица)
		// Middle_Name s:string Отчество (для физ. лица)
		// Born s:string Дата рождения
		// Sex_ID s:int Пол (справочник SEX)
		PersonalInfo.builder()
			.withName(source.getFirstName())
			.withSurename(source.getLastName())
			.withPatronymic(source.getMiddleName())
			.withDayOfBirth(dateToLocalDate(source.getBorn()))
			.withGender(Util.optField(SubjectPersonEntity.class,
				id,
				genders::getById,
				"personal.gender",
				Sex.class,
				MyOptionals.of(source.getSexID())))
			.buildTo(builder::withPersonal);
	    }

	    // DOCUMENT_TYPE_ID s:int Тип документа (справочник DOCUMENTS_TYPES)
	    // DOCUMENT_NUMBER s:string Номер документа
	    // DOCUMENT_GIVED_BY s:string Документ выдан
	    // DOCUMENT_GIVED_DATE s:string Дата выдачи документа
	    IdentityCardInfo.builder() //
		    .withNumber(MyOptionals.of(source.getDOCUMENTNUMBER()))
		    .withDateOfIssue(MyOptionals.of(dateToLocalDate(source.getDOCUMENTGIVEDDATE())))
		    .withIssuingAuthority(source.getDOCUMENTGIVEDBY()) //
		    .withIdentityCardType(Util.optField(SubjectPersonEntity.class,
			    id,
			    identityCardTypes::getById,
			    "identityCard.identityCardType",
			    IdentityCardType.class,
			    MyOptionals.of(source.getDOCUMENTTYPEID())))
		    .buildTo(builder::withIdentityCard);

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
