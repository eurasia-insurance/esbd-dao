package test.entities.complex;

import com.lapsa.insurance.elements.SubjectType;

import tech.lapsa.esbd.dao.entities.SubjectCompanyEntity;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntity;

public final class TestConstants {
    // Branch
    public static final int INVALID_BRANCH_ID = 999999999;

    // CompanyActivityKind
    public static final int INVALID_COMPANY_ACTIVITY_KIND_ID = 999999999;

    // InsuranceCompany
    public static final int INVALID_INSURANCE_COMPANY_ID = 999999999;

    // User
    public static final int INVALID_USER_ID = 999999999;

    // VehicleManufacturer
    public static final int VALID_VEHICLE_MANUFACTURER_ID = 45755; // INFINTI
								   // FX35
    public static final String VALID_VEHICLE_MANUFACTURER_NAME = "INFINTI FX35";
    public static final int ININVALID_VEHICLE_MANUFACTURER_ID = 999999999;
    public static final String INVALID_VEHICLE_MANUFACTURER_NAME = "QQQ";

    // VehicleModel
    public static final int VALID_VEHICLE_MODEL_ID = 143827;
    public static final String VALID_VEHICLE_MODEL_NAME = "INFINITI FX35";
    public static final int ININVALID_VEHICLE_MODEL_ID = 999999999;

    // Subject
    public static final int[] VALID_SUBJECT_IDS = new int[] { 1, 100 };
    public static final SubjectType[] VALID_SUBJECT_TYPES = new SubjectType[] {
	    SubjectType.COMPANY,
	    SubjectType.PERSON };
    public static final Class<?>[] VALID_SUBJECT_CLASSES = new Class<?>[] {
	    SubjectCompanyEntity.class,
	    SubjectPersonEntity.class };

    public static final int INVALID_SUBJECT_ID = -1;

    // SubjectCompany
    public static final int[] VALID_SUBJECT_COMPANY_IDS = new int[] { 1, 2 };
    public static final int[] INVALID_SUBJECT_COMPANY_IDS = new int[] { 100, -1 };

    // SubjectPerson
    public static final int[] VALID_SUBJECT_PERSON_IDS = new int[] { 100, 14132412 };
    public static final int[] INVALID_SUBJECT_PERSON_IDS = new int[] { 1, 2, -1 };

    // Policy
    public static final Integer INVALID_POLICY_ID = 1;
    public static final String INVALID_POLICY_NUMBER = "ZZZ";
}
