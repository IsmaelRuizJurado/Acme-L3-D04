
package acme.features.assistant.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.entities.sessions.Session;
import acme.entities.sessions.SessionType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, Session> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;
		final Session session;
		final Assistant assistant;
		int sessionId;
		Tutorial tutorial;
		principal = super.getRequest().getPrincipal();
		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findTutorialSessionById(sessionId);
		tutorial = this.repository.findTutorialByTutorialSessionId(sessionId);
		assistant = session == null ? null : session.getTutorial().getAssistant();
		status = tutorial != null && session != null && principal.hasRole(assistant) && assistant.getId() == principal.getActiveRoleId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session tutorialSession;
		int id;
		id = super.getRequest().getData("id", int.class);
		tutorialSession = this.repository.findTutorialSessionById(id);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void unbind(final Session tutorialSession) {
		assert tutorialSession != null;
		Tuple tuple;
		Double estimatedTotalTime;
		SelectChoices choices;
		choices = SelectChoices.from(SessionType.class, tutorialSession.getType());
		tuple = super.unbind(tutorialSession, "title", "abstractt", "type", "startPeriod", "finishPeriod", "link");
		estimatedTotalTime = tutorialSession.estimatedLearningTime();
		if (estimatedTotalTime != null)
			tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("type", choices);
		tuple.put("draftMode", tutorialSession.getTutorial().isDraftMode());
		super.getResponse().setData(tuple);
	}
}
