package tech.lapsa.esbd.beans.dao.entities;

public interface EsbdAttributeConverter<X, Y> {

    public static class EsbdConversionException extends RuntimeException {

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

    public Y convertToEsbdValue(X entityAttribute) throws EsbdConversionException;

    public X convertToEntityAttribute(Y esbdValue) throws EsbdConversionException;
}
