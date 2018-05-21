package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.CancelationReason;
import com.lapsa.insurance.elements.PaymentType;
import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.dao.elements.dict.CancelationReasonService.CancelationReasonServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.PaymentTypeService.PaymentTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.PersonTypeService.PersonTypeServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyDriverEntity;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntity;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntity.PolicyEntityBuilder;
import tech.lapsa.esbd.dao.entities.complex.PolicyVehicleEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntity;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.BranchEntity;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.embeded.CancelationInfo;
import tech.lapsa.esbd.dao.entities.embeded.CancelationInfo.CancelationInfoBuilder;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo;
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo.RecordOperationInfoBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfDriver;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPoliciesTF;
import tech.lapsa.esbd.jaxws.wsimport.Driver;
import tech.lapsa.esbd.jaxws.wsimport.PoliciesTF;
import tech.lapsa.esbd.jaxws.wsimport.Policy;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless
@LocalBean
public class PolicyEntityEsbdConverterBean implements AEsbdAttributeConverter<PolicyEntity, Policy> {

    @Override
    public Policy convertToEsbdValue(PolicyEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PolicyEntity convertToEntityAttribute(Policy source) throws EsbdConversionException {
	try {

	    final PolicyEntityBuilder builder = PolicyEntity.builder();

	    final int id = source.getPOLICYID();

	    {
		// POLICY_ID s:int Идентификатор полиса (обязательно)
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// GLOBAL_ID s:string Уникальный глобальный идентификатор полиса
		MyOptionals.of(source.getGLOBALID())
			.ifPresent(builder::withNumber);
	    }

	    {
		// POLICY_NUMBER s:string Номер полиса (обязательно)
		MyOptionals.of(source.getPOLICYNUMBER())
			.ifPresent(builder::withInternalNumber);
	    }

	    {
		// DATE_BEG s:string Дата начала действия полиса (обязательно)
		MyOptionals.of(source.getDATEBEG())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(builder::withValidFrom);
	    }

	    {
		// DATE_END s:string Дата окончания действия полиса
		// (обязательно)
		MyOptionals.of(source.getDATEEND())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(builder::withValidTill);
	    }

	    {
		// PREMIUM s:double Страховая премия (обязательно)
		MyOptionals.of(source.getPREMIUM())
			.ifPresent(builder::withActualPremium);
	    }

	    {
		// CALCULATED_PREMIUM s:double Страховая премия рассчитанная
		// системой
		MyOptionals.of(source.getCALCULATEDPREMIUM())
			.ifPresent(builder::withCalculatedPremium);
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Идентификатор страховой компании
		optField(PolicyEntity.class,
			id,
			insuranceCompanyService::getById,
			"insurer",
			InsuranceCompanyEntity.class,
			MyOptionals.of(source.getSYSTEMDELIMITERID()))
				.ifPresent(builder::withInsurer);
	    }

	    {
		// CLIENT_ID s:int Идентификатор страхователя (обязательно)
		optField(PolicyEntity.class,
			id,
			subjectService::getById,
			"insurant",
			SubjectEntity.class,
			MyOptionals.of(source.getCLIENTID()))
				.ifPresent(builder::withInsurant);
	    }

	    {
		// CLIENT_FORM_ID s:int Форма клиента (справочник CLIENT_FORMS)
		optField(PolicyEntity.class,
			id,
			personTypeService::getById,
			"insurantPersonType",
			PersonType.class,
			MyOptionals.of(source.getCLIENTFORMID()))
				.ifPresent(builder::withInsurantPersonType);
	    }

	    {
		// MIDDLEMAN_ID s:int Посредник (Идентификатор)
		optField(PolicyEntity.class,
			id,
			insuranceAgentService::getById,
			"insuranceAgent",
			InsuranceAgentEntity.class,
			MyOptionals.of(source.getMIDDLEMANID()))
				.ifPresent(builder::withInsuranceAgent);

		// MIDDLEMAN_CONTRACT_NUMBER s:string Номер договора посредника
	    }

	    {
		// POLICY_DATE s:string Дата полиса
		MyOptionals.of(source.getPOLICYDATE())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(builder::withDateOfIssue);
	    }

	    {
		if (MyStrings.nonEmpty(source.getRESCINDINGDATE())) {
		    // RESCINDING_DATE s:string Дата расторжения полиса
		    // RESCINDING_REASON_ID s:int Идентификатор причины
		    // расторжения
		    final CancelationInfoBuilder b1 = CancelationInfo.builder();

		    MyOptionals.of(source.getRESCINDINGDATE())
			    .map(TemporalUtil::dateToLocalDate)
			    .ifPresent(b1::withDateOf);

		    optField(PolicyEntity.class,
			    id,
			    cancelationReasonTypeService::getById,
			    "cancelationReasonType",
			    CancelationReason.class,
			    MyOptionals.of(source.getRESCINDINGREASONID()))
				    .ifPresent(b1::withReason);

		    b1.buildTo(builder::withCancelation);
		}
	    }

	    {
		// BRANCH_ID s:int Филиал (обязательно)
		optField(PolicyEntity.class,
			id,
			branchService::getById,
			"branch",
			BranchEntity.class,
			MyOptionals.of(source.getBRANCHID()))
				.ifPresent(builder::withBranch);
	    }

	    {
		// REWRITE_BOOL s:int Признак переоформления
		// REWRITE_POLICY_ID s:int Ссылка на переоформляемый полис
		final boolean reissued = source.getREWRITEBOOL() == 1;
		if (reissued)
		    builder.withReissuedPolicyId(MyNumbers.requireNonZero(source.getREWRITEPOLICYID()));
	    }

	    {
		// DESCRIPTION s:string Комментарии к полису
		MyOptionals.of(source.getDESCRIPTION())
			.ifPresent(builder::withComments);
	    }

	    {
		// Drivers tns:ArrayOfDriver Водители (обязательно)
		MyOptionals.of(source) //
			.map(Policy::getDrivers) //
			.map(ArrayOfDriver::getDriver) //
			.map(List::stream) //
			.orElseThrow(() -> requireNonEmtyList(PolicyEntity.class, id, "InsuredDrivers")) //
			.peek(x -> MyNumbers.requireEqualsMsg(id, x.getPOLICYID(),
				"%1$s.POLICYID (%3$s) and %2$s.POLICYID (%4$s) are not equals",
				Policy.class.getName(),
				Driver.class.getName(),
				id,
				x.getPOLICYID())) //
			.map(this::_convertPolicyVehicle)
			.forEach(builder::addDriver);
	    }

	    {
		// PoliciesTF tns:ArrayOfPolicies_TF Транспортные средства
		// полиса
		// (обязательно)
		MyOptionals.of(source) //
			.map(Policy::getPoliciesTF) //
			.map(ArrayOfPoliciesTF::getPoliciesTF) //
			.map(List::stream) //
			.orElseThrow(() -> requireNonEmtyList(PolicyEntity.class, id, "InsuredVehicles")) //
			.peek(x -> MyNumbers.requireEqualsMsg(id, x.getPOLICYID(),
				"%1$s.POLICYID (%3$s) and %2$s.POLICYID (%4$s) are not equals",
				Policy.class.getName(),
				PoliciesTF.class.getName(),
				id,
				x.getPOLICYID())) //
			.map(this::_convertPolicyVehicle)
			.forEach(builder::addVehicle);
	    }

	    {
		// CREATED_BY_USER_ID s:int Идентификатор пользователя,
		// создавшего
		// полис
		// INPUT_DATE s:string Дата\время ввода полиса в систему
		// INPUT_DATE_TIME s:string Дата\время ввода полиса в систему
		final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		MyOptionals.of(source.getINPUTDATETIME())
			.flatMap(TemporalUtil::optTemporalToInstant)
			.ifPresent(b1::withInstant);

		optField(PolicyEntity.class,
			id,
			userService::getById,
			"created.author",
			UserEntity.class,
			MyOptionals.of(source.getCREATEDBYUSERID()))
				.ifPresent(b1::withAuthor);

		b1.buildTo(builder::withCreated);
	    }

	    {
		if (MyStrings.nonEmpty(source.getRECORDCHANGEDATDATETIME())) {
		    // RECORD_CHANGED_AT s:string Дата\время изменения полиса
		    // RECORD_CHANGED_AT_DATETIME s:string Дата\время изменения
		    // полиса
		    // CHANGED_BY_USER_ID s:int Идентификатор пользователя,
		    // изменившего полис
		    final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		    MyOptionals.of(source.getRECORDCHANGEDATDATETIME())
			    .flatMap(TemporalUtil::optTemporalToInstant)
			    .ifPresent(b1::withInstant);

		    optField(PolicyEntity.class,
			    id,
			    userService::getById,
			    "modified.author",
			    UserEntity.class,
			    MyOptionals.of(source.getCHANGEDBYUSERID()))
				    .ifPresent(b1::withAuthor);

		    b1.buildTo(builder::withModified);
		}
	    }

	    // ScheduledPayments tns:ArrayOfSCHEDULED_PAYMENT Плановые
	    // платежи
	    // по
	    // полису

	    {
		// PAYMENT_ORDER_TYPE_ID s:int Порядок оплаты (Идентификатор)
		// PAYMENT_ORDER_TYPE s:string Порядок оплаты
		optField(PolicyEntity.class,
			id,
			paymentTypeService::getById,
			"paymentType",
			PaymentType.class,
			MyOptionals.of(source.getPAYMENTORDERTYPEID()))
				.ifPresent(builder::withPaymentType);
	    }

	    {
		// PAYMENT_DATE s:string Дата оплаты
		MyOptionals.of(source.getPAYMENTDATE())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(builder::withDateOfPayment);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }

    // private

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @EJB
    private SubjectEntityServiceLocal subjectService;

    @EJB
    private InsuranceAgentEntityServiceLocal insuranceAgentService;

    @EJB
    private PersonTypeServiceLocal personTypeService;

    @EJB
    private CancelationReasonServiceLocal cancelationReasonTypeService;

    @EJB
    private BranchEntityServiceLocal branchService;

    @EJB
    private UserEntityServiceLocal userService;

    @EJB
    private PaymentTypeServiceLocal paymentTypeService;

    @EJB
    private PolicyDriverEntityEsbdConverterBean policyDriverEntityConverter;

    private PolicyDriverEntity _convertPolicyVehicle(Driver source) {
	try {
	    return policyDriverEntityConverter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw esbdConversionExceptionToEJBException(e);
	}
    }

    @EJB
    private PolicyVehicleEntityEsbdConverterBean policyVehicleEntityConverter;

    private PolicyVehicleEntity _convertPolicyVehicle(PoliciesTF source) {
	try {
	    return policyVehicleEntityConverter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw esbdConversionExceptionToEJBException(e);
	}
    }

}
