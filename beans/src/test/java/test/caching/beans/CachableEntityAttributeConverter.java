package test.caching.beans;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter;

public class CachableEntityAttributeConverter implements AEsbdAttributeConverter<CachableEntity, CachableESBDEntity> {

    @Override
    public CachableESBDEntity convertToEsbdValue(CachableEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CachableEntity convertToEntityAttribute(CachableESBDEntity source) throws EsbdConversionException {
	return new CachableEntity(source.getId());
    }

}
