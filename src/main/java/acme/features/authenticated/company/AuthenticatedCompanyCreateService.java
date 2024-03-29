
package acme.features.authenticated.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class AuthenticatedCompanyCreateService extends AbstractService<Authenticated, Company> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedCompanyRepository repository;

	// AbstractService<Authenticated, Company> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Company.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Company c;
		Principal p;
		int uaId;
		UserAccount ua;

		p = super.getRequest().getPrincipal();
		uaId = p.getAccountId();
		ua = this.repository.findOneUserAccountById(uaId);

		c = new Company();
		c.setUserAccount(ua);

		super.getBuffer().setData(c);
	}

	@Override
	public void bind(final Company object) {
		assert object != null;

		super.bind(object, "name", "vat", "summary", "link");
	}

	@Override
	public void validate(final Company object) {
		assert object != null;
	}

	@Override
	public void perform(final Company object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Company object) {
		Tuple tuple;

		tuple = super.unbind(object, "name", "vat", "summary", "link");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
