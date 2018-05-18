package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class CompanyActivityKindServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private CompanyActivityKindEntityServiceLocal service;

    @Test
    public void testGetAll() {
	final List<CompanyActivityKindEntity> all = service.getAll();
	assertThat(all,
		allOf(
			not(nullValue()),
			not(empty())));
    }

    @Test
    public void testGetById() throws IllegalArgument {
	final List<CompanyActivityKindEntity> list = service.getAll();
	for (final CompanyActivityKindEntity i : list)
	    try {
		final CompanyActivityKindEntity res = service.getById(i.getId());
		assertThat(res, allOf(not(nullValue()), is(i)));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_COMPANY_ACTIVITY_KIND_ID);
    }

}
