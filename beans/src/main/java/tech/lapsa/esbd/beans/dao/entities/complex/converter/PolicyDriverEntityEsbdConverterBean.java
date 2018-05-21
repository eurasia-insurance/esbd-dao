package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;
import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.InsuranceClassType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.dao.elements.dict.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.MaritalStatusService.MaritalStatusServiceLocal;
import tech.lapsa.esbd.dao.elements.nondict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyDriverEntity;
import tech.lapsa.esbd.dao.entities.complex.PolicyDriverEntity.PolicyDriverEntityBuilder;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntity;
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
public class PolicyDriverEntityEsbdConverterBean implements AEsbdAttributeConverter<PolicyDriverEntity, Driver> {

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
    public Driver convertToEsbdValue(PolicyDriverEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PolicyDriverEntity convertToEntityAttribute(Driver source) throws EsbdConversionException {
	try {

	    final PolicyDriverEntityBuilder builder = PolicyDriverEntity.builder();

	    final int id = source.getDRIVERID();

	    {
		// DRIVER_ID s:int Идентификатор водителя
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// POLICY_ID s:int Идентификатор полиса
	    }

	    {
		// CLIENT_ID s:int Идентификатор клиента (обязательно)
		optField(PolicyDriverEntity.class,
			id,
			subjectPersonService::getById,
			"insuredPerson",
			SubjectPersonEntity.class,
			MyOptionals.of(source.getCLIENTID()))
				.ifPresent(builder::withInsuredPerson);
	    }

	    {
		// HOUSEHOLD_POSITION_ID s:int Идентификатор семейного положения
		optField(PolicyDriverEntity.class,
			id,
			maritalStatusService::getById,
			"maritalStatus",
			MaritalStatus.class,
			MyOptionals.of(source.getHOUSEHOLDPOSITIONID()))
				.ifPresent(builder::withMaritalStatus);
	    }

	    {
		// AGE_EXPERIENCE_ID s:int Идентификатор возраста\стажа вождения
		optField(PolicyDriverEntity.class,
			id,
			driverExpirienceClassificationService::getById,
			"insuredAgeExpirienceClass",
			InsuredAgeAndExpirienceClass.class,
			MyOptionals.of(source.getAGEEXPERIENCEID()))
				.ifPresent(builder::withInsuredAgeExpirienceClass);
	    }

	    {
		// EXPERIENCE s:int Стаж вождения
		MyOptionals.of(source.getEXPERIENCE())
			.ifPresent(builder::withDrivingExpirience);
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
		optField(PolicyDriverEntity.class,
			id,
			insuranceClassTypeService::getById,
			"insuraceClassType",
			InsuranceClassType.class,
			MyOptionals.of(source.getClassId()))
				.ifPresent(builder::withInsuraceClassType);
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
			.withAuthor(reqField(PolicyDriverEntity.class,
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
			.withAuthor(reqField(PolicyDriverEntity.class,
				id,
				userService::getById,
				"modified.author",
				UserEntity.class,
				source.getCHANGEDBYUSERID()))
			.buildTo(builder::withModified);
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
		optField(PolicyDriverEntity.class,
			id,
			insuranceCompanyService::getById,
			"insurer",
			InsuranceCompanyEntity.class,
			MyOptionals.of(source.getSYSTEMDELIMITERID()))
				.ifPresent(builder::withInsurer);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
