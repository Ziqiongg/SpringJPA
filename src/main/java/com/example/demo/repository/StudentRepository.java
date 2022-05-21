package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

    //use Query annotation to write method, Student is the name of Entity(Student class)
    //?1 means only pass in 1 element -- email
    @Query("SELECT s FROM Student s WHERE s.email = ?1")

    //using method to create your own query to get data you want
    Optional<Student> findStudentByEmail(String email);

    //?2 means pass in 2 elements -firstname and age
    //("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2") is not sql, is jpql for java persistence
    @Query("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2")
    //using @Query, you can change the name here
    List<Student> findStudentByFirstNameEqualsAndAgeIsGreaterThanEqual(String firstName, Integer age);


    //using native query
    //if you use another database, the query won't work, but if you use jpql, it will work
    @Query(
            value = "SELECT * FROM student WHERE first_name = ?1 AND age >= ?2",
            nativeQuery = true)
        //using @Query, you can change the method name here
    List<Student> findStudentByFirstNameEqualsAndAgeIsGreaterThanEqualNATIVE(String firstName, Integer age);


    //rename the parameter
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Query(
            value = "SELECT * FROM student WHERE first_name = :firstName AND age >= :age",
            nativeQuery = true)
    //using @Query, you can change the method name here
    List<Student> findStudentByFirstNameEqualsAndAgeIsGreaterThanEqualNATIVEChangeParameter(
            @Param("firstName") String firstName,
            @Param("age") Integer age);


    //delete student
    //use the @Transactional to modify your data, default readOnly = false
    // if only want to read data @Transactional(readOnly = true)
    //if you put  @Transactional(readOnly = true) above the whole page, it means the whole interface is read only, except this one

    @Transactional
    @Modifying
    @Query("DELETE FROM Student u WHERE u.id = ?1")
    int deleteStudentById(Long id);









}
