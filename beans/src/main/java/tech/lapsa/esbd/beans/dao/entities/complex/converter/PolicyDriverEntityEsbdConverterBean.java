package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.InsuranceClassType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.entities.complex.Util;
import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService.MaritalStatusServiceLocal;
import tech.lapsa.esbd.dao.elements.nondict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.InsuredDriverEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.complex.UserEntity;
import tech.lapsa.esbd.dao.entities.complex.InsuredDriverEntity.InsuredDriverEntityBuilder;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.embeded.DriverLicenseInfo;
import tech.lapsa.esbd.dao.entities.embeded.GPWParticipantInfo;
import tech.lapsa.esbd.dao.entities.embeded.HandicappedInfo;
import tech.lapsa.esbd.dao.entities.embeded.PensionerInfo;
import tech.lapsa.esbd.dao.entities.embeded.PrivilegerInfo;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo;
import tech.lapsa.esbd.jaxws.wsimport.Driver;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class PolicyDriverEntityEsbdConverterBean implements AEsbdAttributeConverter<InsuredDriverEntity, Driver> {

    @EJB
    private SubjectPersonEntityServiceLocal subjectPersonService;

    @EJB
    private MaritalStatusServiceLocal maritalStatusService;

    @EJB
    private InsuredAgeAndExpirienceClassServiceLocal driverExpirienceClassificationService;

    @EJB
    private InsuranceClassTypeServiceLocal insuranceClassTypeService;

    @EJB
    private UserEntityServiceLocal userService;

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @Override
    public Driver convertToEsbdValue(InsuredDriverEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public InsuredDriverEntity convertToEntityAttribute(Driver source) throws EsbdConversionException {
	try {

	    final InsuredDriverEntityBuilder builder = InsuredDriverEntity.builder();

	    final int id = source.getDRIVERID();

	    {
		// DRIVER_ID s:int Идентификатор водителя
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// POLICY_ID s:int Идентификатор полиса
	    }

	    {
		// CLIENT_ID s:int Идентификатор клиента (обязательно)
		builder.withInsuredPerson(Util.reqField(InsuredDriverEntity.class,
			id,
			subjectPersonService::getById,
			"insuredPerson",
			SubjectPersonEntity.class,
			source.getCLIENTID()));
	    }

	    {
		// HOUSEHOLD_POSITION_ID s:int Идентификатор семейного положения
		builder.withMaritalStatus(Util.reqField(InsuredDriverEntity.class,
			id,
			maritalStatusService::getById,
			"maritalStatus",
			MaritalStatus.class,
			source.getHOUSEHOLDPOSITIONID()));
	    }

	    {
		// AGE_EXPERIENCE_ID s:int Идентификатор возраста\стажа вождения
		builder.withInsuredAgeExpirienceClass(Util.reqField(InsuredDriverEntity.class,
			id,
			driverExpirienceClassificationService::getById,
			"insuredAgeExpirienceClass",
			InsuredAgeAndExpirienceClass.class,
			source.getAGEEXPERIENCEID()));
	    }

	    {
		// EXPERIENCE s:int Стаж вождения
		builder.withDrivingExpirience(source.getEXPERIENCE());
	    }

	    {
		// DRIVER_CERTIFICATE s:string Номер водительского удостоверения
		// DRIVER_CERTIFICATE_DATE s:string Дата выдачи водительского
		// удостоверения
		DriverLicenseInfo.builder() //
			.withDateOfIssue(dateToLocalDate(source.getDRIVERCERTIFICATEDATE())) //
			.withNumber(source.getDRIVERCERTIFICATE()) //
			.buildTo(builder::withDriverLicense);
	    }

	    {
		// getClassId
		builder.withInsuraceClassType(Util.reqField(InsuredDriverEntity.class,
			id,
			insuranceClassTypeService::getById,
			"insuraceClassType",
			InsuranceClassType.class,
			source.getClassId()));
	    }

	    {
		// PRIVELEGER_BOOL s:int Признак приравненного лица
		// PRIVELEDGER_TYPE s:string Тип приравненного лица
		// PRIVELEDGER_CERTIFICATE s:string Удостоверение приравненного
		// лица
		// PRIVELEDGER_CERTIFICATE_DATE s:string Дата выдачи
		// удостоверения
		// приравненного лица
		final boolean privileger = source.getPRIVELEGERBOOL() == 1;
		if (privileger)
		    PrivilegerInfo.builder() //
			    .withType(source.getPRIVELEDGERTYPE())
			    .withCertificateNumber(source.getPRIVELEDGERCERTIFICATE()) //
			    .withCertificateDateOfIssue(
				    dateToLocalDate(source.getPRIVELEDGERCERTIFICATEDATE())) //
			    .buildTo(builder::withPrivilegerInfo);
	    }

	    {
		// WOW_BOOL s:int Признак участника ВОВ
		// WOW_CERTIFICATE s:string Удостоверение участника ВОВ
		// WOW_CERTIFICATE_DATE s:string Дата выдачи удостоверения
		// участника
		// ВОВ
		final boolean gpwParticipant = source.getWOWBOOL() == 1;
		if (gpwParticipant)
		    GPWParticipantInfo.builder() //
			    .withCertificateDateOfIssue(dateToLocalDate(source.getWOWCERTIFICATEDATE())) //
			    .withCertificateNumber(source.getWOWCERTIFICATE()) //
			    .buildTo(builder::withGpwParticipantInfo);
	    }

	    {
		// PENSIONER_BOOL s:int Признак пенсионера
		// PENSIONER_CERTIFICATE s:string Удостоверение пенсионера
		// PENSIONER_CERTIFICATE_DATE s:string Дата выдачи удостоверения
		// пенсионера
		final boolean pensioner = source.getPENSIONERBOOL() == 1;
		if (pensioner)
		    PensionerInfo.builder() //
			    .withCertificateNumber(source.getPENSIONERCERTIFICATE()) //
			    .withCertiticateDateOfIssue(
				    dateToLocalDate(source.getPENSIONERCERTIFICATEDATE())) //
			    .buildTo(builder::withPensionerInfo);
	    }

	    {
		// INVALID_BOOL s:int Признак инвалида
		// INVALID_CERTIFICATE s:string Удостоверение инвалида
		// INVALID_CERTIFICATE_BEG_DATE s:string Дата выдачи
		// удостоверения
		// инвалида
		// INVALID_CERTIFICATE_END_DATE s:string Дата завершения
		// удостоверения
		// инвалида
		final boolean handicapped = source.getINVALIDBOOL() == 1;
		if (handicapped)
		    HandicappedInfo.builder() //
			    .withCertificateNumber(source.getINVALIDCERTIFICATE()) //
			    .withCertificateValidFrom(dateToLocalDate(source.getINVALIDCERTIFICATEBEGDATE())) //
			    .withCertificateValidTill(dateToLocalDate(source.getINVALIDCERTIFICATEENDDATE())) //
			    .buildTo(builder::withHandicappedInfo);
	    }

	    {
		// CREATED_BY_USER_ID s:int Идентификатор пользователя,
		// создавшего
		// запись
		// INPUT_DATE s:string Дата\время ввода записи в систему
		RecordOperationInfo.builder()
			.withInstant(optTemporalToInstant(source.getINPUTDATE()).orElse(null))
			.withAuthor(Util.reqField(InsuredDriverEntity.class,
				id,
				userService::getById,
				"created.author",
				UserEntity.class,
				source.getCREATEDBYUSERID()))
			.buildTo(builder::withCreated);
	    }

	    {
		// RECORD_CHANGED_AT s:string Дата\время изменения записи
		// CHANGED_BY_USER_ID s:int Идентификатор пользователя,
		// изменившего
		// запись
		RecordOperationInfo.builder()
			.withInstant(optTemporalToInstant(source.getRECORDCHANGEDAT()).orElse(null))
			.withAuthor(Util.reqField(InsuredDriverEntity.class,
				id,
				userService::getById,
				"modified.author",
				UserEntity.class,
				source.getCHANGEDBYUSERID()))
			.buildTo(builder::withModified);
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
		builder.withInsurer(Util.reqField(InsuredDriverEntity.class,
			id,
			insuranceCompanyService::getById,
			"insurer",
			InsuranceCompanyEntity.class,
			source.getSYSTEMDELIMITERID()));
	    }
	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
