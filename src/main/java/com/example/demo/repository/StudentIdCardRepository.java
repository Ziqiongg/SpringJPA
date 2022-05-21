package com.example.demo.repository;

import com.example.demo.model.Student;
import com.example.demo.model.StudentIdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StudentIdCardRepository extends CrudRepository<StudentIdCard, Long> {
}
