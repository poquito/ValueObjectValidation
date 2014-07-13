package validation.ui;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import validation.model.AccountId;

@FacesConverter(forClass = AccountId.class)
public class AccountIdConverter extends AbstractConverter  {

	@Override
	public Object convert(FacesContext context, UIComponent component, String value) {
		return new AccountId(value);
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((AccountId) value).getValue();
	}

}
