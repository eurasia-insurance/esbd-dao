package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import static tech.lapsa.esbd.beans.dao.entities.complex.Util.*;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.datatype.XMLGregorianCalendar;

import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.domain.complex.UserEntity;
import tech.lapsa.esbd.domain.complex.UserEntity.UserEntityBuilder;
import tech.lapsa.esbd.domain.dict.BranchEntity;
import tech.lapsa.esbd.domain.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.jaxws.wsimport.User;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class UserEntityEsbdConverterBean implements AEsbdAttributeConverter<UserEntity, User> {

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @EJB
    private SubjectEntityServiceLocal subjectService;

    @EJB
    private BranchEntityServiceLocal branchService;

    @Override
    public User convertToEsbdValue(UserEntity source) throws EsbdConversionException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public UserEntity convertToEntityAttribute(User source) throws EsbdConversionException {

	try {

	    final UserEntityBuilder builder = UserEntity.builder();

	    final int id = source.getID();

	    {
		// ID s:int Идентификатор пользователя
		MyOptionals.of(id)
			.ifPresent(builder::withId);
	    }

	    {
		// Name s:string Имя пользователя
		MyOptionals.of(source.getName())
			.ifPresent(builder::withLogin);
	    }

	    {
		// Branch_ID s:int Филиал пользователя (справочник BRANCHES)
		optField(UserEntity.class,
			id,
			branchService::getById,
			"branch",
			BranchEntity.class,
			MyOptionals.of(source.getBranchID()))
				.ifPresent(builder::withBranch);
	    }

	    {
		// CLIENT_ID s:int Клиент пользователя (справочник CLIENTS)
		optField(UserEntity.class,
			id,
			subjectService::getById,
			"subject",
			SubjectEntity.class,
			MyOptionals.of(source.getCLIENTID()))
				.ifPresent(builder::withSubject);
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Разделитель учета (справочник
		// SYSTEM_DELIMITER)
		optField(UserEntity.class,
			id,
			insuranceCompanyService::getById,
			"organization",
			InsuranceCompanyEntity.class,
			MyOptionals.of(source.getSYSTEMDELIMITERID()))
				.ifPresent(builder::withOrganization);
	    }

	    {
		// IsAuthenticated s:int Пользователь аутентифицирован
		builder.withAuthentificated(source.getIsAuthenticated() == 1);
	    }

	    {
		// SessionID s:string Идентификатор текущей сессии пользователя
		MyOptionals.of(source.getSessionID())
			.ifPresent(builder::withLastSesionId);
	    }

	    {
		// LastRequestTime s:string Время последнего действия
		// пользователя
		MyOptionals.of(source.getLastRequestTime())
			.map(XMLGregorianCalendar::toGregorianCalendar)
			.map(Calendar::toInstant)
			.ifPresent(builder::withLastActivity);
	    }

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
