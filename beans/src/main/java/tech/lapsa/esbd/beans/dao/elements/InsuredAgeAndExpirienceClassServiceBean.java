package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lapsa.insurance.elements.InsuredAgeAndExpirienceClass;

import tech.lapsa.esbd.beans.dao.elements.mapping.InsuredAgeAndExpirienceClassMapping;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.InsuredAgeAndExpirienceClassService;
import tech.lapsa.esbd.dao.elements.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceLocal;
import tech.lapsa.esbd.dao.elements.InsuredAgeAndExpirienceClassService.InsuredAgeAndExpirienceClassServiceRemote;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.logging.MyLogger;

@Singleton(name = InsuredAgeAndExpirienceClassService.BEAN_NAME)
public class InsuredAgeAndExpirienceClassServiceBean
        implements InsuredAgeAndExpirienceClassServiceLocal, InsuredAgeAndExpirienceClassServiceRemote {

	private final MyLogger logger = MyLogger.newBuilder() //
	        .withNameOf(InsuredAgeAndExpirienceClassService.class) //
	        .build();

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public InsuredAgeAndExpirienceClass getById(final Integer id) throws NotFound, IllegalArgument {
		try {
			return _getById(id);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgument(e);
		} catch (final RuntimeException e) {
			logger.WARN.log(e);
			throw new EJBException(e.getMessage());
		}
	}

	private InsuredAgeAndExpirienceClass _getById(final Integer id) throws IllegalArgumentException, NotFound {
		MyNumbers.requireNonZero(id, "id");
		final InsuredAgeAndExpirienceClass result = InsuredAgeAndExpirienceClassMapping.getInstance().forId(id);
		if (result == null)
			throw new NotFound(
			        InsuredAgeAndExpirienceClass.class.getSimpleName() + " not found with ID = '" + id + "'");
		return result;
	}
}
