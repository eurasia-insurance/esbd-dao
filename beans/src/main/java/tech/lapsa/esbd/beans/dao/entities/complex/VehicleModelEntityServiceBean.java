package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleModelEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.complex.VehicleModelEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMODEL;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMODEL;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = VehicleModelEntityService.BEAN_NAME)
public class VehicleModelEntityServiceBean
	extends AOndemandComplexEntitiesService<VehicleModelEntity, VOITUREMODEL, ArrayOfVOITUREMODEL>
	implements VehicleModelEntityServiceLocal, VehicleModelEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, ArrayOfVOITUREMODEL> GET_BY_ID_FUNCTION = (con, id) -> {
	final VOITUREMODEL param = new VOITUREMODEL();
	param.setID(id.intValue());
	return con.getVoitureModels(param);
    };

    private static final Function<ArrayOfVOITUREMODEL, List<VOITUREMODEL>> GET_LIST_FUNCTION = ArrayOfVOITUREMODEL::getVOITUREMODEL;

    // constructor

    protected VehicleModelEntityServiceBean() {
	super(VehicleModelEntityService.class, VehicleModelEntity.class, GET_LIST_FUNCTION, true, GET_BY_ID_FUNCTION,
		null);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByName(final String name) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, name, "name");
	return getManyFromIntermediate(con -> {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setNAME(name);
	    return con.getVoitureModels(search);
	});
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByManufacturer(final VehicleManufacturerEntity manufacturer)
	    throws IllegalArgument {
	MyObjects.requireNonNull(IllegalArgument::new, manufacturer, "manufacturer");
	return getManyFromIntermediate(con -> {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setVOITUREMARKID(manufacturer.getId().intValue());
	    return con.getVoitureModels(search);
	});
    }

    // injected

    @EJB
    private VehicleModelEntityEsbdConverterBean converter;

    @Override
    protected VehicleModelEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
