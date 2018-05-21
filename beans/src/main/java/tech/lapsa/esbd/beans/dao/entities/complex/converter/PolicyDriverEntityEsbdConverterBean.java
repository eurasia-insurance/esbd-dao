package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.InsuranceClassType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.MaritalStatus;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
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
import tech.lapsa.esbd.dao.entities.embeded.DriverLicenseInfo.DriverLicenseInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.GPWParticipantInfo;
import tech.lapsa.esbd.dao.entities.embeded.GPWParticipantInfo.GPWParticipantInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.HandicappedInfo;
import tech.lapsa.esbd.dao.entities.embeded.HandicappedInfo.HandicappedInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.PensionerInfo;
import tech.lapsa.esbd.dao.entities.embeded.PensionerInfo.PensionerInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.PrivilegerInfo;
import tech.lapsa.esbd.dao.entities.embeded.PrivilegerInfo.PrivilegerInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo.RecordOperationInfoBuilder;
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
		final DriverLicenseInfoBuilder b1 = DriverLicenseInfo.builder();

		MyOptionals.of(source.getDRIVERCERTIFICATEDATE())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(b1::withDateOfIssue);

		MyOptionals.of(source.getDRIVERCERTIFICATE())
			.ifPresent(b1::withNumber);

		b1.buildTo(builder::withDriverLicense);
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
		if (source.getPRIVELEGERBOOL() == 1) {
		    // PRIVELEDGER_TYPE s:string Тип приравненного лица
		    // PRIVELEDGER_CERTIFICATE s:string Удостоверение
		    // приравненного лица
		    // PRIVELEDGER_CERTIFICATE_DATE s:string Дата выдачи
		    // удостоверения приравненного лица
		    final PrivilegerInfoBuilder b1 = PrivilegerInfo.builder();

		    MyOptionals.of(source.getPRIVELEDGERTYPE())
			    .ifPresent(b1::withType);

		    MyOptionals.of(source.getPRIVELEDGERCERTIFICATE())
			    .ifPresent(b1::withCertificateNumber);

		    MyOptionals.of(source.getPRIVELEDGERCERTIFICATEDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withCertificateDateOfIssue);

		    b1.buildTo(builder::withPrivilegerInfo);
		}
	    }

	    {
		// WOW_BOOL s:int Признак участника ВОВ
		if (source.getWOWBOOL() == 1) {
		    // WOW_CERTIFICATE s:string Удостоверение участника ВОВ
		    // WOW_CERTIFICATE_DATE s:string Дата выдачи удостоверения
		    // участника ВОВ
		    final GPWParticipantInfoBuilder b1 = GPWParticipantInfo.builder();

		    MyOptionals.of(source.getWOWCERTIFICATE())
			    .ifPresent(b1::withCertificateNumber);

		    MyOptionals.of(source.getWOWCERTIFICATEDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withCertificateDateOfIssue);

		    b1.buildTo(builder::withGpwParticipantInfo);
		}
	    }

	    {
		// PENSIONER_BOOL s:int Признак пенсионера
		if (source.getPENSIONERBOOL() == 1) {
		    // PENSIONER_CERTIFICATE s:string Удостоверение пенсионера
		    // PENSIONER_CERTIFICATE_DATE s:string Дата выдачи
		    // удостоверения пенсионера
		    final PensionerInfoBuilder b1 = PensionerInfo.builder();

		    MyOptionals.of(source.getPENSIONERCERTIFICATE())
			    .ifPresent(b1::withCertificateNumber);

		    MyOptionals.of(source.getPENSIONERCERTIFICATEDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withCertiticateDateOfIssue);

		    b1.buildTo(builder::withPensionerInfo);
		}
	    }

	    {
		// INVALID_BOOL s:int Признак инвалида
		if (source.getINVALIDBOOL() == 1) {
		    // INVALID_CERTIFICATE s:string Удостоверение инвалида
		    // INVALID_CERTIFICATE_BEG_DATE s:string Дата выдачи
		    // удостоверения инвалида
		    // INVALID_CERTIFICATE_END_DATE s:string Дата завершения
		    // удостоверения инвалида
		    final HandicappedInfoBuilder b1 = HandicappedInfo.builder();

		    MyOptionals.of(source.getINVALIDCERTIFICATE())
			    .ifPresent(b1::withCertificateNumber);

		    MyOptionals.of(source.getINVALIDCERTIFICATEBEGDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withCertificateValidFrom);

		    MyOptionals.of(source.getINVALIDCERTIFICATEENDDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withCertificateValidTill);

		    b1.buildTo(builder::withHandicappedInfo);
		}
	    }

	    {
		// CREATED_BY_USER_ID s:int Идентификатор пользователя,
		// создавшего
		// запись
		// INPUT_DATE s:string Дата\время ввода записи в систему
		final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		MyOptionals.of(source.getINPUTDATE())
			.flatMap(TemporalUtil::optTemporalToInstant)
			.ifPresent(b1::withInstant);

		optField(PolicyDriverEntity.class,
			id,
			userService::getById,
			"created.author",
			UserEntity.class,
			MyOptionals.of(source.getCREATEDBYUSERID()))
				.ifPresent(b1::withAuthor);

		b1.buildTo(builder::withCreated);
	    }

	    {
		// RECORD_CHANGED_AT s:string Дата\время изменения записи
		// CHANGED_BY_USER_ID s:int Идентификатор пользователя,
		// изменившего
		// запись
		final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		MyOptionals.of(source.getRECORDCHANGEDAT())
			.flatMap(TemporalUtil::optTemporalToInstant)
			.ifPresent(b1::withInstant);

		optField(PolicyDriverEntity.class,
			id,
			userService::getById,
			"modified.author",
			UserEntity.class,
			MyOptionals.of(source.getCHANGEDBYUSERID()))
				.ifPresent(b1::withAuthor);

		b1.buildTo(builder::withModified);
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
