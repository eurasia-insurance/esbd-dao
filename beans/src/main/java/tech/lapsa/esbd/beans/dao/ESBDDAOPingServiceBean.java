package tech.lapsa.esbd.beans.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.ESBDDAOPingService;
import tech.lapsa.esbd.dao.ESBDDAOPingService.ESBDDAOPingServiceLocal;
import tech.lapsa.esbd.dao.ESBDDAOPingService.ESBDDAOPingServiceRemote;
import tech.lapsa.java.commons.exceptions.IllegalState;
import tech.lapsa.java.commons.function.MyExceptions;

@Stateless(name = ESBDDAOPingService.BEAN_NAME)
public class ESBDDAOPingServiceBean implements ESBDDAOPingServiceLocal, ESBDDAOPingServiceRemote {

	// PRIVATE

	@EJB
	private ConnectionPool pool;

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ping() throws IllegalState {
		try (Connection con = pool.getConnection()) {
		} catch (final ConnectionException ce) {
			throw MyExceptions.format(IllegalState::new, "Can't gen connection to ESBD with message %1$s",
			        ce.getMessage());
		}

	}

}
