
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerLectureRepository extends AbstractRepository {

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findOneLectureById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select l from Lecture l where l.course.id = :courseId")
	Collection<Lecture> findLecturesByCourseId(int courseId);
}
