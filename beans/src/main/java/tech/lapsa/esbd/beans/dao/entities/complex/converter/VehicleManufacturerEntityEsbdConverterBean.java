package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntity.VehicleManufacturerEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMARK;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class VehicleManufacturerEntityEsbdConverterBean
	implements EsbdAttributeConverter<VehicleManufacturerEntity, VOITUREMARK> {

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
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// NAME s:string Наименование марки
		builder.withName(source.getNAME());
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
