package test.entities.dict;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class InsuranceCompanyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private InsuranceCompanyEntityServiceLocal service;

    @Test
    public void testGetAll() {
	final List<InsuranceCompanyEntity> all = service.getAll();
	assertThat(all,
		allOf(
			not(nullValue()),
			not(empty())));
    }

    @Test
    public void testGetById() throws IllegalArgument {
	final List<InsuranceCompanyEntity> list = service.getAll();
	for (final InsuranceCompanyEntity i : list) {
	    System.out.println(i.getId() + " '" + i.getName() + "'");
	    try {
		final InsuranceCompanyEntity res = service.getById(i.getId());
		assertThat(res, allOf(not(nullValue()), is(i)));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
	}
    }

    private static final int INVALID_ID = 999999999;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_ID);
    }

}
