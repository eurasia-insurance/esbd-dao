package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleEntityService.VehicleEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfTF;
import tech.lapsa.esbd.jaxws.wsimport.TF;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.kz.vehicle.VehicleRegNumber;

@Stateless(name = VehicleEntityService.BEAN_NAME)
public class VehicleEntityServiceBean
	extends AOndemandComplexEntitiesService<VehicleEntity, TF>
	implements VehicleEntityServiceLocal, VehicleEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<TF>> GET_BY_ID_FUNCTION = (con, id) -> {
	final TF search = new TF();
	search.setTFID(id.intValue());
	final ArrayOfTF vehicles = con.getTFByKeyFields(search);
	return vehicles.getTF();
    };

    // constructor

    protected VehicleEntityServiceBean() {
	super(VehicleEntityService.class, VehicleEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleEntity> getByRegNumber(final VehicleRegNumber regNumber) throws IllegalArgument {
	try {
	    return _getByRegNumber(regNumber);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleEntity> getByVINCode(final String vinCode) throws IllegalArgument {
	try {
	    return _getByVINCode(vinCode);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // injected

    @EJB
    private VehicleEntityEsbdConverterBean converter;

    @Override
    protected VehicleEntityEsbdConverterBean getConverter() {
	return converter;
    }

    // private

    private List<VehicleEntity> _getByRegNumber(final VehicleRegNumber regNumber) throws IllegalArgumentException {
	MyObjects.requireNonNull(regNumber, "regNumber"); //

	final ArrayOfTF vehicles;
	try (Connection con = pool.getConnection()) {
	    vehicles = con.getTFByNumber(regNumber.getNumber());
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyOptionals.of(vehicles) //
		.map(ArrayOfTF::getTF) //
		.map(Collection::stream) //
		.orElseGet(Stream::empty) //
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }

    private List<VehicleEntity> _getByVINCode(final String vinCode) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(vinCode, "vinCode");
	final ArrayOfTF vehicles;
	try (Connection con = pool.getConnection()) {
	    vehicles = con.getTFByVIN(vinCode);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyOptionals.of(vehicles) //
		.map(ArrayOfTF::getTF) //
		.map(Collection::stream) //
		.orElseGet(Stream::empty) //
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }
}
