package test.entities;

import static test.entities.TestConstants.*;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.junit.Test;

import tech.lapsa.insurance.esbd.NotFound;
import tech.lapsa.insurance.esbd.entities.PolicyEntityService;
import test.ArquillianBaseTestCase;

public class PolicyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private PolicyEntityService service;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NamingException, NotFound {
	service.getById(INVALID_POLICY_ID);
    }

    @Test(expected = NotFound.class)
    public void testGetByPolicyNumber_NotFound() throws NamingException, NotFound {
	service.getByNumber(INVALID_POLICY_NUMBER);
    }
}
