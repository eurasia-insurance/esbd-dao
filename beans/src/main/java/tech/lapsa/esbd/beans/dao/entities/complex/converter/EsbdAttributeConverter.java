package tech.lapsa.esbd.beans.dao.entities.complex.converter;

public interface EsbdAttributeConverter<X, Y> {

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
