package tech.lapsa.esbd.beans.dao.entities;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.VehicleModelEntityService;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleModelEntityService.VehicleModelEntityServiceRemote;
import tech.lapsa.esbd.domain.entities.VehicleManufacturerEntity;
import tech.lapsa.esbd.domain.entities.VehicleModelEntity;
import tech.lapsa.esbd.domain.entities.VehicleModelEntity.VehicleModelEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfVOITUREMODEL;
import tech.lapsa.esbd.jaxws.wsimport.VOITUREMODEL;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = VehicleModelEntityService.BEAN_NAME)
public class VehicleModelEntityServiceBean implements VehicleModelEntityServiceLocal, VehicleModelEntityServiceRemote {

	private final MyLogger logger = MyLogger.newBuilder() //
	        .withNameOf(VehicleModelEntityService.class) //
	        .build();

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@CacheResult
	public VehicleModelEntity getById(@CacheKey final Integer id) throws NotFound, IllegalArgument {
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
	@CacheResult
	public List<VehicleModelEntity> getByName(@CacheKey final String name) throws IllegalArgument {
		try {
			return _getByName(name);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgument(e);
		} catch (final RuntimeException e) {
			logger.WARN.log(e);
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@CacheResult
	public List<VehicleModelEntity> getByManufacturer(@CacheKey final VehicleManufacturerEntity manufacturer)
	        throws IllegalArgument {
		try {
			return _getByManufacturer(manufacturer);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgument(e);
		} catch (final RuntimeException e) {
			logger.WARN.log(e);
			throw new EJBException(e.getMessage());
		}
	}

	// PRIVATE

	@EJB
	private VehicleManufacturerEntityServiceLocal vehicleManufacturerService;

	@EJB
	private ConnectionPool pool;

	private VehicleModelEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
		MyNumbers.requireNonZero(id, "id");
		final ArrayOfVOITUREMODEL models;
		try (Connection con = pool.getConnection()) {
			final VOITUREMODEL search = new VOITUREMODEL();
			search.setID(id.intValue());
			models = con.getVoitureModels(search);
		} catch (ConnectionException e) {
			throw new IllegalStateException(e.getMessage());
		}

		final List<VOITUREMODEL> list = MyOptionals.of(models) //
		        .map(ArrayOfVOITUREMODEL::getVOITUREMODEL) //
		        .filter(MyCollections::nonEmpty).orElseThrow(MyExceptions.supplier(NotFound::new,
		                "%1$s not found with ID = '%2$s'", VehicleManufacturerEntity.class.getSimpleName(), id));
		final VOITUREMODEL source = Util.requireSingle(list, VehicleModelEntity.class, "ID", id);
		return convert(source);
	}

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
		        .map(this::convert) //
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
		        .map(this::convert) //
		        .collect(MyCollectors.unmodifiableList());
	}

	VehicleModelEntity convert(final VOITUREMODEL source) {
		try {

			final VehicleModelEntityBuilder builder = VehicleModelEntity.builder();

			final int id = source.getID();

			{
				// ID s:int Идентификатор модели
				builder.withId(MyOptionals.of(id).orElse(null));
			}

			{
				// NAME s:string Наименование модели
				builder.withName(source.getNAME());
			}

			{
				// VOITURE_MARK_ID s:int Идентификатор марки ТС
				builder.withManufacturer(
				        Util.reqField(VehicleModelEntity.class, id, vehicleManufacturerService::getById, "manufacturer",
				                VehicleManufacturerEntity.class, source.getVOITUREMARKID()));
			}

			return builder.build();

		} catch (final IllegalArgumentException e) {
			// it should not happens
			throw new EJBException(e.getMessage());
		}

	}

}
