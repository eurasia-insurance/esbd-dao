package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.complex.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectPersonServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectPersonEntityServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (final int valid : VALID_SUBJECT_PERSON_IDS) {
		final SubjectPersonEntity res = service.getById(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testGetById_NotFound() throws IllegalArgument {
	for (final int invalid : INVALID_SUBJECT_PERSON_IDS)
	    try {
		service.getById(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

    public static final TaxpayerNumber[] VALID_SUBJECT_PERSON_IINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("581114350286"), TaxpayerNumber.of("870622300359") };

    @Test
    public void testGetByIIN() throws IllegalArgument {
	try {
	    for (final TaxpayerNumber valid : VALID_SUBJECT_PERSON_IINS) {
		final SubjectPersonEntity res = service.getFirstByIdNumber(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    public static final TaxpayerNumber[] INVALID_SUBJECT_PERSON_IINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071") };

    @Test
    public void testGetByIIN_NotFound() throws IllegalArgument {
	for (final TaxpayerNumber invalid : INVALID_SUBJECT_PERSON_IINS)
	    try {
		service.getFirstByIdNumber(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

}
