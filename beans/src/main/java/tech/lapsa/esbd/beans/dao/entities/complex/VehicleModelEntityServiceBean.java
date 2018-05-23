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
import tech.lapsa.esbd.beans.dao.entities.complex.converter.VehicleModelEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.complex.VehicleModelEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMODEL;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMODEL;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = VehicleModelEntityService.BEAN_NAME)
public class VehicleModelEntityServiceBean
	extends AOndemandComplexEntitiesService<VehicleModelEntity, VOITUREMODEL>
	implements VehicleModelEntityServiceLocal, VehicleModelEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<VOITUREMODEL>> GET_BY_ID_FUNCTION = (con, id) -> {
	final VOITUREMODEL search = new VOITUREMODEL();
	search.setID(id.intValue());
	final ArrayOfVOITUREMODEL models = con.getVoitureModels(search);
	return models.getVOITUREMODEL();
    };

    // constructor

    protected VehicleModelEntityServiceBean() {
	super(VehicleModelEntityService.class, VehicleModelEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByName(final String name) throws IllegalArgument {
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

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VehicleModelEntity> getByManufacturer(final VehicleManufacturerEntity manufacturer)
	    throws IllegalArgument {
	try {
	    return _getByManufacturer(manufacturer);
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
    private VehicleModelEntityEsbdConverterBean converter;

    @Override
    protected VehicleModelEntityEsbdConverterBean getConverter() {
	return converter;
    }

    // private

    private List<VehicleModelEntity> _getByName(final String name) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(name, "name");
	final ArrayOfVOITUREMODEL models;
	try (Connection con = pool.getConnection()) {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setNAME(name);
	    models = con.getVoitureModels(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyOptionals.of(models) //
		.map(ArrayOfVOITUREMODEL::getVOITUREMODEL) //
		.map(Collection::stream) //
		.orElseGet(Stream::empty) //
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }

    private List<VehicleModelEntity> _getByManufacturer(final VehicleManufacturerEntity manufacturer)
	    throws IllegalArgumentException {
	MyObjects.requireNonNull(manufacturer, "manufacturer");
	final ArrayOfVOITUREMODEL models;
	try (Connection con = pool.getConnection()) {
	    final VOITUREMODEL search = new VOITUREMODEL();
	    search.setVOITUREMARKID(manufacturer.getId().intValue());
	    models = con.getVoitureModels(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return MyOptionals.of(models) //
		.map(ArrayOfVOITUREMODEL::getVOITUREMODEL) //
		.map(Collection::stream) //
		.orElseGet(Stream::empty) //
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }
}
