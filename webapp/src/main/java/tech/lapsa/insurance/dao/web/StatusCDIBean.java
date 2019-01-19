package tech.lapsa.insurance.dao.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.connection.ConnectionStatus;

@Named("status")
@RequestScoped
@RolesAllowed("role-admin")
public class StatusCDIBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ConnectionPool pool;

    public List<ConnectionStatus> getConnectionStatuses() {
	return pool.getPoolStatus();
    }
}
