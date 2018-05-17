package tech.lapsa.esbd.beans.dao.entities.converter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter;
import tech.lapsa.esbd.dao.entities.InsuredVehicleEntity;
import tech.lapsa.esbd.jaxws.wsimport.PoliciesTF;

@Stateless
@LocalBean
public class PolicyVehicleEntityEsbdConverter implements EsbdAttributeConverter<InsuredVehicleEntity, PoliciesTF> {

    @Override
    public PoliciesTF convertToEsbdValue(InsuredVehicleEntity entityAttribute) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public InsuredVehicleEntity convertToEntityAttribute(PoliciesTF esbdValue) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

}
