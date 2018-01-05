package tech.lapsa.esbd.dao.entities;

import static tech.lapsa.esbd.dao.beans.ESBDDates.*;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import javax.ejb.EJB;

import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.Sex;
import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntity;
import tech.lapsa.esbd.dao.dict.CompanyActivityKindEntityService.CompanyActivityKindEntityServiceLocal;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceLocal;
import tech.lapsa.esbd.dao.elements.IdentityCardTypeService.IdentityCardTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.entities.GeneralSubjectEntityService.GeneralSubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.GeneralSubjectEntityService.GeneralSubjectEntityServiceRemote;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public abstract class ASubjectEntityService<T extends SubjectEntity>
	implements GeneralSubjectEntityServiceRemote<T>, GeneralSubjectEntityServiceLocal<T> {

    @EJB
    protected CountryServiceLocal countries;

    @EJB
    protected KZEconomicSectorServiceLocal economicsSectors;

    @EJB
    protected KZCityServiceLocal cityies;

    @EJB
    private GenderServiceLocal genders;

    @EJB
    private IdentityCardTypeServiceLocal identityCardTypes;

    @EJB
    private CompanyActivityKindEntityServiceLocal companyActivityKinds;

    @EJB
    protected ConnectionPool pool;

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
	}
	return resStream.map(this::convert)
		.collect(MyCollectors.unmodifiableList());
    }

    abstract T convert(Client source);

    void fillValues(final Client source, final SubjectPersonEntity target) {
	fillValues(source, (SubjectEntity) target);

	// First_Name s:string Имя (для физ. лица)
	// Last_Name s:string Фамилия (для физ. лица)
	// Middle_Name s:string Отчество (для физ. лица)
	// Born s:string Дата рождения
	// Sex_ID s:int Пол (справочник SEX)
	target.personal = new PersonalInfo();
	target.personal.name = source.getFirstName();
	target.personal.surename = source.getLastName();
	target.personal.patronymic = source.getMiddleName();
	target.personal.dayOfBirth = convertESBDDateToLocalDate(source.getBorn());
	target.personal._gender = source.getSexID();
	Util.optionalField(target, target.id, genders::getById, target.personal::setGender, "Personal.Gender",
		Sex.class, MyOptionals.of(target.personal._gender));

	// DOCUMENT_TYPE_ID s:int Тип документа (справочник DOCUMENTS_TYPES)
	// DOCUMENT_NUMBER s:string Номер документа
	// DOCUMENT_GIVED_BY s:string Документ выдан
	// DOCUMENT_GIVED_DATE s:string Дата выдачи документа
	target.identityCard = new IdentityCardInfo();
	target.identityCard.dateOfIssue = convertESBDDateToLocalDate(source.getDOCUMENTGIVEDDATE());
	target.identityCard.issuingAuthority = source.getDOCUMENTGIVEDBY();
	target.identityCard.number = source.getDOCUMENTNUMBER();
	target.identityCard._identityCardType = source.getDOCUMENTTYPEID();
	Util.optionalField(target, target.getId(), identityCardTypes::getById,
		target.identityCard::setIdentityCardType, "IdentityCard.IdentityCardType", IdentityCardType.class,
		MyOptionals.of(target.identityCard._identityCardType));
    }

    void fillValues(final Client source, final SubjectCompanyEntity target) {
	fillValues(source, (SubjectEntity) target);

	// Juridical_Person_Name s:string Наименование (для юр. лица)
	target.companyName = source.getJuridicalPersonName();

	// MAIN_CHIEF s:string Первый руководитель
	target.headName = source.getMAINCHIEF();

	// MAIN_ACCOUNTANT s:string Главный бухгалтер
	target.accountantName = source.getMAINACCOUNTANT();

	// ACTIVITY_KIND_ID s:int Вид деятельности (справочник ACTIVITY_KINDS)
	target._companyActivityKind = source.getACTIVITYKINDID();
	Util.optionalField(target, target.getId(), companyActivityKinds::getById, target::setCompanyActivityKind,
		"CompanyActivityKind", CompanyActivityKindEntity.class, MyOptionals.of(target._companyActivityKind));
    }

    void fillValues(final Client source, final SubjectEntity target) {

	// ID s:int Идентификатор клиента (обязательно)
	target.id = MyOptionals.of(source.getID()).orElse(null);

	// RESIDENT_BOOL s:int Признак резидентства (обязательно)
	// COUNTRY_ID s:int Страна (справочник COUNTRIES)
	// SETTLEMENT_ID s:int Населенный пункт (справочник SETTLEMENTS)
	target.origin = new OriginInfo();
	target.origin.resident = source.getRESIDENTBOOL() == 1;
	target.origin._country = source.getCOUNTRYID();
	Util.optionalField(target, target.id, countries::getById, target.origin::setCountry, "Origin.Country",
		Country.class, MyOptionals.of(target.origin._country));
	target.origin._city = source.getSETTLEMENTID();
	Util.optionalField(target, target.id, cityies::getById, target.origin::setCity, "Origin.City", KZCity.class,
		MyOptionals.of(target.origin._city));

	// PHONES s:string Номера телефонов
	// EMAIL s:string Адрес электронной почты
	// Address s:string Адрес
	// WWW s:string Сайт
	target.contact = new ContactInfo();
	target.contact.phone = MyOptionals.of(source.getPHONES())
		.map(PhoneNumber::assertValid)
		.orElse(null);
	target.contact.email = source.getEMAIL();
	target.contact.homeAdress = source.getAddress();
	target.contact.siteUrl = source.getWWW();

	// TPRN s:string РНН
	target.taxPayerNumber = source.getTPRN();

	// DESCRIPTION s:string Примечание
	target.comments = source.getDESCRIPTION();

	// RESIDENT_BOOL s:int Признак резидентства (обязательно)
	target.resident = source.getRESIDENTBOOL() == 1;

	// IIN s:string ИИН/БИН
	target._idNumber = source.getIIN();
	target.idNumber = MyOptionals.of(target._idNumber)
		.map(TaxpayerNumber::assertValid)
		.orElse(null);

	// ECONOMICS_SECTOR_ID s:int Сектор экономики (справочник
	// ECONOMICS_SECTORS)
	target._economicsSector = source.getECONOMICSSECTORID();
	Util.optionalField(target, target.getId(), economicsSectors::getById, target::setEconomicsSector,
		"EconomicsSector", KZEconomicSector.class, MyOptionals.of(target._economicsSector));
    }

}
