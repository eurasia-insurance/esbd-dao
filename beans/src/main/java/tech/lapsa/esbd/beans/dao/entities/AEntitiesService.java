package tech.lapsa.esbd.beans.dao.entities;

import tech.lapsa.esbd.beans.dao.AService;
import tech.lapsa.esbd.beans.dao.Util;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.dao.entities.ICachableEntitiesService.ICachableEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.ICachableEntitiesService.ICachableEntityServiceRemote;
import tech.lapsa.esbd.domain.AEntity;

public abstract class AEntitiesService<DOMAIN extends AEntity, ESBD>
	extends AService<DOMAIN>
	implements ICachableEntityServiceLocal<DOMAIN>, ICachableEntityServiceRemote<DOMAIN> {

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
