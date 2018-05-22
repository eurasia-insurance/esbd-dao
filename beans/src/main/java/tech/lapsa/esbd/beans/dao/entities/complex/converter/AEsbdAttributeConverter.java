package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import tech.lapsa.esbd.domain.AEntity;

public interface AEsbdAttributeConverter<X extends AEntity, Y> {

    public static class EsbdConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public EsbdConversionException() {
	    super();
	}

	public EsbdConversionException(String message, Throwable cause) {
	    super(message, cause);
	}

	public EsbdConversionException(String message) {
	    super(message);
	}

	public EsbdConversionException(Throwable cause) {
	    super(cause);
	}

    }

    Y convertToEsbdValue(X source) throws EsbdConversionException;

    X convertToEntityAttribute(Y source) throws EsbdConversionException;
}
