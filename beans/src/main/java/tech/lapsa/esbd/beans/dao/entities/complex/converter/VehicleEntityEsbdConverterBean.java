package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;
import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.SteeringWheelLocation;
import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.dao.elements.dict.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.VehicleEntity;
import tech.lapsa.esbd.domain.complex.VehicleEntity.VehicleEntityBuilder;
import tech.lapsa.esbd.domain.complex.VehicleModelEntity;
import tech.lapsa.esbd.jaxws.wsimport.TF;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleEntityEsbdConverterBean implements AEsbdAttributeConverter<VehicleEntity, TF> {

    @EJB
    private VehicleClassServiceLocal vehicleClassService;

    @EJB
    private VehicleModelEntityServiceLocal vehicleModelService;

    @Override
    public TF convertToEsbdValue(VehicleEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public VehicleEntity convertToEntityAttribute(TF source) throws EsbdConversionException {
	try {

	    final VehicleEntityBuilder builder = VehicleEntity.builder();

	    final int id = source.getTFID();

	    {
		// TF_ID s:int Идентификатор ТС
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// TF_TYPE_ID s:int Тип ТС (справочник TF_TYPES)
		optField(VehicleEntity.class,
			id,
			vehicleClassService::getById,
			"VehicleClass",
			VehicleClass.class,
			MyOptionals.of(source.getTFTYPEID()))
				.ifPresent(builder::withVehicleClass);

	    }

	    {
		// VIN s:string VIN код (номер кузова) (обязательно)
		MyOptionals.of(source.getVIN())
			.ifPresent(builder::withVinCode);
	    }

	    {
		// MODEL_ID s:int Марка\Модель (справочник VOITURE_MODELS)
		// (обязательно)
		optField(VehicleEntity.class,
			id,
			vehicleModelService::getById,
			"vehicleModel",
			VehicleModelEntity.class,
			MyOptionals.of(source.getMODELID()))
				.ifPresent(builder::withVehicleModel);
	    }

	    {
		// RIGHT_HAND_DRIVE_BOOL s:int Признак расположения руля
		// (0 - слева 1 - справа)
		final SteeringWheelLocation swl = source.getRIGHTHANDDRIVEBOOL() == 0
			? SteeringWheelLocation.LEFT_SIDE
			: source.getRIGHTHANDDRIVEBOOL() == 1
				? SteeringWheelLocation.RIGHT_SIDE
				: null;
		MyOptionals.of(swl)
			.ifPresent(builder::withSteeringWheelLocation);
	    }

	    {
		// ENGINE_VOLUME s:int Объем двигателя (куб.см.)
		// ENGINE_NUMBER s:string Номер двигателя
		// ENGINE_POWER s:int Мощность двигателя (квт.)
		MyOptionals.of(source.getENGINENUMBER())
			.ifPresent(builder::withEngineNumber);
		MyOptionals.of(source.getENGINEVOLUME())
			.ifPresent(builder::withEngineVolume);
		MyOptionals.of(source.getENGINEPOWER())
			.ifPresent(builder::withEnginePower);
	    }

	    {
		// COLOR s:string Цвет ТС
		MyOptionals.of(source.getCOLOR())
			.ifPresent(builder::withColor);
	    }

	    {
		// BORN s:string Год выпуска (обязательно)
		// BORN_MONTH s:int Месяц выпуска ТС
		MyOptionals.of(yearMonthToLocalDate(source.getBORN(), source.getBORNMONTH()))
			.ifPresent(builder::withRealeaseDate);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
