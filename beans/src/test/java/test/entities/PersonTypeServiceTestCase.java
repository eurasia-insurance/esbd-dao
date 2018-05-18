package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.dict.PersonTypeEntity;
import tech.lapsa.esbd.dao.dict.PersonTypeEntityService.PersonTypeEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class PersonTypeServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private PersonTypeEntityServiceLocal service;

    @Test
    public void testGetAll() {
	final List<PersonTypeEntity> all = service.getAll();
	assertThat(all,
		allOf(
			not(nullValue()),
			not(empty())));
    }

    @Test
    public void testGetById() throws IllegalArgument {
	final List<PersonTypeEntity> list = service.getAll();
	for (final PersonTypeEntity i : list)
	    try {
		final PersonTypeEntity res = service.getById(i.getId());
		assertThat(res, allOf(not(nullValue()), is(i)));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    public static final int INVALID_PERSON_TYPE_ID = 999999999;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_PERSON_TYPE_ID);
    }

}
