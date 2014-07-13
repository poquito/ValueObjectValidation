package validation.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * this class provides convenience methods for JSR303 handling.
 * 
 * @author mrodler
 * 
 */
public final class BeanValidator {

	/**
	 * this class provides all parameters to the message interpolator.
	 */
	static class ContraintViolationContext implements Context {
		private ConstraintViolation<?> violation;

		ContraintViolationContext(ConstraintViolation<?> violation) {
			this.violation = violation;
		}

		public ConstraintDescriptor<?> getConstraintDescriptor() {
			return violation.getConstraintDescriptor();
		}

		public Object getValidatedValue() {
			return violation.getInvalidValue();
		}

		public <T> T unwrap(Class<T> arg0) {
			return violation.unwrap(arg0);
		}
	}

	private static final BeanValidator CURRENT = new BeanValidator();

	public static BeanValidator current() {
		return CURRENT;
	}

	private MessageInterpolator interpolator;
	private Validator validator;

	private BeanValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		interpolator = factory.getMessageInterpolator();
		validator = factory.getValidator();
	}

	/**
	 * creates a text message.
	 * 
	 * @param violation
	 * @return the message text.
	 */
	public String createMessage(ConstraintViolation<?> violation) {
		return interpolator.interpolate(violation.getMessageTemplate(), new ContraintViolationContext(violation));
	}

	/**
	 * creates a text message for the given locale.
	 * 
	 * @param violation
	 * @param locale
	 * @return the message text.
	 */
	public String createMessage(ConstraintViolation<?> violation, Locale locale) {
		return interpolator.interpolate(violation.getMessageTemplate(), new ContraintViolationContext(violation), locale);
	}

	/**
	 * validates any object.
	 * 
	 * @param bean
	 *            the object.
	 * 
	 * @throws ConstraintViolationException
	 *             if bean is not valid.
	 * 
	 */
	public void validate(Object bean) {
		Set<ConstraintViolation<Object>> violations = validator.validate(bean);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

}
