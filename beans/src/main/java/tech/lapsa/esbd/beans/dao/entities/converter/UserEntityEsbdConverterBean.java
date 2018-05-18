package tech.lapsa.esbd.beans.dao.entities.converter;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.datatype.XMLGregorianCalendar;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter;
import tech.lapsa.esbd.beans.dao.entities.Util;
import tech.lapsa.esbd.dao.dict.BranchEntity;
import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.InsuranceCompanyEntity;
import tech.lapsa.esbd.dao.dict.InsuranceCompanyEntityService.InsuranceCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.SubjectEntity;
import tech.lapsa.esbd.dao.entities.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.UserEntity;
import tech.lapsa.esbd.dao.entities.UserEntity.UserEntityBuilder;
import tech.lapsa.esbd.dao.entities.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.jaxws.wsimport.User;
import tech.lapsa.java.commons.function.MyOptionals;

@Stateless
@LocalBean
public class UserEntityEsbdConverterBean implements EsbdAttributeConverter<UserEntity, User> {

    @EJB
    private InsuranceCompanyEntityServiceLocal insuranceCompanyService;

    @EJB
    private SubjectEntityServiceLocal subjectService;

    @EJB
    private BranchEntityServiceLocal branchService;

    @EJB
    private UserEntityServiceLocal userService;

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
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// Name s:string Имя пользователя
		builder.withLogin(source.getName());
	    }

	    {
		// Branch_ID s:int Филиал пользователя (справочник BRANCHES)
		builder.withBranch(Util.optField(UserEntity.class,
			id,
			branchService::getById,
			"branch",
			BranchEntity.class,
			MyOptionals.of(source.getBranchID())));
	    }

	    {
		// CLIENT_ID s:int Клиент пользователя (справочник CLIENTS)
		builder.withSubject(Util.optField(UserEntity.class,
			id,
			subjectService::getById,
			"subject", SubjectEntity.class,
			MyOptionals.of(source.getCLIENTID())));
	    }

	    {
		// SYSTEM_DELIMITER_ID s:int Разделитель учета (справочник
		// SYSTEM_DELIMITER)
		builder.withOrganization(Util.optField(UserEntity.class,
			id,
			insuranceCompanyService::getById,
			"organization",
			InsuranceCompanyEntity.class,
			MyOptionals.of(source.getSYSTEMDELIMITERID())));
	    }

	    // IsAuthenticated s:int Пользователь аутентифицирован
	    builder.withAuthentificated(source.getIsAuthenticated() == 1);

	    // SessionID s:string Идентификатор текущей сессии пользователя
	    builder.withLastSesionId(source.getSessionID());

	    // LastRequestTime s:string Время последнего действия пользователя
	    builder.withLastActivity(MyOptionals.of(source.getLastRequestTime())
		    .map(XMLGregorianCalendar::toGregorianCalendar)
		    .map(Calendar::toInstant));

	    // ErrorMessage s:string Описание ошибки аутентификации

	    return builder.build();

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }
}
