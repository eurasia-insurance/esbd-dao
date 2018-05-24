package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService.AOndemandComplexIdByIntermediateService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleManufacturerEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
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
	extends AOndemandComplexIdByIntermediateService<VehicleManufacturerEntity, VOITUREMARK, ArrayOfVOITUREMARK>
	implements VehicleManufacturerEntityServiceLocal, VehicleManufacturerEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, ArrayOfVOITUREMARK> GET_BY_ID_FUNCTION = (con, id) -> {
	final VOITUREMARK param = new VOITUREMARK();
	param.setID(id.intValue());
	return con.getVoitureMarks(param);
    };

    private static final Function<ArrayOfVOITUREMARK, List<VOITUREMARK>> GET_LIST_FUNCTION = ArrayOfVOITUREMARK::getVOITUREMARK;

    // constructor

    protected VehicleManufacturerEntityServiceBean() {
	super(VehicleManufacturerEntityService.class, VehicleManufacturerEntity.class, GET_LIST_FUNCTION,
		GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleManufacturerEntity> getByName(final String name) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, name, "name");
	return manyFromIntermediateArray(con -> {
	    final VOITUREMARK search = new VOITUREMARK();
	    search.setNAME(name);
	    return con.getVoitureMarks(search);
	});
    }

    // injected

    @EJB
    private VehicleManufacturerEntityEsbdConverterBean converter;

    @Override
    protected VehicleManufacturerEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
