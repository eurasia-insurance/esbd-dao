package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.VehicleManufacturerEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class VehicleManufacturerServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleManufacturerEntityServiceLocal service;

    private static final int VALID_ID = 45755; // INFINTI

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final VehicleManufacturerEntity res = service.getById(VALID_ID);
	    assertThat(res, not(nullValue()));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int ININVALID_ID = 999999999;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(ININVALID_ID);
    }

    private static final String VALID_NAME = "INFINITI FX";

    @Test
    public void testGetByName() throws IllegalArgument {
	final List<VehicleManufacturerEntity> list = service.getByName(VALID_NAME);
	assertThat(list, allOf(not(nullValue()), not(empty())));
    }

    private static final String INVALID_NAME = "QQQXXAA";

    @Test
    public void testGetByName_empty() throws IllegalArgument {
	final List<VehicleManufacturerEntity> list = service.getByName(INVALID_NAME);
	assertThat(list, allOf(not(nullValue()), empty()));
    }

}
