package tech.lapsa.esbd.beans.dao;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceLocal;
import tech.lapsa.esbd.dao.IEntitiesService.IEntityServiceRemote;
import tech.lapsa.esbd.domain.AEntity;

public abstract class AEntitiesService<DOMAIN extends AEntity, ESBD>
	extends AService<DOMAIN>
	implements IEntityServiceLocal<DOMAIN>, IEntityServiceRemote<DOMAIN> {

    // constructor

    protected AEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClazz) {
	super(serviceClazz, domainClazz);
    }

    // private

    // converter

    protected abstract AEsbdAttributeConverter<DOMAIN, ESBD> getConverter();

    protected DOMAIN conversion(ESBD source) {
	try {
	    return getConverter().convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
