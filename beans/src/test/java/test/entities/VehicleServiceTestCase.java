package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.domain.entities.VehicleEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.vehicle.VehicleRegNumber;
import test.ArquillianBaseTestCase;

public class VehicleServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleEntityServiceLocal service;

    private static final int VALID_VEHICLE_ID = 9187329; // Vadims Car
							 // Infiniti

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final VehicleEntity entity = service.getById(VALID_VEHICLE_ID);
	    assertThat(entity, not(nullValue()));
	    assertThat(entity.getId(), is(VALID_VEHICLE_ID));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int INVALID_VEHICLE_ID = 999999999;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_VEHICLE_ID);
    }

    private static final String VALID_VEHICLE_VIN_CODE = "JN1TANS51U0303376"; // Vadims
									      // Car
									      // Infiniti
									      // FX
									      // 35

    @Test
    public void testGetByVIN() throws IllegalArgument {
	final List<VehicleEntity> entities = service.getByVINCode(VALID_VEHICLE_VIN_CODE);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final VehicleRegNumber VALID_VEHICLE_REG_NUMBER1 = VehicleRegNumber.of("082KBA02");

    @Test
    public void testGetByRegNumber1() throws IllegalArgument {
	final List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER1);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final VehicleRegNumber VALID_VEHICLE_REG_NUMBER2 = VehicleRegNumber.of("A105YRO");

    @Test
    public void testGetByRegNumber2() throws IllegalArgument {
	final List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER2);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final VehicleRegNumber VALID_VEHICLE_REG_NUMBER3 = VehicleRegNumber.of("357ONA02");

    @Test
    public void testGetByRegNumber3() throws IllegalArgument {
	final List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER3);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }
}
