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

import com.lapsa.insurance.elements.SteeringWheelLocation;
import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.beans.dao.ESBDDates;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleEntityService;
import tech.lapsa.esbd.dao.entities.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleEntityService.VehicleEntityServiceRemote;
import tech.lapsa.esbd.dao.entities.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.esbd.domain.entities.VehicleEntity;
import tech.lapsa.esbd.domain.entities.VehicleModelEntity;
import tech.lapsa.esbd.domain.entities.VehicleEntity.VehicleEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfTF;
import tech.lapsa.esbd.jaxws.wsimport.TF;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.kz.vehicle.VehicleRegNumber;

@Stateless(name = VehicleEntityService.BEAN_NAME)
public class VehicleEntityServiceBean implements VehicleEntityServiceLocal, VehicleEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(VehicleEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @CacheResult
    public List<VehicleEntity> getByRegNumber(@CacheKey final VehicleRegNumber regNumber) throws IllegalArgument {
	try {
	    return _getByRegNumber(regNumber);
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
    public VehicleEntity getById(@CacheKey final Integer id) throws NotFound, IllegalArgument {
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
    public List<VehicleEntity> getByVINCode(@CacheKey final String vinCode) throws IllegalArgument {
	try {
	    return _getByVINCode(vinCode);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // PRIVATE

    @EJB
    private VehicleClassServiceLocal vehicleClassService;

    @EJB
    private VehicleModelEntityServiceLocal vehicleModelService;

    @EJB
    private ConnectionPool pool;

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
		.map(this::convertToBuilder) //
		.peek(x -> x.withRegNum(regNumber)).map(VehicleEntityBuilder::build)
		.collect(MyCollectors.unmodifiableList());
    }

    private VehicleEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final ArrayOfTF vehicles;
	try (Connection con = pool.getConnection()) {
	    final TF search = new TF();
	    search.setTFID(id.intValue());
	    vehicles = con.getTFByKeyFields(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	final List<TF> list = MyOptionals.of(vehicles) //
		.map(ArrayOfTF::getTF) //
		.filter(MyCollections::nonEmpty)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with ID = '%2$s'",
			VehicleEntity.class.getSimpleName(), id));
	final TF source = Util.requireSingle(list, VehicleEntity.class, "ID", id);
	return convertToBuilder(source).build();
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
		.map(this::convertToBuilder) //
		.map(VehicleEntityBuilder::build).collect(MyCollectors.unmodifiableList());
    }

    VehicleEntityBuilder convertToBuilder(final TF source) {
	try {

	    final VehicleEntityBuilder builder = VehicleEntity.builder();

	    final int id = source.getTFID();

	    {
		// TF_ID s:int Идентификатор ТС
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// TF_TYPE_ID s:int Тип ТС (справочник TF_TYPES)
		builder.withVehicleClass(Util.reqField(VehicleEntity.class, id, vehicleClassService::getById,
			"VehicleClass", VehicleClass.class, source.getTFTYPEID()));
	    }

	    {
		// VIN s:string VIN код (номер кузова) (обязательно)
		builder.withVinCode(source.getVIN());
	    }

	    {
		// MODEL_ID s:int Марка\Модель (справочник VOITURE_MODELS)
		// (обязательно)
		builder.withVehicleModel(Util.reqField(VehicleEntity.class, id, vehicleModelService::getById,
			"vehicleModel", VehicleModelEntity.class, source.getMODELID()));
	    }

	    {
		// RIGHT_HAND_DRIVE_BOOL s:int Признак расположения руля (0 -
		// слева;
		// 1 -
		// справа)
		builder.withSteeringWheelLocation(source.getRIGHTHANDDRIVEBOOL() == 0 ? SteeringWheelLocation.LEFT_SIDE
			: SteeringWheelLocation.RIGHT_SIDE);
	    }

	    {
		// ENGINE_VOLUME s:int Объем двигателя (куб.см.)
		// ENGINE_NUMBER s:string Номер двигателя
		// ENGINE_POWER s:int Мощность двигателя (квт.)
		builder.withEngine(source.getENGINENUMBER(), source.getENGINEVOLUME(), source.getENGINEPOWER());
	    }

	    {
		// COLOR s:string Цвет ТС
		builder.withColor(source.getCOLOR());
	    }

	    {
		// BORN s:string Год выпуска (обязательно)
		// BORN_MONTH s:int Месяц выпуска ТС
		builder.withRealeaseDate(ESBDDates.fromESBDYearMonth(source.getBORN(), source.getBORNMONTH()));
	    }

	    return builder;

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}
    }
}
