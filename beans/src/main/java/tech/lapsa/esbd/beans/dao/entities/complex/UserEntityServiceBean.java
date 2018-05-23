package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import tech.lapsa.esbd.beans.dao.APreloadedComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.UserEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.UserEntity;
import tech.lapsa.esbd.jaxws.wsimport.User;

@Singleton(name = UserEntityService.BEAN_NAME)
@Startup
public class UserEntityServiceBean
	extends APreloadedComplexEntitiesService<UserEntity, User>
	implements UserEntityServiceLocal, UserEntityServiceRemote {

    // static finals

    private static final Function<Connection, List<User>> GET_ALL_FUNCTION = (con) -> con.getUsers().getUser();
    private static final Function<User, Integer> GET_ESBD_ID_FUNCTION = User::getID;

    // constructor

    protected UserEntityServiceBean() {
	super(UserEntityService.class, UserEntity.class, GET_ALL_FUNCTION, GET_ESBD_ID_FUNCTION);
    }

    // injected

    @EJB
    private UserEntityEsbdConverterBean converter;

    @Override
    protected UserEntityEsbdConverterBean getConverter() {
	return converter;
    }

}
