package validation.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * a sample value object representing an account id.
 * 
 * instances of this class are always valid account ids.
 * 
 * @author mrodler
 */
public class AccountId {

	@NotNull
	@Size(min = 4, max = 10)
	@Pattern(regexp = "[A|B]+[0-9]+")
	private String value;

	public AccountId(String value) {
		this.value = value;
		BeanValidator.current().validate(this);
	}

	public String getValue() {
		return value;
	}

}
