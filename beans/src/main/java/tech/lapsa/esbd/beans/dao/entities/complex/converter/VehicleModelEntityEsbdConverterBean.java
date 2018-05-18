package tech.lapsa.esbd.beans.dao.entities.converter;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter;
import tech.lapsa.esbd.beans.dao.entities.Util;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntity;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleModelEntity;
import tech.lapsa.esbd.dao.entities.VehicleModelEntity.VehicleModelEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMODEL;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleModelEntityEsbdConverterBean
	implements EsbdAttributeConverter<VehicleModelEntity, VOITUREMODEL> {

    @EJB
    private VehicleManufacturerEntityServiceLocal vehicleManufacturerService;

    @Override
    public VOITUREMODEL convertToEsbdValue(VehicleModelEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public VehicleModelEntity convertToEntityAttribute(VOITUREMODEL source) throws EsbdConversionException {
	try {

	    final VehicleModelEntityBuilder builder = VehicleModelEntity.builder();

	    final int id = source.getID();

	    {
		// ID s:int Идентификатор модели
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// NAME s:string Наименование модели
		builder.withName(source.getNAME());
	    }

	    {
		// VOITURE_MARK_ID s:int Идентификатор марки ТС
		builder.withManufacturer(Util.reqField(VehicleModelEntity.class,
			id,
			vehicleManufacturerService::getById,
			"manufacturer",
			VehicleManufacturerEntity.class,
			source.getVOITUREMARKID()));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
