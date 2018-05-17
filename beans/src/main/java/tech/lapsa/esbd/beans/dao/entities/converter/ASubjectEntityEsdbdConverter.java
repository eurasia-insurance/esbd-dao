package tech.lapsa.esbd.beans.dao.entities.converter;

import javax.ejb.EJB;

import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.kz.country.KZCity;
import com.lapsa.kz.economic.KZEconomicSector;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.dao.elements.CountryService.CountryServiceLocal;
import tech.lapsa.esbd.dao.elements.KZCityService.KZCityServiceLocal;
import tech.lapsa.esbd.dao.elements.KZEconomicSectorService.KZEconomicSectorServiceLocal;
import tech.lapsa.esbd.dao.entities.ContactInfo;
import tech.lapsa.esbd.dao.entities.OriginInfo;
import tech.lapsa.esbd.dao.entities.SubjectEntity;
import tech.lapsa.esbd.dao.entities.SubjectEntity.SubjectEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public class ASubjectEntityEsdbdConverter {

    @EJB
    private CountryServiceLocal countries;

    @EJB
    private KZEconomicSectorServiceLocal economicSectors;

    @EJB
    private KZCityServiceLocal cities;

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
				cities::getById,
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
			economicSectors::getById,
			"EconomicsSector",
			KZEconomicSector.class,
			MyOptionals.of(source.getECONOMICSSECTORID())));
	    }

	} catch (final IllegalArgumentException e) {
	    // it should not happens
	    throw new EsbdConversionException(e.getMessage());
	}
    }

}
