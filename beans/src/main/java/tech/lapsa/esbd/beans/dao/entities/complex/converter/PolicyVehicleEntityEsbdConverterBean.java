package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.Util.*;

import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;
import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService.KZAreaServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.PolicyVehicleEntity;
import tech.lapsa.esbd.domain.complex.PolicyVehicleEntity.PolicyVehicleEntityBuilder;
import tech.lapsa.esbd.domain.complex.UserEntity;
import tech.lapsa.esbd.domain.complex.VehicleEntity;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.domain.embedded.RecordOperationInfo;
import tech.lapsa.esbd.domain.embedded.RecordOperationInfo.RecordOperationInfoBuilder;
import tech.lapsa.esbd.domain.embedded.VehicleCertificateInfo;
import tech.lapsa.esbd.domain.embedded.VehicleCertificateInfo.VehicleCertificateInfoBuilder;
import tech.lapsa.esbd.jaxws.wsimport.PoliciesTF;
import tech.lapsa.java.commons.function.MyOptionals;
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
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// POLICY_ID s:int Идентификатор полиса
	    }

	    {
		// TF_ID s:int Идентификатор ТС
		optField(PolicyVehicleEntity.class,
			id,
			vehicleService::getById,
			"vehicle",
			VehicleEntity.class,
			MyOptionals.of(source.getTFID()))
				.ifPresent(builder::withVehicle);
	    }

	    {
		// TF_TYPE_ID s:int Идентификатор типа ТС (обязательно)
		optField(PolicyVehicleEntity.class,
			id,
			vehicleClassService::getById,
			"vehicleClass",
			VehicleClass.class,
			MyOptionals.of(source.getTFTYPEID()))
				.ifPresent(builder::withVehicleClass);
	    }

	    {
		// TF_AGE_ID s:int Идентификатор возраста ТС (обязательно)
		optField(PolicyVehicleEntity.class,
			id,
			vehicleAgeClassService::getById,
			"vehicleAgeClass",
			VehicleAgeClass.class,
			MyOptionals.of(source.getTFAGEID()))
				.ifPresent(builder::withVehicleAgeClass);
	    }

	    {
		// TF_NUMBER s:string Гос. номер ТС
		// TF_REGISTRATION_CERTIFICATE s:string Номер тех. паспорта
		// GIVE_DATE s:string Дата выдачи тех. паспорта
		// REGION_ID s:int Идентификатор региона регистрации ТС
		// (обязательно)
		// BIG_CITY_BOOL s:int Признак города областного значения
		// (обязательно)
		final VehicleCertificateInfoBuilder b1 = VehicleCertificateInfo.builder();

		MyOptionals.of(source.getTFREGISTRATIONCERTIFICATE())
			.ifPresent(b1::withNumber);

		MyOptionals.of(source.getGIVEDATE())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(b1::withDateOfIssue);

		b1.withRegistrationMajorCity(source.getBIGCITYBOOL() == 1);

		optField(PolicyVehicleEntity.class,
			id,
			countryRegionService::getById,
			"certificate.registrationRegion",
			KZArea.class,
			MyOptionals.of(source.getREGIONID()))
				.ifPresent(b1::withRegistrationRegion);

		MyOptionals.of(source.getTFNUMBER())
			.map(VehicleRegNumber::assertValid)
			.ifPresent(b1::withRegistrationNumber);

		b1.buildTo(builder::withCertificate);
	    }

	    {
		// PURPOSE s:string Цель использования ТС
		MyOptionals.of(source.getPURPOSE())
			.ifPresent(builder::withVehiclePurpose);
	    }

	    {
		// ODOMETER s:int Показания одометра
		MyOptionals.ofPositive(source.getODOMETER())
			.ifPresent(builder::withCurrentOdometerValue);
	    }

	    {
		final Optional<String> instant = MyOptionals.of(source.getINPUTDATE());
		final Optional<Integer> author = MyOptionals.of(source.getCREATEDBYUSERID());
		if (instant.isPresent() || author.isPresent()) {

		    final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		    instant.flatMap(TemporalUtil::optTemporalToInstant)
			    .ifPresent(b1::withInstant);

		    optField(PolicyVehicleEntity.class,
			    id,
			    userService::getById,
			    "created.author",
			    UserEntity.class,
			    author)
				    .ifPresent(b1::withAuthor);

		    b1.buildTo(builder::withCreated);
		}
	    }

	    {
		final Optional<String> instant = MyOptionals.of(source.getRECORDCHANGEDAT());
		final Optional<Integer> author = MyOptionals.of(source.getCHANGEDBYUSERID());
		if (instant.isPresent() || author.isPresent()) {

		    final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		    instant.flatMap(TemporalUtil::optTemporalToInstant)
			    .ifPresent(b1::withInstant);

		    optField(PolicyVehicleEntity.class,
			    id,
			    userService::getById,
			    "modified.author",
			    UserEntity.class,
			    author)
				    .ifPresent(b1::withAuthor);

		    b1.buildTo(builder::withModified);
		}
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
		optField(PolicyVehicleEntity.class,
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
