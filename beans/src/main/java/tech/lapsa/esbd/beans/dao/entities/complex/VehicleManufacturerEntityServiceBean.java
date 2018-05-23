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
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleManufacturerEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMARK;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMARK;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = VehicleManufacturerEntityService.BEAN_NAME)
public class VehicleManufacturerEntityServiceBean
	extends AOndemandComplexEntitiesService<VehicleManufacturerEntity, VOITUREMARK>
	implements VehicleManufacturerEntityServiceLocal, VehicleManufacturerEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<VOITUREMARK>> GET_BY_ID_FUNCTION = (con, id) -> {
	MyNumbers.requireNonZero(id, "id");
	final VOITUREMARK search = new VOITUREMARK();
	search.setID(id.intValue());
	final ArrayOfVOITUREMARK manufacturers = con.getVoitureMarks(search);
	return manufacturers.getVOITUREMARK();
    };

    // constructor

    protected VehicleManufacturerEntityServiceBean() {
	super(VehicleManufacturerEntityService.class, VehicleManufacturerEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

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

    // injected

    @EJB
    private VehicleManufacturerEntityEsbdConverterBean converter;

    @Override
    protected VehicleManufacturerEntityEsbdConverterBean getConverter() {
	return converter;
    }

    // private

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
}
