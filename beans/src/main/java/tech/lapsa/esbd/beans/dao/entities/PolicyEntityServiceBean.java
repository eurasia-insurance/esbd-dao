package tech.lapsa.esbd.beans.dao.entities;

import static tech.lapsa.esbd.beans.dao.ESBDDates.convertESBDDateToLocalDate;
import static tech.lapsa.esbd.beans.dao.entities.Util.optField;
import static tech.lapsa.esbd.beans.dao.entities.Util.optFieldIgnoreFieldNotFound;
import static tech.lapsa.esbd.beans.dao.entities.Util.reqField;

import java.util.List;
import java.util.stream.Stream;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lapsa.insurance.elements.CancelationReason;
import com.lapsa.insurance.elements.InsuranceClassType;
import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;
import com.lapsa.insurance.elements.MaritalStatus;
import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;
import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.ESBDDates;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.elements.CancelationReasonService.CancelationReasonServiceLocal;
import tech.lapsa.esbd.dao.elements.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;
import tech.lapsa.esbd.dao.elements.KZAreaService.KZAreaServiceLocal;
import tech.lapsa.esbd.dao.elements.MaritalStatusService.MaritalStatusServiceLocal;
import tech.lapsa.esbd.dao.elements.VehicleAgeClassService.VehicleAgeClassServiceLocal;
import tech.lapsa.esbd.dao.elements.VehicleClassService.VehicleClassServiceLocal;
import tech.lapsa.esbd.dao.entities.PolicyEntityService;
import tech.lapsa.esbd.dao.entities.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.PolicyEntityService.PolicyEntityServiceRemote;
import tech.lapsa.esbd.dao.entities.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.VehicleEntityService.VehicleEntityServiceLocal;
import tech.lapsa.esbd.domain.dict.BranchEntity;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.domain.entities.DriverLicenseInfo;
import tech.lapsa.esbd.domain.entities.GPWParticipantInfo;
import tech.lapsa.esbd.domain.entities.HandicappedInfo;
import tech.lapsa.esbd.domain.entities.InsuredDriverEntity;
import tech.lapsa.esbd.domain.entities.InsuredDriverEntity.InsuredDriverEntityBuilder;
import tech.lapsa.esbd.domain.entities.InsuredVehicleEntity;
import tech.lapsa.esbd.domain.entities.InsuredVehicleEntity.InsuredVehicleEntityBuilder;
import tech.lapsa.esbd.domain.entities.PensionerInfo;
import tech.lapsa.esbd.domain.entities.PolicyEntity;
import tech.lapsa.esbd.domain.entities.PolicyEntity.PolicyEntityBuilder;
import tech.lapsa.esbd.domain.entities.PrivilegerInfo;
import tech.lapsa.esbd.domain.entities.RecordOperationInfo;
import tech.lapsa.esbd.domain.entities.SubjectEntity;
import tech.lapsa.esbd.domain.entities.SubjectPersonEntity;
import tech.lapsa.esbd.domain.entities.UserEntity;
import tech.lapsa.esbd.domain.entities.VehicleCertificateInfo;
import tech.lapsa.esbd.domain.entities.VehicleEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfDriver;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPoliciesTF;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPolicy;
import tech.lapsa.esbd.jaxws.wsimport.Driver;
import tech.lapsa.esbd.jaxws.wsimport.PoliciesTF;
import tech.lapsa.esbd.jaxws.wsimport.Policy;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = PolicyEntityService.BEAN_NAME)
public class PolicyEntityServiceBean implements PolicyEntityServiceLocal, PolicyEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
            .withNameOf(PolicyEntityService.class) //
            .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @CacheResult
    public PolicyEntity getById(@CacheKey final Integer id) throws NotFound, IllegalArgument {
        try {
            return _getById(id);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgument(e);
        } catch (final RuntimeException e) {
            logger.WARN.log(e);
            throw new EJBException(e.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @CacheResult
    public PolicyEntity getByNumber(@CacheKey final String number) throws NotFound, IllegalArgument {
        try {
            return _getByNumber(number);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgument(e);
        } catch (final RuntimeException e) {
            logger.WARN.log(e);
            throw new EJBException(e.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @CacheResult
    public List<PolicyEntity> getByInternalNumber(@CacheKey final String internalNumber) throws IllegalArgument {
        try {
            return _getByInternalNumber(internalNumber);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgument(e);
        } catch (final RuntimeException e) {
            logger.WARN.log(e);
            throw new EJBException(e.getMessage());
        }
    }

    // PRIVATE

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @EJB
    private SubjectEntityServiceLocal subjectService;

    @EJB
    private CancelationReasonServiceLocal cancelationReasonTypeService;

    @EJB
    private BranchEntityServiceLocal branchService;

    @EJB
    private InsuredAgeAndExpirienceClassServiceLocal driverExpirienceClassificationService;

    @EJB
    private InsuranceClassTypeServiceLocal insuranceClassTypeService;

    @EJB
    private MaritalStatusServiceLocal maritalStatusService;

    @EJB
    private UserEntityServiceLocal userService;

    @EJB
    private VehicleEntityServiceLocal vehicleService;

    @EJB
    private VehicleClassServiceLocal vehicleClassService;

    @EJB
    private VehicleAgeClassServiceLocal vehicleAgeClassService;

    @EJB
    private KZAreaServiceLocal countryRegionService;

    @EJB
    private ConnectionPool pool;

    private PolicyEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
        MyNumbers.requireNonZero(id, "id");

        final Policy source;
        try (Connection con = pool.getConnection()) {
            source = con.getPolicyByID(id);
        } catch (ConnectionException e) {
            throw new IllegalStateException(e.getMessage());
        }
        if (source == null)
            throw new NotFound(PolicyEntity.class.getSimpleName() + " not found with ID = '" + id + "'");

        return convertToBuilder(source).build();
    }

    private PolicyEntity _getByNumber(final String number) throws IllegalArgumentException, NotFound {
        MyStrings.requireNonEmpty(number, "number");

        final Policy source;
        try (Connection con = pool.getConnection()) {
            source = con.getPolicyByGlobalID(number);
        } catch (ConnectionException e) {
            throw new IllegalStateException(e.getMessage());
        }
        if (MyObjects.isNull(source))
            throw new NotFound(PolicyEntity.class.getSimpleName() + " not found with NUMBER = '" + number + "'");

        return convertToBuilder(source).build();
    }

    private List<PolicyEntity> _getByInternalNumber(final String internalNumber) throws IllegalArgumentException {
        MyStrings.requireNonEmpty(internalNumber, "internalNumber");

        final ArrayOfPolicy policies;
        try (Connection con = pool.getConnection()) {
            policies = con.getPoliciesByNumber(internalNumber);
        } catch (ConnectionException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return MyOptionals.of(policies) //
                .map(ArrayOfPolicy::getPolicy) //
                .map(List::stream) //
                .orElseGet(Stream::empty) //
                .map(this::convertToBuilder) //
                .map(PolicyEntityBuilder::build)
                .collect(MyCollectors.unmodifiableList());
    }

    PolicyEntityBuilder convertToBuilder(final Policy source) throws EJBException {
        try {

            final PolicyEntityBuilder builder = PolicyEntity.builder();

            final int id = source.getPOLICYID();

            {
                // POLICY_ID s:int Идентификатор полиса (обязательно)
                builder.withId(MyOptionals.of(id).orElse(null));
            }

            {
                // GLOBAL_ID s:string Уникальный глобальный идентификатор полиса
                builder.withNumber(source.getGLOBALID());
            }

            {
                // POLICY_NUMBER s:string Номер полиса (обязательно)
                builder.withInternalNumber(source.getPOLICYNUMBER());
            }

            {
                // DATE_BEG s:string Дата начала действия полиса (обязательно)
                builder.withValidFrom(convertESBDDateToLocalDate(source.getDATEBEG()));
            }

            {
                // DATE_END s:string Дата окончания действия полиса
                // (обязательно)
                builder.withValidTill(convertESBDDateToLocalDate(source.getDATEEND()));
            }

            {
                // PREMIUM s:double Страховая премия (обязательно)
                builder.withActualPremium(MyOptionals.of(source.getPREMIUM()).orElse(null));
            }

            {
                // CALCULATED_PREMIUM s:double Страховая премия рассчитанная
                // системой
                builder.withCalculatedPremium(MyOptionals.of(source.getCALCULATEDPREMIUM()).orElse(null));
            }

            {
                // SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
                builder.withInsurer(reqField(PolicyEntity.class, id, insuranceCompanyService::getById, "insurer",
                        InsuranceCompanyEntity.class, source.getSYSTEMDELIMITERID()));
            }

            {
                // CLIENT_ID s:int Идентификатор страхователя (обязательно)
                builder.withInsurant(reqField(PolicyEntity.class, id, subjectService::getById, "insurant",
                        SubjectEntity.class, source.getCLIENTID()));
            }

            {
                // POLICY_DATE s:string Дата полиса
                builder.withDateOfIssue(convertESBDDateToLocalDate(source.getPOLICYDATE()));
            }

            {
                // RESCINDING_DATE s:string Дата расторжения полиса
                // RESCINDING_REASON_ID s:int Идентификатор причины расторжения
                if (MyStrings.nonEmpty(source.getRESCINDINGDATE())
                        || MyNumbers.positive(source.getRESCINDINGREASONID()))
                    builder.withCancelation(ESBDDates.convertESBDDateToLocalDate(source.getRESCINDINGDATE()),
                            reqField(PolicyEntity.class, id, cancelationReasonTypeService::getById,
                                    "cancelationReasonType", CancelationReason.class, source.getRESCINDINGREASONID()));
            }

            {
                // BRANCH_ID s:int Филиал (обязательно)
                builder.withBranch(reqField(PolicyEntity.class, id, branchService::getById, "branch",
                        BranchEntity.class, source.getBRANCHID()));
            }

            {
                // REWRITE_BOOL s:int Признак переоформления
                // REWRITE_POLICY_ID s:int Ссылка на переоформляемый полис
                final boolean reissued = source.getREWRITEBOOL() == 1;
                if (reissued)
                    builder.withReissuedPolicyId(MyNumbers.requirePositive(source.getREWRITEPOLICYID()));
            }

            {
                // DESCRIPTION s:string Комментарии к полису
                builder.withComments(source.getDESCRIPTION());
            }

            {
                // Drivers tns:ArrayOfDriver Водители (обязательно)
                MyOptionals.of(source) //
                        .map(Policy::getDrivers) //
                        .map(ArrayOfDriver::getDriver) //
                        .map(List::stream) //
                        .orElseThrow(() -> Util.requireNonEmtyList(PolicyEntity.class, id, "InsuredDrivers")) //
                        .peek(x -> MyNumbers.requireEqualsMsg(id, x.getPOLICYID(),
                                "%1$s.POLICYID (%3$s) and %2$s.POLICYID (%4$s) are not equals", Policy.class.getName(),
                                Driver.class.getName(), id, x.getPOLICYID())) //
                        .map(this::convertToBuilder).forEach(builder::addDriverBuilder);
            }

            {
                // PoliciesTF tns:ArrayOfPolicies_TF Транспортные средства
                // полиса
                // (обязательно)
                MyOptionals.of(source) //
                        .map(Policy::getPoliciesTF) //
                        .map(ArrayOfPoliciesTF::getPoliciesTF) //
                        .map(List::stream) //
                        .orElseThrow(() -> Util.requireNonEmtyList(PolicyEntity.class, id, "InsuredVehicles")) //
                        .peek(x -> MyNumbers.requireEqualsMsg(id, x.getPOLICYID(),
                                "%1$s.POLICYID (%3$s) and %2$s.POLICYID (%4$s) are not equals", Policy.class.getName(),
                                PoliciesTF.class.getName(), id, x.getPOLICYID())) //
                        .map(this::convertToBuilder) //
                        .forEach(builder::addVehicleBuilder);
            }

            {
                // CREATED_BY_USER_ID s:int Идентификатор пользователя,
                // создавшего
                // полис
                // INPUT_DATE s:string Дата\время ввода полиса в систему
                RecordOperationInfo.builder()
                        .withDate(convertESBDDateToLocalDate(source.getINPUTDATE()))
                        .withAuthor(reqField(PolicyEntity.class, id, userService::getById, "created.author",
                                UserEntity.class, source.getCREATEDBYUSERID()))
                        .buildTo(builder::withCreated);
            }

            {
                // RECORD_CHANGED_AT s:string Дата\время изменения полиса
                // RECORD_CHANGED_AT_DATETIME s:string Дата\время изменения
                // полиса
                // CHANGED_BY_USER_ID s:int Идентификатор пользователя,
                // изменившего
                // полис
                if (MyStrings.nonEmpty(source.getRECORDCHANGEDAT()))
                    RecordOperationInfo.builder()
                            .withDate(convertESBDDateToLocalDate(source.getRECORDCHANGEDAT()))
                            .withAuthor(reqField(PolicyEntity.class, id, userService::getById, "modified.author",
                                    UserEntity.class, source.getCHANGEDBYUSERID()))
                            .buildTo(builder::withModified);
            }

            {
                builder.withPolicyDate(
                        MyOptionals.of(source.getPOLICYDATE()).map(ESBDDates::convertESBDDateToLocalDate));
            }

            // ScheduledPayments tns:ArrayOfSCHEDULED_PAYMENT Плановые платежи
            // по полису
            // PAYMENT_ORDER_TYPE_ID s:int Порядок оплаты (Идентификатор)
            // PAYMENT_ORDER_TYPE s:string Порядок оплаты
            {
                // PAYMENT_DATE s:string Дата оплаты
                builder.withPaymentDate(
                        MyOptionals.of(source.getPAYMENTDATE()).map(ESBDDates::convertESBDDateToLocalDate));
            }

            // MIDDLEMAN_ID s:int Посредник (Идентификатор)
            // MIDDLEMAN_CONTRACT_NUMBER s:string Номер договора посредника
            // CLIENT_FORM_ID s:int Форма клиента (справочник CLIENT_FORMS)

            return builder;

        } catch (final IllegalArgumentException e) {
            // it should not happens
            throw new EJBException(e.getMessage());
        }
    }

    InsuredDriverEntityBuilder convertToBuilder(final Driver source) {
        try {

            final InsuredDriverEntityBuilder builder = InsuredDriverEntity.builder();

            final int id = source.getPOLICYID();

            {
                // DRIVER_ID s:int Идентификатор водителя
                builder.withId(MyOptionals.of(source.getPOLICYID()).orElse(null));
            }

            {
                // POLICY_ID s:int Идентификатор полиса
            }

            {
                // CLIENT_ID s:int Идентификатор клиента (обязательно)
                final SubjectEntity insured = reqField(InsuredDriverEntity.class, id, subjectService::getById,
                        "insured", SubjectEntity.class, source.getCLIENTID());
                builder.withInsured(insured);

                // getClassId
                if (insured instanceof SubjectPersonEntity) {
                    builder.withInsuraceClassType(
                            reqField(InsuredDriverEntity.class, id, insuranceClassTypeService::getById,
                                    "insuraceClassType", InsuranceClassType.class, source.getClassId()));
                }
            }

            {
                // HOUSEHOLD_POSITION_ID s:int Идентификатор семейного положения
                builder.withMaritalStatus(optField(InsuredDriverEntity.class, id, maritalStatusService::getById,
                        "maritalStatus", MaritalStatus.class, MyOptionals.of(source.getHOUSEHOLDPOSITIONID())));
            }

            {
                // AGE_EXPERIENCE_ID s:int Идентификатор возраста\стажа вождения
                builder.withInsuredAgeExpirienceClass(reqField(InsuredDriverEntity.class, id,
                        driverExpirienceClassificationService::getById, "insuredAgeExpirienceClass",
                        InsuredAgeAndExpirienceClass.class, source.getAGEEXPERIENCEID()));
            }

            {
                // EXPERIENCE s:int Стаж вождения
                builder.withDrivingExpirience(source.getEXPERIENCE());
            }

            {
                // DRIVER_CERTIFICATE s:string Номер водительского удостоверения
                // DRIVER_CERTIFICATE_DATE s:string Дата выдачи водительского
                // удостоверения
                DriverLicenseInfo.builder() //
                        .withDateOfIssue(convertESBDDateToLocalDate(source.getDRIVERCERTIFICATEDATE())) //
                        .withNumber(source.getDRIVERCERTIFICATE()) //
                        .buildTo(builder::withDriverLicense);
            }

            {
                // PRIVELEGER_BOOL s:int Признак приравненного лица
                // PRIVELEDGER_TYPE s:string Тип приравненного лица
                // PRIVELEDGER_CERTIFICATE s:string Удостоверение приравненного
                // лица
                // PRIVELEDGER_CERTIFICATE_DATE s:string Дата выдачи
                // удостоверения
                // приравненного лица
                final boolean privileger = source.getPRIVELEGERBOOL() == 1;
                if (privileger)
                    PrivilegerInfo.builder() //
                            .withType(source.getPRIVELEDGERTYPE())
                            .withCertificateNumber(source.getPRIVELEDGERCERTIFICATE()) //
                            .withCertificateDateOfIssue(
                                    convertESBDDateToLocalDate(source.getPRIVELEDGERCERTIFICATEDATE())) //
                            .buildTo(builder::withPrivilegerInfo);
            }

            {
                // WOW_BOOL s:int Признак участника ВОВ
                // WOW_CERTIFICATE s:string Удостоверение участника ВОВ
                // WOW_CERTIFICATE_DATE s:string Дата выдачи удостоверения
                // участника
                // ВОВ
                final boolean gpwParticipant = source.getWOWBOOL() == 1;
                if (gpwParticipant)
                    GPWParticipantInfo.builder() //
                            .withCertificateDateOfIssue(convertESBDDateToLocalDate(source.getWOWCERTIFICATEDATE())) //
                            .withCertificateNumber(source.getWOWCERTIFICATE()) //
                            .buildTo(builder::withGpwParticipantInfo);
            }

            {
                // PENSIONER_BOOL s:int Признак пенсионера
                // PENSIONER_CERTIFICATE s:string Удостоверение пенсионера
                // PENSIONER_CERTIFICATE_DATE s:string Дата выдачи удостоверения
                // пенсионера
                final boolean pensioner = source.getPENSIONERBOOL() == 1;
                if (pensioner)
                    PensionerInfo.builder() //
                            .withCertificateNumber(source.getPENSIONERCERTIFICATE()) //
                            .withCertiticateDateOfIssue(
                                    convertESBDDateToLocalDate(source.getPENSIONERCERTIFICATEDATE())) //
                            .buildTo(builder::withPensionerInfo);
            }

            {
                // INVALID_BOOL s:int Признак инвалида
                // INVALID_CERTIFICATE s:string Удостоверение инвалида
                // INVALID_CERTIFICATE_BEG_DATE s:string Дата выдачи
                // удостоверения
                // инвалида
                // INVALID_CERTIFICATE_END_DATE s:string Дата завершения
                // удостоверения
                // инвалида
                final boolean handicapped = source.getINVALIDBOOL() == 1;
                if (handicapped)
                    HandicappedInfo.builder() //
                            .withCertificateNumber(source.getINVALIDCERTIFICATE()) //
                            .withCertificateValidFrom(convertESBDDateToLocalDate(source.getINVALIDCERTIFICATEBEGDATE())) //
                            .withCertificateValidTill(convertESBDDateToLocalDate(source.getINVALIDCERTIFICATEENDDATE())) //
                            .buildTo(builder::withHandicappedInfo);
            }

            {
                // CREATED_BY_USER_ID s:int Идентификатор пользователя,
                // создавшего
                // запись
                // INPUT_DATE s:string Дата\время ввода записи в систему
                RecordOperationInfo.builder()
                        .withDate(convertESBDDateToLocalDate(source.getINPUTDATE()))
                        .withAuthor(reqField(InsuredDriverEntity.class, id, userService::getById, "created.author",
                                UserEntity.class, source.getCREATEDBYUSERID()))
                        .buildTo(builder::withCreated);
            }

            {
                // RECORD_CHANGED_AT s:string Дата\время изменения записи
                // CHANGED_BY_USER_ID s:int Идентификатор пользователя,
                // изменившего
                // запись
                RecordOperationInfo.builder()
                        .withDate(convertESBDDateToLocalDate(source.getRECORDCHANGEDAT()))
                        .withAuthor(reqField(InsuredDriverEntity.class, id, userService::getById, "modified.author",
                                UserEntity.class, source.getCHANGEDBYUSERID()))
                        .buildTo(builder::withModified);
            }

            {
                // SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
                builder.withInsurer(reqField(InsuredDriverEntity.class, id, insuranceCompanyService::getById, "insurer",
                        InsuranceCompanyEntity.class, source.getSYSTEMDELIMITERID()));
            }
            return builder;

        } catch (final IllegalArgumentException e) {
            // it should not happens
            throw new EJBException(e.getMessage());
        }
    }

    InsuredVehicleEntityBuilder convertToBuilder(final PoliciesTF source) {
        try {

            final InsuredVehicleEntityBuilder builder = InsuredVehicleEntity.builder();

            final int id = source.getPOLICYTFID();

            {
                // POLICY_TF_ID s:int Идентификатор ТС полиса
                builder.withId(MyOptionals.of(id).orElse(null));
            }

            {
                // POLICY_ID s:int Идентификатор полиса
            }

            {
                // TF_ID s:int Идентификатор ТС
                builder.withVehicle(optFieldIgnoreFieldNotFound(InsuredVehicleEntity.class, id, vehicleService::getById,
                        "vehicle", VehicleEntity.class, MyOptionals.of(source.getTFID()), logger.WARN));
            }

            {
                // TF_TYPE_ID s:int Идентификатор типа ТС (обязательно)
                builder.withVehicleClass(reqField(InsuredVehicleEntity.class, id, vehicleClassService::getById,
                        "vehicleClass", VehicleClass.class, source.getTFTYPEID()));
            }

            {
                // TF_AGE_ID s:int Идентификатор возраста ТС (обязательно)
                builder.withVehicleAgeClass(reqField(InsuredVehicleEntity.class, id, vehicleAgeClassService::getById,
                        "vehicleAgeClass", VehicleAgeClass.class, source.getTFAGEID()));
            }

            {
                // TF_NUMBER s:string Гос. номер ТС
                // TF_REGISTRATION_CERTIFICATE s:string Номер тех. паспорта
                // GIVE_DATE s:string Дата выдачи тех. паспорта
                // REGION_ID s:int Идентификатор региона регистрации ТС
                // (обязательно)
                // BIG_CITY_BOOL s:int Признак города областного значения
                // (обязательно)
                VehicleCertificateInfo.builder() //
                        .withCertificateNumber(source.getTFREGISTRATIONCERTIFICATE())
                        .withDateOfIssue(convertESBDDateToLocalDate(source.getGIVEDATE()))
                        .withRegistrationMajorCity(source.getBIGCITYBOOL() == 1)
                        .withRegistrationRegion(reqField(InsuredVehicleEntity.class, id, countryRegionService::getById,
                                "certificate.registrationRegion", KZArea.class, source.getREGIONID()))
                        .withRegistrationNumber(source.getTFNUMBER()).buildTo(builder::withCertificate);
            }

            {
                // PURPOSE s:string Цель использования ТС
                builder.withVehiclePurpose(source.getPURPOSE());
            }

            {
                // ODOMETER s:int Показания одометра
                builder.withCurrentOdometerValue(source.getODOMETER());
            }

            {
                // CREATED_BY_USER_ID s:int Идентификатор пользователя,
                // создавшего
                // запись
                // INPUT_DATE s:string Дата\время ввода записи в систему
                RecordOperationInfo.builder()
                        .withDate(convertESBDDateToLocalDate(source.getINPUTDATE()))
                        .withAuthor(reqField(InsuredVehicleEntity.class, id, userService::getById, "created.author",
                                UserEntity.class, source.getCREATEDBYUSERID()))
                        .buildTo(builder::withCreated);
            }

            {
                // RECORD_CHANGED_AT s:string Дата\время изменения записи
                // CHANGED_BY_USER_ID s:int Идентификатор пользователя,
                // изменившего
                // запись
                if (MyStrings.nonEmpty(source.getRECORDCHANGEDAT()))
                    RecordOperationInfo.builder()
                            .withDate(convertESBDDateToLocalDate(source.getRECORDCHANGEDAT()))
                            .withAuthor(reqField(InsuredVehicleEntity.class, id, userService::getById,
                                    "modified.author", UserEntity.class, source.getCHANGEDBYUSERID()))
                            .buildTo(builder::withModified);
            }

            {
                // SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
                builder.withInsurer(reqField(InsuredVehicleEntity.class, id, insuranceCompanyService::getById,
                        "insurer", InsuranceCompanyEntity.class, source.getSYSTEMDELIMITERID()));
            }

            return builder;

        } catch (final IllegalArgumentException e) {
            // it should not happens
            throw new EJBException(e.getMessage());
        }
    }
}
