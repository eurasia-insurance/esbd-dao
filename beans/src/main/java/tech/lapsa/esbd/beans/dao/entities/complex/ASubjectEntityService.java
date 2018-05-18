package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity;
import tech.lapsa.esbd.dao.entities.complex.GeneralSubjectEntityService.GeneralSubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.GeneralSubjectEntityService.GeneralSubjectEntityServiceRemote;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public abstract class ASubjectEntityService<T extends SubjectEntity>
	extends AComplexEntitiesService<T, Client>
	implements GeneralSubjectEntityServiceLocal<T>, GeneralSubjectEntityServiceRemote<T> {

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
	return resStream.map(this::conversion)
		.collect(MyCollectors.unmodifiableList());
    }
}
