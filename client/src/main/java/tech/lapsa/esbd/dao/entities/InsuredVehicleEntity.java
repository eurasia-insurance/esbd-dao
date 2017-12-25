package tech.lapsa.esbd.dao.entities;

import com.lapsa.insurance.elements.VehicleAgeClass;
import com.lapsa.insurance.elements.VehicleClass;

import tech.lapsa.esbd.dao.Domain;
import tech.lapsa.esbd.dao.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.infos.RecordOperationInfo;
import tech.lapsa.esbd.dao.infos.VehicleCertificateInfo;
import tech.lapsa.patterns.domain.HashCodePrime;

@HashCodePrime(13)
public class InsuredVehicleEntity extends Domain {

    private static final long serialVersionUID = 1L;

    // POLICY_TF_ID s:int Идентификатор ТС полиса
    private Integer id;

    // POLICY_ID s:int Идентификатор полиса
    private PolicyEntity policy;

    // TF_ID s:int Идентификатор ТС
    private VehicleEntity vehicle;

    // TF_TYPE_ID s:int Идентификатор типа ТС (обязательно)
    private VehicleClass vehicleClass;

    // TF_AGE_ID s:int Идентификатор возраста ТС (обязательно)
    private VehicleAgeClass vehicleAgeClass;

    // TF_NUMBER s:string Гос. номер ТС
    // TF_REGISTRATION_CERTIFICATE s:string Номер тех. паспорта
    // GIVE_DATE s:string Дата выдачи тех. паспорта
    // REGION_ID s:int Идентификатор региона регистрации ТС (обязательно)
    // BIG_CITY_BOOL s:int Признак города областного значения (обязательно)
    private VehicleCertificateInfo certificate = new VehicleCertificateInfo();

    // PURPOSE s:string Цель использования ТС
    private String vehiclePurpose;

    // ODOMETER s:int Показания одометра
    private long currentOdometerValue;

    // CREATED_BY_USER_ID s:int Идентификатор пользователя, создавшего запись
    // INPUT_DATE s:string Дата\время ввода записи в систему
    private RecordOperationInfo created = new RecordOperationInfo();

    // RECORD_CHANGED_AT s:string Дата\время изменения записи
    // CHANGED_BY_USER_ID s:int Идентификатор пользователя, изменившего запись
    private RecordOperationInfo modified = new RecordOperationInfo();

    // SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
    private InsuranceCompanyEntity insurer;

    // GENERATED

    public Integer getId() {
	return id;
    }

    public void setId(final Integer id) {
	this.id = id;
    }

    public PolicyEntity getPolicy() {
	return policy;
    }

    public void setPolicy(final PolicyEntity policy) {
	this.policy = policy;
    }

    public VehicleEntity getVehicle() {
	return vehicle;
    }

    public void setVehicle(final VehicleEntity vehicle) {
	this.vehicle = vehicle;
    }

    public VehicleClass getVehicleClass() {
	return vehicleClass;
    }

    public void setVehicleClass(final VehicleClass vehicleClass) {
	this.vehicleClass = vehicleClass;
    }

    public VehicleAgeClass getVehicleAgeClass() {
	return vehicleAgeClass;
    }

    public void setVehicleAgeClass(final VehicleAgeClass vehicleAgeClass) {
	this.vehicleAgeClass = vehicleAgeClass;
    }

    public VehicleCertificateInfo getCertificate() {
	return certificate;
    }

    public void setCertificate(final VehicleCertificateInfo certificate) {
	this.certificate = certificate;
    }

    public String getVehiclePurpose() {
	return vehiclePurpose;
    }

    public void setVehiclePurpose(final String vehiclePurpose) {
	this.vehiclePurpose = vehiclePurpose;
    }

    public long getCurrentOdometerValue() {
	return currentOdometerValue;
    }

    public void setCurrentOdometerValue(final long currentOdometerValue) {
	this.currentOdometerValue = currentOdometerValue;
    }

    public RecordOperationInfo getCreated() {
	return created;
    }

    public void setCreated(final RecordOperationInfo created) {
	this.created = created;
    }

    public RecordOperationInfo getModified() {
	return modified;
    }

    public void setModified(final RecordOperationInfo modified) {
	this.modified = modified;
    }

    public InsuranceCompanyEntity getInsurer() {
	return insurer;
    }

    public void setInsurer(final InsuranceCompanyEntity insurer) {
	this.insurer = insurer;
    }

}
