package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.UserEntityEsbdConverterBean;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.EsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.UserEntity;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceRemote;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfUser;
import tech.lapsa.esbd.jaxws.wsimport.User;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.logging.MyLogger;

@Singleton(name = UserEntityService.BEAN_NAME)
@Startup
public class UserEntityServiceBean
	extends AComplexEntitiesService<UserEntity, User>
	implements UserEntityServiceLocal, UserEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(UserEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<UserEntity> getAll() {
	try {
	    return _getAll();
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    public UserEntity getById(final Integer id) throws NotFound, IllegalArgument {
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

    // PRIVATE

    private static class Holder {
	private final User esbd;
	private UserEntity fetched;

	private Holder(final User esbd) {
	    this.esbd = esbd;
	}
    }

    private Map<Integer, Holder> all;

    @PostConstruct
    @Schedule(dayOfWeek = "*")
    public void reload() {
	final ArrayOfUser items;
	try (Connection con = pool.getConnection()) {
	    items = con.getUsers();
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	all = MyOptionals.of(items) //
		.map(ArrayOfUser::getUser) //
		.map(List::stream) //
		.orElseGet(Stream::empty) //
		.collect(MyCollectors.unmodifiableMap(User::getID, Holder::new));
    }

    private UserEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final Holder holder = all.get(id);
	if (holder == null)
	    throw MyExceptions.format(NotFound::new, "%1$s(%2$s) not found", UserEntity.class.getSimpleName(), id);
	synchronized (holder) {
	    if (holder.fetched == null)
		holder.fetched = conversion(holder.esbd);
	}
	return holder.fetched;
    }

    private List<UserEntity> _getAll() {
	try {
	    return all.entrySet()
		    .stream()
		    .map(Map.Entry::getValue)
		    .filter(MyObjects::nonNull)
		    .map(holder -> {
			synchronized (holder) {
			    if (holder.fetched == null)
				holder.fetched = conversion(holder.esbd);
			}
			return holder.fetched;
		    })
		    .collect(MyCollectors.unmodifiableList());
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // converter

    @EJB
    private UserEntityEsbdConverterBean converter;

    @Override
    UserEntity conversion(final User source) {
	try {
	    return converter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
