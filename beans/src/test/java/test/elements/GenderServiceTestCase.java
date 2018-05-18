package test.elements;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.mapping.SexMapping;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class SexServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private GenderServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	for (final int id : SexMapping.getInstance().getAllIds())
	    try {
		final Sex res = service.getById(id);
		assertThat(res, allOf(not(nullValue()), equalTo(SexMapping.getInstance().forId(id))));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_SEX_ID);
    }

}
