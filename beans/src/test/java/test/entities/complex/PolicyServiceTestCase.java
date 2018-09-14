package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.PolicyEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class PolicyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private PolicyEntityServiceLocal service;

    private static final String VALID_NUMBER = "28814668809O";

    @Test
    public void testGetByNumber() throws IllegalArgument, NotFound {
	PolicyEntity value = service.getByNumber(VALID_NUMBER);
	assertThat(value, not(nullValue()));
    }

    private static final Integer INVALID_ID = 1;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_ID);
    }

    private static final String INVALID_POLICY_NUMBER = "ZZZ";

    @Test(expected = NotFound.class)
    public void testGetByPolicyNumber_NotFound() throws NotFound, IllegalArgument {
	service.getByNumber(INVALID_POLICY_NUMBER);
    }
}
