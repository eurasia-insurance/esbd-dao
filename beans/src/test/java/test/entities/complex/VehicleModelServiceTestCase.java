package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntity;
import tech.lapsa.esbd.dao.entities.complex.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntity;
import tech.lapsa.esbd.dao.entities.complex.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class VehicleModelServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleModelEntityServiceLocal service;

    private static final int VALID_ID = 143827;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final VehicleModelEntity res = service.getById(VALID_ID);
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

    private static final String VALID_NAME = "INFINITI FX35";

    @Test
    public void testGetByName() throws IllegalArgument {
	final List<VehicleModelEntity> list = service.getByName(VALID_NAME);
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleModelEntity e : list)
	    assertThat(e, not(nullValue()));
    }

    @Inject
    private VehicleManufacturerEntityServiceLocal manufacturers;

    private static final int VALID_MANUFACTURER_ID = 45755; // INFINTI

    @Test
    public void testGetByManufacturer() throws NotFound, IllegalArgument {
	final VehicleManufacturerEntity validManufacturer = manufacturers.getById(VALID_MANUFACTURER_ID);
	final List<VehicleModelEntity> list = service.getByManufacturer(validManufacturer);
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleModelEntity e : list)
	    assertThat(e, not(nullValue()));
    }
}
