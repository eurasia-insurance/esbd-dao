package tech.lapsa.esbd.beans.dao.entities;

import static tech.lapsa.esbd.beans.dao.ESBDDates.*;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.Sex;
import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceLocal;
import tech.lapsa.esbd.dao.elements.IdentityCardTypeService.IdentityCardTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.entities.GeneralSubjectEntityService.GeneralSubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.GeneralSubjectEntityService.GeneralSubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.domain.entities.ContactInfo;
import tech.lapsa.esbd.domain.entities.IdentityCardInfo;
import tech.lapsa.esbd.domain.entities.OriginInfo;
import tech.lapsa.esbd.domain.entities.PersonalInfo;
import tech.lapsa.esbd.domain.entities.SubjectCompanyEntity;
import tech.lapsa.esbd.domain.entities.SubjectEntity;
import tech.lapsa.esbd.domain.entities.SubjectPersonEntity;
import tech.lapsa.esbd.domain.entities.SubjectCompanyEntity.SubjectCompanyEntityBuilder;
import tech.lapsa.esbd.domain.entities.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.domain.entities.SubjectPersonEntity.SubjectPersonEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public abstract class ASubjectEntityService<T extends SubjectEntity>
	implements GeneralSubjectEntityServiceLocal<T>, GeneralSubjectEntityServiceRemote<T> {

    @EJB
    CountryServiceLocal countries;

    @EJB
    KZEconomicSectorServiceLocal economicsSectors;

    @EJB
    KZCityServiceLocal cityies;

    @EJB
    GenderServiceLocal genders;

    @EJB
    IdentityCardTypeServiceLocal identityCardTypes;

    @EJB
    CompanyActivityKindEntityServiceLocal companyActivityKinds;

    @EJB
    ConnectionPool pool;

    List<T> _getByIdNumber(final TaxpayerNumber taxpayerNumber,
	    final boolean fetchNaturals,
	    final boolean fetchCompanies) {
	MyObjects.requireNonNull(taxpayerNumber, "taxpayerNumber"); //
	TaxpayerNumber.requireValid(taxpayerNumber);

	final int[] residentBools = new int[] { 1, 0 };

	final int[] naturalPersonBools;
	{
	    final Builder builder = IntStream.builder();
	    if (fetchNaturals)
		builder.add(1);
	    if (fetchCompanies)
		builder.add(0);
	    naturalPersonBools = builder.build().toArray();
	}

	Stream<Client> resStream = Stream.of();
	try (Connection con = pool.getConnection()) {
	    for (final int residentBool : residentBools)
		for (final int naturalPersonBool : naturalPersonBools) {
		    final Client search = new Client();
		    search.setIIN(taxpayerNumber.getNumber());
		    search.setNaturalPersonBool(naturalPersonBool);
		    search.setRESIDENTBOOL(residentBool);
		    final ArrayOfClient clients = con.getClientsByKeyFields(search);
		    resStream = Stream.concat(
			    resStream,
			    MyOptionals.of(clients) //
				    .map(ArrayOfClient::getClient)
				    .map(List::stream)
				    .orElseGet(Stream::empty));
		}
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	return resStream.map(this::convert)
		.collect(MyCollectors.unmodifiableList());
    }

    abstract T convert(Client source);

    void fillValues(final Client source, final SubjectPersonEntityBuilder builder) {
	try {
	    final int id = source.getID();

	    fillValues(source, (SubjectEntityBuilder<?, ?>) builder);

	    {
		// First_Name s:string Имя (для физ. лица)
		// Last_Name s:string Фамилия (для физ. лица)
		// Middle_Name s:string Отчество (для физ. лица)
		// Born s:string Дата рождения
		// Sex_ID s:int Пол (справочник SEX)
		PersonalInfo.builder()
			.withName(source.getFirstName())
			.withSurename(source.getLastName())
			.withPatronymic(source.getMiddleName())
			.withDayOfBirth(convertESBDDateToLocalDate(source.getBorn()))
			.withGender(Util.optField(SubjectPersonEntity.class,
				id,
				genders::getById,
				"personal.gender",
				Sex.class,
				MyOptionals.of(source.getSexID())))
			.buildTo(builder::withPersonal);
	    }

	    // DOCUMENT_TYPE_ID s:int Тип документа (справочник DOCUMENTS_TYPES)
	    // DOCUMENT_NUMBER s:string Номер документа
	    // DOCUMENT_GIVED_BY s:string Документ выдан
	    // DOCUMENT_GIVED_DATE s:string Дата выдачи документа
	    IdentityCardInfo.builder() //
		    .withNumber(MyOptionals.of(source.getDOCUMENTNUMBER()))
		    .withDateOfIssue(MyOptionals.of(convertESBDDateToLocalDate(source.getDOCUMENTGIVEDDATE())))
		    .withIssuingAuthority(source.getDOCUMENTGIVEDBY()) //
		    .withIdentityCardType(Util.optField(SubjectPersonEntity.class,
			    id,
			    identityCardTypes::getById,
			    "identityCard.identityCardType",
			    IdentityCardType.class,
			    MyOptionals.of(source.getDOCUMENTTYPEID())))
		    .buildTo(builder::withIdentityCard);

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}
    }

    void fillValues(final Client source, final SubjectCompanyEntityBuilder builder) {
	try {
	    final int id = source.getID();

	    fillValues(source, (SubjectEntityBuilder<?, ?>) builder);

	    {
		// Juridical_Person_Name s:string Наименование (для юр. лица)
		builder.withCompanyName(source.getJuridicalPersonName());
	    }

	    {
		// MAIN_CHIEF s:string Первый руководитель
		builder.withHeadName(source.getMAINCHIEF());
	    }

	    {
		// MAIN_ACCOUNTANT s:string Главный бухгалтер
		builder.withAccountantName(source.getMAINACCOUNTANT());
	    }

	    {
		// ACTIVITY_KIND_ID s:int Вид деятельности (справочник
		// ACTIVITY_KINDS)
		builder.withCompanyActivityKind(Util.optFieldIgnoreFieldNotFound(SubjectCompanyEntity.class,
			id,
			companyActivityKinds::getById,
			"companyActivityKind",
			CompanyActivityKindEntity.class,
			MyOptionals.of(source.getACTIVITYKINDID())));
	    }

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}
    }

    void fillValues(final Client source, final SubjectEntityBuilder<?, ?> builder) {
	try {
	    final int id = source.getID();

	    {
		// ID s:int Идентификатор клиента (обязательно)
		builder.withId(MyOptionals.of(id).orElse(null));
	    }

	    {
		// RESIDENT_BOOL s:int Признак резидентства (обязательно)
		// COUNTRY_ID s:int Страна (справочник COUNTRIES)
		// SETTLEMENT_ID s:int Населенный пункт (справочник SETTLEMENTS)
		OriginInfo.builder() //
			.withResident(source.getRESIDENTBOOL() == 1)
			.withCountry(Util.optField(SubjectEntity.class,
				id,
				countries::getById,
				"origin.country",
				Country.class,
				MyOptionals.of(source.getCOUNTRYID())))
			.withCity(Util.optField(SubjectEntity.class,
				id,
				cityies::getById,
				"origin.city",
				KZCity.class,
				MyOptionals.of(source.getSETTLEMENTID())))
			.buildTo(builder::withOrigin);
	    }

	    {
		// PHONES s:string Номера телефонов
		// EMAIL s:string Адрес электронной почты
		// Address s:string Адрес
		// WWW s:string Сайт
		ContactInfo.builder() //
			.withPhone(MyOptionals.of(source.getPHONES())
				.map(PhoneNumber::assertValid)) //
			.withHomeAdress(source.getAddress()) //
			.withEmail(source.getEMAIL()) //
			.withSiteUrl(source.getWWW())
			.buildTo(builder::withContact);
	    }

	    {
		// TPRN s:string РНН
		builder.withTaxPayerNumber(source.getTPRN());
	    }

	    {
		// DESCRIPTION s:string Примечание
		builder.withComments(source.getDESCRIPTION());
	    }

	    {
		// RESIDENT_BOOL s:int Признак резидентства (обязательно)
		builder.withResident(source.getRESIDENTBOOL() == 1);
	    }

	    {
		// IIN s:string ИИН/БИН
		builder.withIdNumber(MyOptionals.of(source.getIIN())
			.map(TaxpayerNumber::assertValid));
	    }

	    {
		// ECONOMICS_SECTOR_ID s:int Сектор экономики (справочник
		// ECONOMICS_SECTORS)
		builder.withEconomicsSector(Util.optField(SubjectEntity.class,
			id,
			economicsSectors::getById,
			"EconomicsSector",
			KZEconomicSector.class,
			MyOptionals.of(source.getECONOMICSSECTORID())));
	    }

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}
    }
}
