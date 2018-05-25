package tech.lapsa.esbd.beans.dao.entities.complex.converter;

import tech.lapsa.esbd.domain.AEntity;

public interface AEsbdAttributeConverter<DOMAIN extends AEntity, ESBD> {

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

    ESBD convertToEsbdValue(DOMAIN source) throws EsbdConversionException;

    DOMAIN convertToEntityAttribute(ESBD source) throws EsbdConversionException;
}
