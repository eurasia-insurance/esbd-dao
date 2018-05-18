package test.entities.dict;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.complex.TestConstants.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
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
	for (final InsuranceCompanyEntity i : list)
	    try {
		final InsuranceCompanyEntity res = service.getById(i.getId());
		assertThat(res, allOf(not(nullValue()), is(i)));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_INSURANCE_COMPANY_ID);
    }

}
