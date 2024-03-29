
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCoursePublishService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int objectId;
		Course object;
		final Principal principal;
		final int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(objectId);

		status = object.isDraftMode() && object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int objectId;
		Course object;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(objectId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractt", "price", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		int courseId;
		boolean hasLectures;
		boolean hasLecturesNotPublished;
		boolean hasHandsOnLecturesInCourse;

		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {
			final boolean draftMode = object.isDraftMode();
			super.state(draftMode, "draftMode", "lecturer.course.error.draftMode.published");
		}
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Course instance;
			final String code = object.getCode();
			boolean eval;

			instance = this.repository.findOneCourseByCode(code);
			eval = instance == null || object.getId() == instance.getId();
			super.state(eval, "code", "lecturer.course.error.code.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("price")) {
			final double retailPrice = object.getPrice().getAmount();
			super.state(retailPrice >= 0, "price", "lecturer.course.error.price.negative");
		}

		courseId = object.getId();
		hasLectures = this.repository.hasACourseLecturesByCourseId(courseId);
		hasLecturesNotPublished = this.repository.hasACourseLecturesNotPublishedByCourseId(courseId);
		hasHandsOnLecturesInCourse = this.repository.hasACourseHandsOnLecturesByCourseId(courseId);

		super.state(hasLectures, "*", "lecturer.course.form.error.noLecture");
		super.state(hasLecturesNotPublished, "*", "lecturer.course.form.error.lectureNotPublished");
		super.state(hasHandsOnLecturesInCourse, "*", "lecturer.course.form.error.noHandsOn");
	}

	@Override
	public void perform(final Course object) {
		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		final Collection<Lecture> lectures;
		CourseType courseType = CourseType.THEORETICAL;

		lectures = this.repository.findLecturesByCourseId(object.getId());
		for (final Lecture l : lectures) {
			final LectureType lt = l.getLectureType();
			if (lt.equals(LectureType.HANDS_ON))
				courseType = CourseType.HANDS_ON;
		}

		tuple = super.unbind(object, "code", "title", "abstractt", "price", "link");
		tuple.put("courseType", courseType);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
