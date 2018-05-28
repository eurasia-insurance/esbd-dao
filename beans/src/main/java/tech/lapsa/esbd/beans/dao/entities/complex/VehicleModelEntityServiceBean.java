package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaIntermediateArrayService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleModelEntityEsbdConverterBean;
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
	extends AOndemandComplexViaIntermediateArrayService<VehicleModelEntity, VOITUREMODEL, ArrayOfVOITUREMODEL>
	implements VehicleModelEntityServiceLocal, VehicleModelEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<ArrayOfVOITUREMODEL> ESBD_LOOKUP_FUNCTION = (con, id) -> {
	final VOITUREMODEL param = new VOITUREMODEL();
	param.setID(id.intValue());
	return con.getVoitureModels(param);
    };

    private static final Function<ArrayOfVOITUREMODEL, List<VOITUREMODEL>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfVOITUREMODEL::getVOITUREMODEL;

    // constructor

    protected VehicleModelEntityServiceBean() {
	super(VehicleModelEntityService.class, VehicleModelEntity.class, INTERMEDIATE_TO_LIST_FUNCTION,
		ESBD_LOOKUP_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByName(final String name) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, name, "name");
	return cacheControl.put(domainClazz, manyFromIntermediateArray(con -> {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setNAME(name);
	    return con.getVoitureModels(search);
	}));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByManufacturer(final VehicleManufacturerEntity manufacturer)
	    throws IllegalArgument {
	MyObjects.requireNonNull(IllegalArgument::new, manufacturer, "manufacturer");
	return cacheControl.put(domainClazz, manyFromIntermediateArray(con -> {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setVOITUREMARKID(manufacturer.getId().intValue());
	    return con.getVoitureModels(search);
	}));
    }

    // injected

    @EJB
    private VehicleModelEntityEsbdConverterBean converter;

    @Override
    protected VehicleModelEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
