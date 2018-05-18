package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class InsuranceAgentServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private InsuranceAgentEntityServiceLocal service;

    private static final int VALID_ID = 34655; // Vadims Car
					       // Infiniti

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final InsuranceAgentEntity entity = service.getById(VALID_ID);
	    assertThat(entity, not(nullValue()));
	    assertThat(entity.getId(), is(VALID_ID));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int INVALID_ID = 999999999;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_ID);
    }
}
