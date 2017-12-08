package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.junit.Test;

import tech.lapsa.insurance.esbd.NotFound;
import tech.lapsa.insurance.esbd.dict.CompanyActivityKindEntity;
import tech.lapsa.insurance.esbd.dict.CompanyActivityKindEntityService;
import test.ArquillianBaseTestCase;

public class CompanyActivityKindServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private CompanyActivityKindEntityService service;

    @Test
    public void testGetAll() throws NamingException {
	List<CompanyActivityKindEntity> all = service.getAll();
	assertThat(all,
		allOf(
			not(nullValue()),
			not(empty())));
    }

    @Test
    public void testGetById() throws NamingException {
	List<CompanyActivityKindEntity> list = service.getAll();
	for (CompanyActivityKindEntity i : list) {
	    try {
		CompanyActivityKindEntity res = service.getById(i.getId());
		assertThat(res, allOf(not(nullValue()), is(i)));
	    } catch (NotFound e) {
		fail(e.getMessage());
	    }
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NamingException, NotFound {
	service.getById(INVALID_COMPANY_ACTIVITY_KIND_ID);
    }

}
