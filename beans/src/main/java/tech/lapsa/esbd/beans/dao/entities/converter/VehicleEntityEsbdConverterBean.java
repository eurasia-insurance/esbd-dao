package tech.lapsa.esbd.beans.dao.entities.converter;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.SteeringWheelLocation;
import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter;
import tech.lapsa.esbd.beans.dao.entities.Util;
import tech.lapsa.esbd.dao.elements.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleEntity;
import tech.lapsa.esbd.dao.entities.VehicleEntity.VehicleEntityBuilder;
import tech.lapsa.esbd.dao.entities.VehicleModelEntity;
import tech.lapsa.esbd.dao.entities.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.jaxws.wsimport.TF;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleEntityEsbdConverterBean implements EsbdAttributeConverter<VehicleEntity, TF> {

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
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// TF_TYPE_ID s:int Тип ТС (справочник TF_TYPES)
		builder.withVehicleClass(Util.reqField(VehicleEntity.class,
			id,
			vehicleClassService::getById,
			"VehicleClass",
			VehicleClass.class,
			source.getTFTYPEID()));
	    }

	    {
		// VIN s:string VIN код (номер кузова) (обязательно)
		builder.withVinCode(source.getVIN());
	    }

	    {
		// MODEL_ID s:int Марка\Модель (справочник VOITURE_MODELS)
		// (обязательно)
		builder.withVehicleModel(Util.reqField(VehicleEntity.class,
			id,
			vehicleModelService::getById,
			"vehicleModel",
			VehicleModelEntity.class,
			source.getMODELID()));
	    }

	    {
		// RIGHT_HAND_DRIVE_BOOL s:int Признак расположения руля (0 -
		// слева;
		// 1 -
		// справа)
		builder.withSteeringWheelLocation(source.getRIGHTHANDDRIVEBOOL() == 0
			? SteeringWheelLocation.LEFT_SIDE
			: SteeringWheelLocation.RIGHT_SIDE);
	    }

	    {
		// ENGINE_VOLUME s:int Объем двигателя (куб.см.)
		// ENGINE_NUMBER s:string Номер двигателя
		// ENGINE_POWER s:int Мощность двигателя (квт.)
		builder.withEngine(source.getENGINENUMBER(), source.getENGINEVOLUME(), source.getENGINEPOWER());
	    }

	    {
		// COLOR s:string Цвет ТС
		builder.withColor(source.getCOLOR());
	    }

	    {
		// BORN s:string Год выпуска (обязательно)
		// BORN_MONTH s:int Месяц выпуска ТС
		builder.withRealeaseDate(TemporalUtil.yearMonthToLocalDate(source.getBORN(), source.getBORNMONTH()));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
