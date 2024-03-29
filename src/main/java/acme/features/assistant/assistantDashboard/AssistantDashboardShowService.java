
package acme.features.assistant.assistantDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.AssistantDashboard;
import acme.form.Stats;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantDashboardShowService extends AbstractService<Assistant, AssistantDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Assistant assistant;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		status = assistant != null && principal.hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int assistantId;
		final AssistantDashboard assistantDashboard;
		final Principal principal;
		int userAccountId;
		final Assistant assistant;

		final Stats sessionLength;
		final double averageSessionLength;
		final double deviationSessionLength;
		final double minimumSessionLength;
		final double maximumSessionLength;
		final int countSession;

		final Stats tutorialLength;
		final double averageTutorialLength;
		final double deviationTutorialLength;
		final double minimumTutorialLength;
		final double maximumTutorialLength;
		final int countTutorial;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		assistantId = assistant.getId();

		countSession = this.repository.findCountTutorialSession(assistantId);
		countTutorial = this.repository.findCountTutorial(assistantId);

		if (countSession == 0 || countTutorial == 0) {
			assistantDashboard = new AssistantDashboard();
			sessionLength = new Stats(countSession, 0., 0., 0., 0.);
			assistantDashboard.setSessionTime(sessionLength);
			tutorialLength = new Stats(countTutorial, 0., 0., 0., 0.);
			assistantDashboard.setTutorialTime(tutorialLength);
			assistantDashboard.setTotalNumberOfTutorial(countTutorial);
			super.getBuffer().setData(assistantDashboard);
		} else {

			averageSessionLength = this.repository.findAverageTutorialSessionLength(assistantId);
			deviationSessionLength = this.repository.findDeviationTutorialSessionLength(assistantId);
			minimumSessionLength = this.repository.findMinimumTutorialSessionLength(assistantId);
			maximumSessionLength = this.repository.findMaximumTutorialSessionLength(assistantId);

			sessionLength = new Stats(countSession, averageSessionLength, maximumSessionLength, minimumSessionLength, deviationSessionLength);

			averageTutorialLength = this.repository.findAvgTutorialLength(assistantId);
			deviationTutorialLength = this.repository.findDevTutorialLength(assistantId);
			minimumTutorialLength = this.repository.findMinTutorialLength(assistantId);
			maximumTutorialLength = this.repository.findMaxTutorialLength(assistantId);

			tutorialLength = new Stats(countTutorial, averageTutorialLength, maximumTutorialLength, minimumTutorialLength, deviationTutorialLength);

			assistantDashboard = new AssistantDashboard();
			assistantDashboard.setSessionTime(sessionLength);
			assistantDashboard.setTutorialTime(tutorialLength);
			assistantDashboard.setTotalNumberOfTutorial(countTutorial);
			super.getBuffer().setData(assistantDashboard);
		}
	}

	@Override
	public void unbind(final AssistantDashboard assistantDashboard) {
		Tuple tuple;
		tuple = super.unbind(assistantDashboard, "totalNumberOfTutorial", "sessionTime", "tutorialTime");
		super.getResponse().setData(tuple);
	}
}
