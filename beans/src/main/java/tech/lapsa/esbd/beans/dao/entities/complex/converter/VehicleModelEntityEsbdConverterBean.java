package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.complex.VehicleModelEntity;
import tech.lapsa.esbd.domain.complex.VehicleModelEntity.VehicleModelEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMODEL;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleModelEntityEsbdConverterBean
	implements AEsbdAttributeConverter<VehicleModelEntity, VOITUREMODEL> {

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
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// NAME s:string Наименование модели
		MyOptionals.of(source.getNAME())
			.ifPresent(builder::withName);
	    }

	    {
		// VOITURE_MARK_ID s:int Идентификатор марки ТС
		optField(VehicleModelEntity.class,
			id,
			vehicleManufacturerService::getById,
			"manufacturer",
			VehicleManufacturerEntity.class,
			MyOptionals.of(source.getVOITUREMARKID()))
				.ifPresent(builder::withManufacturer);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
