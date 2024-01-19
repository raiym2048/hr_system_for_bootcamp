package com.example.hr_system.repository;

import com.example.hr_system.entities.Vacancy;
import com.example.hr_system.enums.StatusOfVacancy;
import com.example.hr_system.enums.TypeOfEmployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    @Query("SELECT v FROM Vacancy v " +
            "WHERE (:minResponse IS NULL OR v.response >= :minResponse) " +
            "AND (:maxResponse IS NULL OR v.response <= :maxResponse)")
    List<Vacancy> findAllByResponseRangeAndCategory(
            @Param("minResponse") Integer minResponse,
            @Param("maxResponse") Integer maxResponse);

    List<Vacancy> findAllByEmployerIdAndStatusOfVacancyAndDays(Long id, StatusOfVacancy status, String days);


    @Query("SELECT v FROM Vacancy v " +
            "WHERE (v.response >= :minResponse) " +
            "AND (v.response <= :maxResponse) " +
            "AND (:id IS NULL OR v.employer.id = :id) " +
            "AND (:status = '' or :status IS NULL OR v.statusOfVacancy = :status) " +
            "AND (:days = '' or :days IS NULL OR v.days = :days)")
    List<Vacancy> findAllByParameters(
            @Nullable @Param("minResponse") int minResponse,
            @Nullable @Param("maxResponse") int maxResponse,
            @Nullable @Param("id") Long id,
            @Nullable @Param("status") String status,
            @Nullable @Param("days") String days);



    List<Vacancy> findAllByEmployerId(Long id);
    List<Vacancy> findAllByDays(String days);
    List<Vacancy> findAllByStatusOfVacancy(StatusOfVacancy statusOfVacancy);

    @Query("select v from Vacancy  v where lower(v.position.name) like %?1% or" +
            " lower(v.position.category.name) like %?1% or  lower(v.contactInformation.city) like %?1% or " +
            " lower(v.contactInformation.country) like %?1%")
    List<Vacancy> search(@Param("search") String search);

        @Query("SELECT v FROM Vacancy v " +
                "WHERE (:positionName IS NULL OR v.position.name = :positionName) " +
                "AND (:categoryName IS NULL OR v.position.category.name = :categoryName) " +
                "AND (:country IS NULL OR v.contactInformation.country = :country) " +
                "AND (:city IS NULL OR v.contactInformation.city = :city) " +
                "AND (:experience IS NULL OR v.experience = :experience) " +
                "AND v.id <> :vacancyId " +
                "ORDER BY v.creationDate")
        List<Vacancy> findSimilarOrSameVacancies(
                @Param("positionName") String positionName,
                @Param("categoryName") String categoryName,
                @Param("country") String country,
                @Param("city") String city,
                @Param("experience") String experience,
                @Param("vacancyId") Long vacancyId);

    @Query("SELECT v FROM Vacancy v " +
            "WHERE (:category IS NULL OR v.position.category.name LIKE %:category%) " +
            "AND (:position IS NULL OR v.position.name LIKE %:position%) " +
            "AND (:country IS NULL OR v.contactInformation.country LIKE %:country%) " +
            "AND (:city IS NULL OR v.contactInformation.city LIKE %:city%) " +
            "AND (:experience IS NULL OR v.experience LIKE %:experience%)" +
            "AND (:type_of_employments IS NULL OR v.typeOfEmploymentS = :type_of_employments)")
    List<Vacancy> filter2(@Param("category") String category,
                         @Param("position") String position,
                         @Param("country") String country,
                         @Param("city") String city,
                         @Param("experience") String experience,
                          @Param("type_of_employments")TypeOfEmployment type_of_employments);

    @Query("SELECT u FROM Vacancy u " +
            "LEFT JOIN u.position js " +
            "LEFT JOIN u.employer emp " +
            "WHERE lower(js.name) LIKE lower(concat('%', :vacancyName, '%')) " +
            "OR lower(emp.companyName) LIKE lower(concat('%', :vacancyName, '%'))")
    List<Vacancy> searchVacancyByName(@Param("vacancyName") String vacancyName);

    @Query("SELECT v FROM Vacancy v " +
            "LEFT JOIN v.position js " +
            "LEFT JOIN v.employer emp " +
            "WHERE ((lower(js.name) LIKE lower(concat('%', :vacancyName, '%')) OR " +
            "lower(emp.companyName) LIKE lower(concat('%', :vacancyName, '%'))) OR :vacancyName IS NULL) " +
            "AND (" +
            "CASE " +
            "WHEN EXTRACT(DAY FROM (CURRENT_DATE - v.creationDate)) < 2 THEN 'TODAY' " +
            "WHEN EXTRACT(DAY FROM (CURRENT_DATE - v.creationDate)) BETWEEN 2 AND 7 THEN 'THIS_WEEK' " +
            "WHEN EXTRACT(DAY FROM (CURRENT_DATE - v.creationDate)) BETWEEN 8 AND 29 THEN 'THIS_MONTH' " +
            "ELSE 'THIS_YEAR' " +
            "END) = :dateFilter")
    List<Vacancy> searchVacancyByNameAndDateFilter(@Param("vacancyName") String vacancyName, @Param("dateFilter") String dateFilter);





}
