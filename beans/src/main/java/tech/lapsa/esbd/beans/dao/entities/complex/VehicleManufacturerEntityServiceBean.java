package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaIntermediateArrayService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleManufacturerEntityEsbdConverterBean;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMARK;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMARK;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = VehicleManufacturerEntityService.BEAN_NAME)
public class VehicleManufacturerEntityServiceBean
	extends AOndemandComplexViaIntermediateArrayService<VehicleManufacturerEntity, VOITUREMARK, ArrayOfVOITUREMARK>
	implements VehicleManufacturerEntityServiceLocal, VehicleManufacturerEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<ArrayOfVOITUREMARK> ESBD_LOOKUP_FUNCTION = (con, id) -> {
	final VOITUREMARK param = new VOITUREMARK();
	param.setID(id.intValue());
	return con.getVoitureMarks(param);
    };

    private static final Function<ArrayOfVOITUREMARK, List<VOITUREMARK>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfVOITUREMARK::getVOITUREMARK;

    // constructor

    protected VehicleManufacturerEntityServiceBean() {
	super(VehicleManufacturerEntityService.class, VehicleManufacturerEntity.class, INTERMEDIATE_TO_LIST_FUNCTION,
		ESBD_LOOKUP_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleManufacturerEntity> getByName(final String name) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, name, "name");
	return cacheControl.put(domainClazz, manyFromIntermediateArray(con -> {
	    final VOITUREMARK search = new VOITUREMARK();
	    search.setNAME(name);
	    return con.getVoitureMarks(search);
	}));
    }

    // injected

    @EJB
    private VehicleManufacturerEntityEsbdConverterBean converter;

    @Override
    protected VehicleManufacturerEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
