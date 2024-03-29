
package acme.features.company.companyDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyDashboardRepository extends AbstractRepository {

	@Query("select c from Company c where c.userAccount.id = ?1")
	Company findOneCompanyByUserAccountId(int userAccountId);

	// ----------------------------Sessions----------------------------------------
	@Query("select count(sp) from PracticumSession sp where sp.practicum.company.id = ?1")
	int findCountSession(int companyId);

	@Query("select avg(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1")
	double findAverageSessionLength(int companyId);

	@Query("select stddev(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession  sp where sp.practicum.company.id = ?1")
	double findDeviationSessionLength(int companyId);

	@Query("select min(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1")
	double findMinimumSessionLength(int companyId);

	@Query("select max(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1")
	double findMaximumSessionLength(int companyId);

	// ----------------------------Practicum----------------------------------------	
	@Query("select avg((select sum(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findAveragePracticaLength(int companyId);

	@Query("select stddev((select sum(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findDeviationPracticaLength(int companyId);

	@Query("select min((select sum(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findMinimumPracticaLength(int companyId);

	@Query("select max((select sum(datediff(sp.endPeriod,sp.startPeriod)) from PracticumSession sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findMaximumPracticaLength(int companyId);

	@Query("select count(p) from Practicum p where p.company.id = ?1")
	int findCountPractica(int companyId);

	@Query("SELECT MONTH(sp.startPeriod), COUNT(sp) FROM PracticumSession sp WHERE sp.practicum.company.id = ?1 GROUP BY MONTH(sp.startPeriod) ORDER BY COUNT(sp) DESC")
	List<Object[]> findTotalNumberOfPracticaByMonth(int companyId);
}
