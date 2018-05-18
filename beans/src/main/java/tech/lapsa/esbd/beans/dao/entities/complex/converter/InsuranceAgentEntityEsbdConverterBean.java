package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntity.InsuranceAgentEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.MIDDLEMAN;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class InsuranceAgentEntityEsbdConverterBean
	implements AEsbdAttributeConverter<InsuranceAgentEntity, MIDDLEMAN> {

    @Override
    public MIDDLEMAN convertToEsbdValue(InsuranceAgentEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public InsuranceAgentEntity convertToEntityAttribute(MIDDLEMAN source) throws EsbdConversionException {
	try {

	    final InsuranceAgentEntityBuilder builder = InsuranceAgentEntity.builder();

	    final int id = source.getMIDDLEMANID();

	    {
		// ID s:int Идентификатор
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
