package validation.ui;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import validation.model.BeanValidator;

/**
 * the abstract converter which handles validation exceptions.
 * 
 * @author mrodler
 * 
 */
public abstract class AbstractConverter implements Converter {

	/**
	 * implementors must convert the value.
	 * 
	 * @param context
	 * @param component
	 * @param value
	 * @return the converted object
	 */
	public abstract Object convert(FacesContext context, UIComponent component, String value);

	/**
	 * subclasses may override this method for null handling.
	 * 
	 * the default implementation returns true if value is null or value.empty()
	 * returns true.
	 * 
	 * @param value
	 * @return true if value is null.
	 */
	public boolean isNullValue(String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		return false;
	}

	public final Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (isNullValue(value)) {
			return null;
		}

		try {

			return convert(context, component, value);

		} catch (ConstraintViolationException e) {

			Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

			if (violations.isEmpty()) {
				throw new ConverterException("violation failed:" + e.getMessage());
			}

			Locale requestLocale = context.getExternalContext().getRequestLocale();

			Iterator<ConstraintViolation<?>> iterator = violations.iterator();
			ConverterException exception = new ConverterException(createMessage(iterator.next(), requestLocale));

			// if there is more than one constraint violation, we need to add
			// them to the faces context.
			while (iterator.hasNext()) {
				FacesMessage message = createMessage(iterator.next(), requestLocale);
				context.addMessage(component.getClientId(), message);
			}

			throw exception;
		}
	}

	public FacesMessage createMessage(ConstraintViolation<?> violation, Locale locale) {
		String violationMessage = BeanValidator.current().createMessage(violation, locale);
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, violationMessage, "Validation Error");
	}

}