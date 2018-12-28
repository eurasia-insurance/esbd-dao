package test.elements;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.elements.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.domain.entities.SubjectPersonEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class InsuranceClassTypeTestCase extends ArquillianBaseTestCase {

    @Inject
    private InsuranceClassTypeServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final InsuranceClassType res = service.getById(VALID_INSURANCE_CLASS_TYPE_ID);
	    assertThat(res, allOf(not(nullValue()), equalTo(InsuranceClassType.CLASS_12)));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_INSURANCE_CLASS_TYPE_ID);
    }

    @Test
    public void testGetByCode() throws IllegalArgument {
	final InsuranceClassType[] list = InsuranceClassType.values();
	final Set<String> codes = InsuranceClassTypeMapping.getInstance().getAllIds();
	for (final String id : codes)
	    try {
		final InsuranceClassType res = service.getByCode(id);
		assertThat(res, allOf(not(nullValue()), isIn(list)));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetByCode_NotFound() throws NotFound, IllegalArgument {
	service.getByCode(INVALID_INSURANCE_CLASS_TYPE_CODE);
    }

    @Inject
    private SubjectPersonEntityServiceLocal persons;

    @Test
    public void testGetForSubject() throws IllegalArgument {
	try {
	    final SubjectPersonEntity e = persons.getById(VALID_SUBJECT_PERSON_ID);
	    final InsuranceClassType res = service.getForSubject(e);
	    assertThat(res, allOf(not(nullValue()), equalTo(VALID_CLASS_TYPE_FOR_CLIENT)));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }
}
