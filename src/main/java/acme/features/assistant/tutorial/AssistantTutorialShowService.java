
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.entities.course.Course;
import acme.entities.sessions.Session;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialShowService extends AbstractService<Assistant, Tutorial> {

	@Autowired
	protected AssistantTutorialRepository repository;


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
		final Assistant assistant;
		final Tutorial tutorial;
		int tutorialId;
		Principal principal;
		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		principal = super.getRequest().getPrincipal();
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && (tutorial.isDraftMode() == false || principal.hasRole(assistant)) && assistant.getId() == principal.getActiveRoleId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;
		final Collection<Session> sessions;
		final Double estimatedTotalTime;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		sessions = this.repository.findSessionsByTutorialId(object.getId());
		estimatedTotalTime = this.repository.findTutorialById(object.getId()).totalTime(sessions);
		tuple = super.unbind(object, "code", "title", "abstractt", "goals", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("sessions", sessions);
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		super.getResponse().setData(tuple);
	}
}
