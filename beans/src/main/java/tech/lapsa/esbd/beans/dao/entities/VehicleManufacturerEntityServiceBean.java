package tech.lapsa.esbd.beans.dao.entities;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceRemote;
import tech.lapsa.esbd.domain.entities.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.entities.VehicleManufacturerEntity.VehicleManufacturerEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMARK;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMARK;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = VehicleManufacturerEntityService.BEAN_NAME)
public class VehicleManufacturerEntityServiceBean
	implements VehicleManufacturerEntityServiceLocal, VehicleManufacturerEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(VehicleManufacturerEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public VehicleManufacturerEntity getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    return _getById(id);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleManufacturerEntity> getByName(final String name) throws IllegalArgument {
	try {
	    return _getByName(name);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // PRIVATE

    @EJB
    private ConnectionPool pool;

    private VehicleManufacturerEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final ArrayOfVOITUREMARK manufacturers;
	try (Connection con = pool.getConnection()) {
	    final VOITUREMARK search = new VOITUREMARK();
	    search.setID(id.intValue());
	    manufacturers = con.getVoitureMarks(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<VOITUREMARK> list = MyOptionals.of(manufacturers) //
		.map(ArrayOfVOITUREMARK::getVOITUREMARK) //
		.filter(MyCollections::nonEmpty)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with ID = '%2$s'",
			VehicleManufacturerEntity.class.getSimpleName(), id));
	final VOITUREMARK source = Util.requireSingle(list, VehicleManufacturerEntity.class, "ID", id);
	return convert(source);

    }

    private List<VehicleManufacturerEntity> _getByName(final String name) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(name, "name");
	final ArrayOfVOITUREMARK manufacturers;
	try (Connection con = pool.getConnection()) {
	    final VOITUREMARK search = new VOITUREMARK();
	    search.setNAME(name);
	    manufacturers = con.getVoitureMarks(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyOptionals.of(manufacturers) //
		.map(ArrayOfVOITUREMARK::getVOITUREMARK) //
		.map(Collection::stream) //
		.orElseGet(Stream::empty) //
		.map(this::convert) //
		.collect(MyCollectors.unmodifiableList());
    }

    VehicleManufacturerEntity convert(final VOITUREMARK source) {
	try {

	    final VehicleManufacturerEntityBuilder builder = VehicleManufacturerEntity.builder();

	    final int id = source.getID();

	    {
		// ID s:int Идентификатор
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// NAME s:string Наименование марки
		builder.withName(source.getNAME());
	    }

	    {
		// IS_FOREIGN_BOOL s:int Признак иностранной марки
		builder.withForeign(Boolean.valueOf(source.getISFOREIGNBOOL() != 0));
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}
    }
}
