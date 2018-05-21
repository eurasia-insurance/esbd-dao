package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleManufacturerEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
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
	extends AComplexEntitiesService<VehicleManufacturerEntity, VOITUREMARK>
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
	} catch (final EJBException e) {
	    throw e;
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
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // PRIVATE

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
	return conversion(source);

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
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }

    // converter

    @EJB
    private VehicleManufacturerEntityEsbdConverterBean converter;

    @Override
    VehicleManufacturerEntity conversion(final VOITUREMARK source) {
	try {
	    return converter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
