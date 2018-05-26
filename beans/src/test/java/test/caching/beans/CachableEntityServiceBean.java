package test.caching.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexIdBySingleService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter;
import tech.lapsa.esbd.connection.Connection;

@LocalBean
@Stateless
public class CachableEntityServiceBean
	extends AOndemandComplexIdBySingleService<CachableEntity, CachableESBDEntity, ArrayOfCachableESBDEntity> {

    protected CachableEntityServiceBean() {
	super(CachableEntityServiceBean.class, CachableEntity.class, ArrayOfCachableESBDEntity::getList,
		CachableEntityServiceBean::getSingleById);
    }

    private static final AEsbdAttributeConverter<CachableEntity, CachableESBDEntity> converter = new CachableEntityAttributeConverter();

    @Override
    protected AEsbdAttributeConverter<CachableEntity, CachableESBDEntity> getConverter() {
	return converter;
    }

    public static CachableESBDEntity getSingleById(Connection con, Integer id) {
	return new CachableESBDEntity(id);
    }
}
