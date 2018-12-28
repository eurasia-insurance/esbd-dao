package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.entities.SubjectCompanyEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectCompanyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectCompanyEntityServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (final int valid : VALID_SUBJECT_COMPANY_IDS) {
		final SubjectCompanyEntity res = service.getById(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testGetById_NotFound() throws IllegalArgument {
	for (final int invalid : INVALID_SUBJECT_COMPANY_IDS)
	    try {
		service.getById(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

    public static final TaxpayerNumber[] VALID_SUBJECT_COMPANY_BINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071") };

    @Test
    public void testGetByBIN() throws IllegalArgument {
	try {
	    for (final TaxpayerNumber valid : VALID_SUBJECT_COMPANY_BINS) {
		final SubjectCompanyEntity res = service.getFirstByIdNumber(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    public static final TaxpayerNumber[] INVALID_SUBJECT_COMPANY_BINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("581114350286") };

    @Test
    public void testGetByBIN_NotFound() throws IllegalArgument {
	for (final TaxpayerNumber invalid : INVALID_SUBJECT_COMPANY_BINS)
	    try {
		service.getFirstByIdNumber(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }
}
