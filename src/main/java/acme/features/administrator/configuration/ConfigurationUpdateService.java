
package acme.features.administrator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Configuration;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;

@Service
public class ConfigurationUpdateService extends AbstractService<Administrator, Configuration> {

	@Autowired
	protected ConfigurationRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Configuration object;

		object = this.repository.findSystemConfiguration();

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Configuration object) {
		assert object != null;

		super.bind(object, "systemCurrency", "acceptedCurrencies");
	}

	@Override
	public void validate(final Configuration object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("acceptedCurrencies"))
			super.state(object.getAcceptedCurrencies().contains(object.getSystemCurrency()), "acceptedCurrencies", "administrator.configuration.acceptedCurrencies");
		super.state(object.getAcceptedCurrencies().contains("EUR") && object.getAcceptedCurrencies().contains("USD") && object.getAcceptedCurrencies().contains("GBP"), "acceptedCurrencies", "administrator.configuration.acceptedCurrencies-Default-ones");
	}

	@Override
	public void perform(final Configuration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Configuration object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "systemCurrency", "acceptedCurrencies");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
