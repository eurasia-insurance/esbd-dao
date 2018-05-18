package test.elements.dict;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.elements.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class InsuranceClassTypeTestCase extends ArquillianBaseTestCase {

    private static final int VALID_INSURANCE_CLASS_TYPE_ID = 14;

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

    private static final int INVALID_INSURANCE_CLASS_TYPE_ID = 999999999;

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

    private static final String INVALID_INSURANCE_CLASS_TYPE_CODE = "QQ";

    @Test(expected = NotFound.class)
    public void testGetByCode_NotFound() throws NotFound, IllegalArgument {
	service.getByCode(INVALID_INSURANCE_CLASS_TYPE_CODE);
    }

    @Inject
    private SubjectPersonEntityServiceLocal persons;

    private static final int VALID_SUBJECT_PERSON_ID = 14132412; // Evsyukovs ID
    private static final InsuranceClassType VALID_CLASS_TYPE_FOR_CLIENT = InsuranceClassType.CLASS_13; // Evsyukovs
												       // Class

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
