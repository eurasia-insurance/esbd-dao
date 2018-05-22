package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.domain.complex.InsuranceAgentEntity.InsuranceAgentEntityBuilder;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.domain.complex.UserEntity;
import tech.lapsa.esbd.domain.dict.BranchEntity;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.domain.embedded.ContractInfo;
import tech.lapsa.esbd.domain.embedded.ContractInfo.ContractInfoBuilder;
import tech.lapsa.esbd.domain.embedded.RecordOperationInfo;
import tech.lapsa.esbd.domain.embedded.RecordOperationInfo.RecordOperationInfoBuilder;
import tech.lapsa.esbd.jaxws.wsimport.MIDDLEMAN;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class InsuranceAgentEntityEsbdConverterBean
	implements AEsbdAttributeConverter<InsuranceAgentEntity, MIDDLEMAN> {

    @EJB
    private SubjectEntityServiceLocal subjectService;

    @EJB
    private BranchEntityServiceLocal branchService;

    @EJB
    private UserEntityServiceLocal userService;

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @Override
    public MIDDLEMAN convertToEsbdValue(InsuranceAgentEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public InsuranceAgentEntity convertToEntityAttribute(MIDDLEMAN source) throws EsbdConversionException {
	try {

	    final InsuranceAgentEntityBuilder builder = InsuranceAgentEntity.builder();

	    final int id = source.getMIDDLEMANID();

	    {
		// ID s:int Идентификатор
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// CONTRACT_DATE s:string дата договора посредника
		// CONTRACT_NUMBER s:string номер договора посредника
		final ContractInfoBuilder b1 = ContractInfo.builder();

		MyOptionals.of(source.getCONTRACTDATE())
			.map(TemporalUtil::dateToLocalDate)
			.ifPresent(b1::withDateOf);

		MyOptionals.of(source.getCONTRACTNUMBER())
			.ifPresent(b1::withNumber);

		b1.buildTo(builder::withContract);
	    }

	    {
		// LETTER_OF_ATTORNEY_NUMBER s:string номер доверенности
		// посредника
		MyOptionals.of(source.getLETTEROFATTORNEYNUMBER())
			.ifPresent(builder::withLetterOfAttorneyNumber);
	    }

	    {
		// BRANCH_ID s:int идентификатор регионального подразделения
		optField(InsuranceAgentEntity.class,
			id,
			branchService::getById,
			"branch",
			BranchEntity.class,
			MyOptionals.of(source.getBRANCHID()))
				.ifPresent(builder::withBranch);
	    }

	    {
		// USER_ID s:int идентификатор пользователя
		optField(InsuranceAgentEntity.class,
			id,
			userService::getById,
			"user",
			UserEntity.class,
			MyOptionals.of(source.getUSERID()))
				.ifPresent(builder::withUser);
	    }

	    {
		// CLIENT_ID s:int идентификатор клиента
		optField(InsuranceAgentEntity.class,
			id,
			subjectService::getById,
			"subject",
			SubjectEntity.class,
			MyOptionals.of(source.getCLIENTID()))
				.ifPresent(builder::withSubject);
	    }

	    {
		final Optional<String> instant = MyOptionals.of(source.getINPUTDATE());
		final Optional<Integer> author = MyOptionals.of(source.getCREATEDBYUSERID());
		if (instant.isPresent() || author.isPresent()) {

		    final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		    instant.flatMap(TemporalUtil::optTemporalToInstant)
			    .ifPresent(b1::withInstant);

		    optField(InsuranceAgentEntity.class,
			    id,
			    userService::getById,
			    "created.author",
			    UserEntity.class,
			    author)
				    .ifPresent(b1::withAuthor);

		    b1.buildTo(builder::withCreated);
		}
	    }

	    {
		final Optional<String> instant = MyOptionals.of(source.getRECORDCHANGEDAT());
		final Optional<Integer> author = MyOptionals.of(source.getCHANGEDBYUSERID());
		if (instant.isPresent() || author.isPresent()) {

		    final RecordOperationInfoBuilder b1 = RecordOperationInfo.builder();

		    instant.flatMap(TemporalUtil::optTemporalToInstant)
			    .ifPresent(b1::withInstant);

		    optField(InsuranceAgentEntity.class,
			    id,
			    userService::getById,
			    "modified.author",
			    UserEntity.class,
			    author)
				    .ifPresent(b1::withAuthor);

		    b1.buildTo(builder::withModified);
		}
	    }

	    {
		optField(InsuranceAgentEntity.class,
			id,
			insuranceCompanyService::getById,
			"insurer",
			InsuranceCompanyEntity.class,
			MyOptionals.of(source.getSYSTEMDELIMITERID()))
				.ifPresent(builder::withInsurer);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
