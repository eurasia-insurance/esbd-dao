package test.entities.complex;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.complex.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.PolicyEntity;
import tech.lapsa.esbd.dao.entities.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class PolicyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private PolicyEntityServiceLocal service;

    @Test
    public void testGetByNumber() throws IllegalArgument, NotFound {
	final String VALID_POLICY_NUMBER = "28814668809O";
	PolicyEntity value = service.getByNumber(VALID_POLICY_NUMBER);
	assertThat(value, not(nullValue()));
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_POLICY_ID);
    }

    @Test(expected = NotFound.class)
    public void testGetByPolicyNumber_NotFound() throws NotFound, IllegalArgument {
	service.getByNumber(INVALID_POLICY_NUMBER);
    }
}
