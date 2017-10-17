package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.junit.Test;

import com.lapsa.insurance.esbd.domain.entities.policy.VehicleEntity;
import com.lapsa.insurance.esbd.services.NotFound;
import com.lapsa.insurance.esbd.services.policy.VehicleServiceDAO;

import test.ArquillianBaseTestCase;

public class VehicleServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleServiceDAO service;

    private static final long VALID_VEHICLE_ID = 9187329L; // Vadims Car
							   // Infiniti

    @Test
    public void testGetById() throws NamingException {
	try {
	    VehicleEntity entity = service.getById(VALID_VEHICLE_ID);
	    assertThat(entity, not(nullValue()));
	    assertThat(entity.getId(), is(VALID_VEHICLE_ID));
	} catch (NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final long INVALID_VEHICLE_ID = 999999999L;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NamingException, NotFound {
	service.getById(INVALID_VEHICLE_ID);
    }

    private static final String VALID_VEHICLE_VIN_CODE = "JN1TANS51U0303376"; // Vadims
									      // Car
									      // Infiniti
									      // FX
									      // 35

    @Test
    public void testGetByVIN() throws NamingException {
	List<VehicleEntity> entities = service.getByVINCode(VALID_VEHICLE_VIN_CODE);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final String VALID_VEHICLE_REG_NUMBER1 = "082KBA02";

    @Test
    public void testGetByRegNumber1() throws NamingException {
	List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER1);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final String VALID_VEHICLE_REG_NUMBER2 = "A105YRO";

    @Test
    public void testGetByRegNumber2() throws NamingException {
	List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER2);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }

    private static final String VALID_VEHICLE_REG_NUMBER3 = "357ONA02";

    @Test
    public void testGetByRegNumber3() throws NamingException {
	List<VehicleEntity> entities = service.getByRegNumber(VALID_VEHICLE_REG_NUMBER3);
	assertThat(entities, not(allOf(nullValue(), empty())));
	entities.forEach(System.out::println);
    }
}
