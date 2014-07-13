package validation.ui;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import validation.model.AccountId;

@Named
@ViewScoped
public class IndexController {

	@NotNull
	private AccountId accountId;

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public String update() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "success", accountId.getValue());
		FacesContext.getCurrentInstance().addMessage(null, message);
		return null;
	}

}
