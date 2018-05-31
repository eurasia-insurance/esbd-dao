package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import tech.lapsa.esbd.beans.dao.entities.APreloadedEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.UserEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.UserEntity;
import tech.lapsa.esbd.jaxws.wsimport.User;
import tech.lapsa.java.commons.exceptions.IllegalArgument;

@Singleton(name = UserEntityService.BEAN_NAME)
@Startup
public class UserEntityServiceBean
	extends APreloadedEntitiesService<UserEntity, User>
	implements UserEntityServiceLocal, UserEntityServiceRemote {

    // static finals

    private static final Function<Connection, List<User>> GET_ALL_FUNCTION = (con) -> con.getUsers().getUser();
    private static final Function<User, Integer> GET_ESBD_ID_FUNCTION = User::getID;

    // constructor

    protected UserEntityServiceBean() {
	super(UserEntityService.class, UserEntity.class, GET_ALL_FUNCTION, GET_ESBD_ID_FUNCTION);
    }

    // public

    @Override
    public UserEntity currentUser() {
	final User user;
	try (Connection con = pool.getConnection()) {
	    user = con.currentUser();
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	try {
	    return getById(user.getID());
	} catch (NotFound | IllegalArgument e) {
	    throw new EJBException(e.getMessage());
	}
    }

    // injected

    @EJB
    private UserEntityEsbdConverterBean converter;

    @Override
    protected UserEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
