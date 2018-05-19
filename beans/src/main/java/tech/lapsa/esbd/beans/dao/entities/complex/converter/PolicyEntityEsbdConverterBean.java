package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.TemporalUtil.*;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lapsa.insurance.elements.CancelationReason;
import com.lapsa.insurance.elements.PaymentType;
import com.lapsa.insurance.elements.PersonType;

import tech.lapsa.esbd.beans.dao.entities.complex.Util;
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
import tech.lapsa.esbd.dao.entities.embeded.RecordOperationInfo;
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
		builder.withValidFrom(dateToLocalDate(source.getDATEBEG()));
	    }

	    {
		// DATE_END s:string Дата окончания действия полиса
		// (обязательно)
		builder.withValidTill(dateToLocalDate(source.getDATEEND()));
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
		builder.withInsurer(Util.reqField(PolicyEntity.class,
			id,
			insuranceCompanyService::getById,
			"insurer",
			InsuranceCompanyEntity.class,
			source.getSYSTEMDELIMITERID()));
	    }

	    {
		// CLIENT_ID s:int Идентификатор страхователя (обязательно)
		builder.withInsurant(Util.reqField(PolicyEntity.class,
			id,
			subjectService::getById,
			"insurant",
			SubjectEntity.class,
			source.getCLIENTID()));
	    }

	    {
		// CLIENT_FORM_ID s:int Форма клиента (справочник CLIENT_FORMS)
		if (MyNumbers.nonZero(source.getCLIENTFORMID())) {
		    builder.withInsurantPersonType(Util.reqField(PolicyEntity.class,
			    id,
			    personTypeService::getById,
			    "insurantPersonType",
			    PersonType.class,
			    source.getCLIENTFORMID()));
		}
	    }

	    {
		// MIDDLEMAN_ID s:int Посредник (Идентификатор)
		if (MyNumbers.nonZero(source.getMIDDLEMANID())) {
		    builder.withInsuranceAgent(Util.reqField(PolicyEntity.class,
			    id,
			    insuranceAgentService::getById,
			    "insuranceAgent",
			    InsuranceAgentEntity.class,
			    source.getMIDDLEMANID()));
		}

		// MIDDLEMAN_CONTRACT_NUMBER s:string Номер договора посредника
	    }

	    {
		// POLICY_DATE s:string Дата полиса
		builder.withDateOfIssue(dateToLocalDate(source.getPOLICYDATE()));
	    }

	    {
		// RESCINDING_DATE s:string Дата расторжения полиса
		// RESCINDING_REASON_ID s:int Идентификатор причины расторжения
		if (MyStrings.nonEmpty(source.getRESCINDINGDATE())
			|| MyNumbers.positive(source.getRESCINDINGREASONID()))
		    CancelationInfo.builder()
			    .withDateOf(dateToLocalDate(source.getRESCINDINGDATE()))
			    .withReason(Util.reqField(PolicyEntity.class,
				    id,
				    cancelationReasonTypeService::getById,
				    "cancelationReasonType",
				    CancelationReason.class,
				    source.getRESCINDINGREASONID()))
			    .buildTo(builder::withCancelation);
	    }

	    {
		// BRANCH_ID s:int Филиал (обязательно)
		builder.withBranch(Util.reqField(PolicyEntity.class,
			id,
			branchService::getById,
			"branch",
			BranchEntity.class,
			source.getBRANCHID()));
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
			.orElseThrow(() -> Util.requireNonEmtyList(PolicyEntity.class, id, "InsuredVehicles")) //
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
		RecordOperationInfo.builder()
			.withInstant(datetimeToInstant(source.getINPUTDATETIME()))
			.withAuthor(Util.reqField(PolicyEntity.class,
				id,
				userService::getById,
				"created.author",
				UserEntity.class,
				source.getCREATEDBYUSERID()))
			.buildTo(builder::withCreated);
	    }

	    {
		// RECORD_CHANGED_AT s:string Дата\время изменения полиса
		// RECORD_CHANGED_AT_DATETIME s:string Дата\время изменения
		// полиса
		// CHANGED_BY_USER_ID s:int Идентификатор пользователя,
		// изменившего
		// полис
		if (MyStrings.nonEmpty(source.getRECORDCHANGEDATDATETIME()))
		    RecordOperationInfo.builder()
			    .withInstant(datetimeToInstant(source.getRECORDCHANGEDATDATETIME()))
			    .withAuthor(Util.reqField(PolicyEntity.class,
				    id,
				    userService::getById,
				    "modified.author",
				    UserEntity.class,
				    source.getCHANGEDBYUSERID()))
			    .buildTo(builder::withModified);
	    }

	    // ScheduledPayments tns:ArrayOfSCHEDULED_PAYMENT Плановые
	    // платежи
	    // по
	    // полису

	    {
		// PAYMENT_ORDER_TYPE_ID s:int Порядок оплаты (Идентификатор)
		// PAYMENT_ORDER_TYPE s:string Порядок оплаты
		builder.withPaymentType(Util.reqField(PolicyEntity.class,
			id,
			paymentTypeService::getById,
			"paymentType",
			PaymentType.class,
			source.getPAYMENTORDERTYPEID()));
	    }

	    {
		// PAYMENT_DATE s:string Дата оплаты
		if (MyStrings.nonEmpty(source.getPAYMENTDATE()))
		    builder.withDateOfPayment(dateToLocalDate(source.getPAYMENTDATE()));
	    }

	    final PolicyEntity res = builder.build();
	    return res;

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
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }

    @EJB
    private PolicyVehicleEntityEsbdConverterBean policyVehicleEntityConverter;

    private PolicyVehicleEntity _convertPolicyVehicle(PoliciesTF source) {
	try {
	    return policyVehicleEntityConverter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }

}
