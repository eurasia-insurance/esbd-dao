package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;

@Stateless
@LocalBean
public class SubjectEntityEsbdConverterBean implements AEsbdAttributeConverter<SubjectEntity, Client> {

    @EJB
    private SubjectPersonEntityConverterBean subjectPersonEntityConveter;

    @EJB
    private SubjectCompanyEntityConverterBean subjectCompanyEntityConveter;

    @Override
    public Client convertToEsbdValue(SubjectEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SubjectEntity convertToEntityAttribute(Client source) throws EsbdConversionException {
	return source.getNaturalPersonBool() == 1
		? subjectPersonEntityConveter.convertToEntityAttribute(source)
		: subjectCompanyEntityConveter.convertToEntityAttribute(source);
    }
}
