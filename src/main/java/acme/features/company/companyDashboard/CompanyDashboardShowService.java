
package acme.features.company.companyDashboard;

import java.time.Month;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.CompanyDashboard;
import acme.form.Stats;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyDashboardShowService extends AbstractService<Company, CompanyDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		Company company;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		company = this.repository.findOneCompanyByUserAccountId(userAccountId);

		status = company != null && principal.hasRole(Company.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int companyId;
		CompanyDashboard companyDashboard;
		final Principal principal;
		int userAccountId;
		Company company;

		Stats sessionLength;
		double averageSessionLength;
		double deviationSessionLength;
		double minimumSessionLength;
		double maximumSessionLength;
		int countSession;

		Stats practicaLength;
		double averagePracticaLength;
		double deviationPracticaLength;
		double minimumPracticaLength;
		double maximumPracticaLength;
		int countPractica;

		final Map<String, Long> totalNumberOfPractica;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		company = this.repository.findOneCompanyByUserAccountId(userAccountId);
		companyId = company.getId();

		countSession = this.repository.findCountSession(companyId);
		countPractica = this.repository.findCountPractica(companyId);

		if (countSession == 0 || countPractica == 0) {
			companyDashboard = new CompanyDashboard();
			sessionLength = new Stats(countSession, 0., 0., 0., 0.);
			companyDashboard.setSessionTime(sessionLength);
			practicaLength = new Stats(countPractica, 0., 0., 0., 0.);
			companyDashboard.setPracticumTime(practicaLength);
			super.getBuffer().setData(companyDashboard);
		} else {

			//------------------------------SESSIONS--------------------------------------------
			averageSessionLength = this.repository.findAverageSessionLength(companyId);
			deviationSessionLength = this.repository.findDeviationSessionLength(companyId);
			minimumSessionLength = this.repository.findMinimumSessionLength(companyId);
			maximumSessionLength = this.repository.findMaximumSessionLength(companyId);
			countSession = this.repository.findCountSession(companyId);
			sessionLength = new Stats(countSession, averageSessionLength, maximumSessionLength, minimumSessionLength, deviationSessionLength);

			//------------------------------PRACTICA--------------------------------------------
			averagePracticaLength = this.repository.findAveragePracticaLength(companyId);
			deviationPracticaLength = this.repository.findDeviationPracticaLength(companyId);
			minimumPracticaLength = this.repository.findMinimumPracticaLength(companyId);
			maximumPracticaLength = this.repository.findMaximumPracticaLength(companyId);
			countPractica = this.repository.findCountPractica(companyId);
			practicaLength = new Stats(countPractica, averagePracticaLength, maximumPracticaLength, minimumPracticaLength, deviationPracticaLength);

			totalNumberOfPractica = this.repository.findTotalNumberOfPracticaByMonth(companyId).stream().collect(Collectors.toMap(key -> Month.of((int) key[0]).toString(), value -> (long) value[1]));

			companyDashboard = new CompanyDashboard();

			companyDashboard.setTotalPracticumByMonthLastYear(totalNumberOfPractica);
			companyDashboard.setPracticumSessionLength(sessionLength);
			companyDashboard.setPracticumLength(practicaLength);

			super.getBuffer().setData(companyDashboard);
		}
	}

	@Override
	public void unbind(final CompanyDashboard companyDashboard) {
		Tuple tuple;

		tuple = super.unbind(companyDashboard, "totalPracticumByMonthLastYear", "practicumSessionLength", "practicumLength");

		super.getResponse().setData(tuple);
	}
}
