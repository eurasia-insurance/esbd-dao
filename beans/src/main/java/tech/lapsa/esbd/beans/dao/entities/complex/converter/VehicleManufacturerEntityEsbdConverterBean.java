package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity.VehicleManufacturerEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMARK;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleManufacturerEntityEsbdConverterBean
	implements AEsbdAttributeConverter<VehicleManufacturerEntity, VOITUREMARK> {

    @Override
    public VOITUREMARK convertToEsbdValue(VehicleManufacturerEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public VehicleManufacturerEntity convertToEntityAttribute(VOITUREMARK source) throws EsbdConversionException {
	try {

	    final VehicleManufacturerEntityBuilder builder = VehicleManufacturerEntity.builder();

	    final int id = source.getID();

	    {
		// ID s:int Идентификатор
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// NAME s:string Наименование марки
		MyOptionals.of(source.getNAME())
			.ifPresent(builder::withName);
	    }

	    {
		// IS_FOREIGN_BOOL s:int Признак иностранной марки
		builder.withForeign(Boolean.valueOf(source.getISFOREIGNBOOL() != 0));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
