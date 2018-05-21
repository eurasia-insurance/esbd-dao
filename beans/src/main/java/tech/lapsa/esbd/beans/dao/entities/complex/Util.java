package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.ejb.EJBException;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

public final class Util {

    private Util() {
    }

    public static <T> T requireSingle(final List<T> list,
	    final Class<?> clazz,
	    final String keyName,
	    final Object key) throws EJBException {
	if (list.size() > 1)
	    throw MyExceptions.illegalArgumentFormat("Too many %1$s (%2$s) with %3$s = '%4$s'",
		    clazz.getSimpleName(), // 1
		    list.size(), // 2
		    keyName, // 3
		    key // 4
	    );
	return list.get(0);
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
	R apply(T value) throws Exception;
    }

    public static <T> IllegalArgumentException requireNonEmtyList(final Class<T> targetClazz,
	    final Object targetId,
	    final String fieldName) {
	final String message = MyStrings.format("Error fetching %1$s(%2$s).%3$s -> list is empty",
		targetClazz, // 1,
		targetId, // 2
		fieldName // 3
	);
	return new IllegalArgumentException(message);
    }

    // optField

    public static <T, F, FI> Optional<F> optField(final Class<T> targetClazz,
	    final Object targetId,
	    final ThrowingFunction<FI, F> entityGeter,
	    final String fieldName,
	    final Class<F> fieldClazz,
	    final Optional<FI> optFieldId,
	    final Predicate<Throwable> ignoreException) {

	if (!optFieldId.isPresent())
	    return Optional.empty();

	F fieldObject = null;
	try {
	    fieldObject = entityGeter.apply(optFieldId.get());
	} catch (final Exception e) {
	    if (ignoreException == null || !ignoreException.test(e)) {
		final String message = MyStrings.format(
			"Error occured on fetching %4$s[ID=%5$s] '%6$s'. Tried to bind to %1$s[ID=%2$s].'%3$s'",
			targetClazz.getSimpleName(), // 1,
			targetId, // 2
			fieldName, // 3
			fieldClazz.getSimpleName(), // 4
			optFieldId.get(), // 5
			e.getMessage() // 6
		);
		throw new IllegalArgumentException(message, e);
	    }
	}
	return MyOptionals.of(fieldObject);
    }

    public static <T, F, FI> Optional<F> optField(final Class<T> targetClazz,
	    final Object targetId,
	    final ThrowingFunction<FI, F> fieldGeter,
	    final String fieldName,
	    final Class<F> fieldClazz,
	    final Optional<FI> optFieldId) {
	return optField(targetClazz, targetId, fieldGeter, fieldName, fieldClazz, optFieldId, null);
    }

    public static <T, F, FI> Optional<F> optFieldIgnoreFieldNotFound(final Class<T> targetClazz,
	    final Object targetId,
	    final ThrowingFunction<FI, F> fieldGeter,
	    final String fieldName,
	    final Class<F> fieldClazz,
	    final Optional<FI> optFieldId) {
	return optField(targetClazz, targetId, fieldGeter, fieldName, fieldClazz, optFieldId,
		e -> (e instanceof NotFound));
    }

    public static EJBException esbdConversionExceptionToEJBException(EsbdConversionException e) {
	return MyExceptions.format(EJBException::new, "An exception has thrown during mapping the entity %1$s '%2$s'",
		e.getClass(), e.getMessage());
    }
}
