package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;
import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.entities.complex.Util;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService.KZAreaServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyVehicleEntity;
import tech.lapsa.esbd.dao.entities.complex.PolicyVehicleEntity.PolicyVehicleEntityBuilder;
import tech.lapsa.esbd.dao.entities.complex.UserEntity;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntity;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo;
import tech.lapsa.esbd.dao.entities.embeded.VehicleCertificateInfo;
import tech.lapsa.esbd.jaxws.wsimport.PoliciesTF;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.kz.vehicle.VehicleRegNumber;

@Stateless
@LocalBean
public class PolicyVehicleEntityEsbdConverterBean implements AEsbdAttributeConverter<PolicyVehicleEntity, PoliciesTF> {

    @EJB
    private VehicleEntityServiceLocal vehicleService;

    @EJB
    private VehicleClassServiceLocal vehicleClassService;

    @EJB
    private VehicleAgeClassServiceLocal vehicleAgeClassService;

    @EJB
    private KZAreaServiceLocal countryRegionService;

    @EJB
    private UserEntityServiceLocal userService;

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @Override
    public PoliciesTF convertToEsbdValue(PolicyVehicleEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PolicyVehicleEntity convertToEntityAttribute(PoliciesTF source) throws EsbdConversionException {
	try {

	    final PolicyVehicleEntityBuilder builder = PolicyVehicleEntity.builder();

	    final int id = source.getPOLICYTFID();

	    {
		// POLICY_TF_ID s:int Идентификатор ТС полиса
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// POLICY_ID s:int Идентификатор полиса
	    }

	    {
		// TF_ID s:int Идентификатор ТС
		builder.withVehicle(Util.reqField(PolicyVehicleEntity.class,
			id,
			vehicleService::getById,
			"vehicle",
			VehicleEntity.class,
			source.getTFID()));
	    }

	    {
		// TF_TYPE_ID s:int Идентификатор типа ТС (обязательно)
		builder.withVehicleClass(Util.reqField(PolicyVehicleEntity.class,
			id,
			vehicleClassService::getById,
			"vehicleClass",
			VehicleClass.class,
			source.getTFTYPEID()));
	    }

	    {
		// TF_AGE_ID s:int Идентификатор возраста ТС (обязательно)
		builder.withVehicleAgeClass(Util.reqField(PolicyVehicleEntity.class,
			id,
			vehicleAgeClassService::getById,
			"vehicleAgeClass",
			VehicleAgeClass.class,
			source.getTFAGEID()));
	    }

	    {
		// TF_NUMBER s:string Гос. номер ТС
		// TF_REGISTRATION_CERTIFICATE s:string Номер тех. паспорта
		// GIVE_DATE s:string Дата выдачи тех. паспорта
		// REGION_ID s:int Идентификатор региона регистрации ТС
		// (обязательно)
		// BIG_CITY_BOOL s:int Признак города областного значения
		// (обязательно)
		VehicleCertificateInfo.builder() //
			.withCertificateNumber(source.getTFREGISTRATIONCERTIFICATE())
			.withDateOfIssue(dateToLocalDate(source.getGIVEDATE()))
			.withRegistrationMajorCity(source.getBIGCITYBOOL() == 1)
			.withRegistrationRegion(Util.reqField(PolicyVehicleEntity.class,
				id,
				countryRegionService::getById,
				"certificate.registrationRegion",
				KZArea.class,
				source.getREGIONID()))
			.withRegistrationNumber(VehicleRegNumber.assertValid(source.getTFNUMBER()))
			.buildTo(builder::withCertificate);
	    }

	    {
		// PURPOSE s:string Цель использования ТС
		builder.withVehiclePurpose(source.getPURPOSE());
	    }

	    {
		// ODOMETER s:int Показания одометра
		builder.withCurrentOdometerValue(source.getODOMETER());
	    }

	    {
		// CREATED_BY_USER_ID s:int Идентификатор пользователя,
		// создавшего
		// запись
		// INPUT_DATE s:string Дата\время ввода записи в систему
		RecordOperationInfo.builder()
			.withInstant(optTemporalToInstant(source.getINPUTDATE()).orElse(null))
			.withAuthor(Util.reqField(PolicyVehicleEntity.class,
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
		if (MyStrings.nonEmpty(source.getRECORDCHANGEDAT()))
		    RecordOperationInfo.builder()
			    .withInstant(optTemporalToInstant(source.getRECORDCHANGEDAT()).orElse(null))
			    .withAuthor(Util.reqField(PolicyVehicleEntity.class,
				    id,
				    userService::getById,
				    "modified.author",
				    UserEntity.class,
				    source.getCHANGEDBYUSERID()))
			    .buildTo(builder::withModified);
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
		builder.withInsurer(Util.reqField(PolicyVehicleEntity.class,
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
