package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaIntermediateArrayService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleEntityEsbdConverterBean;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfTF;
import tech.lapsa.esbd.jaxws.wsimport.TF;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.kz.vehicle.VehicleRegNumber;

@Stateless(name = VehicleEntityService.BEAN_NAME)
public class VehicleEntityServiceBean
	extends AOndemandComplexViaIntermediateArrayService<VehicleEntity, TF, ArrayOfTF>
	implements VehicleEntityServiceLocal, VehicleEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<ArrayOfTF> ESBD_LOOKUP_FUNCTION = (con, id) -> {
	final TF param = new TF();
	param.setTFID(id.intValue());
	return con.getTFByKeyFields(param);
    };

    private static final ESBDEntityStoreFunction<TF> ESBD_STORE_FUNCTION = (con, e) -> con.setTF(e);

    private static final Function<ArrayOfTF, List<TF>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfTF::getTF;

    // constructor

    protected VehicleEntityServiceBean() {
	super(VehicleEntityService.class,
		VehicleEntity.class,
		INTERMEDIATE_TO_LIST_FUNCTION,
		ESBD_LOOKUP_FUNCTION,
		ESBD_STORE_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleEntity> getByRegNumber(final VehicleRegNumber regNumber) throws IllegalArgument {
	MyObjects.requireNonNull(regNumber, "regNumber"); //
	return cacheControl.put(domainClazz,
		manyFromIntermediateArray(con -> con.getTFByNumber(regNumber.getNumber())));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleEntity> getByVINCode(final String vinCode) throws IllegalArgument {
	MyStrings.requireNonEmpty(vinCode, "vinCode");
	return cacheControl.put(domainClazz, manyFromIntermediateArray(con -> con.getTFByVIN(vinCode)));
    }

    // injected

    @EJB
    private VehicleEntityEsbdConverterBean converter;

    @Override
    protected VehicleEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
