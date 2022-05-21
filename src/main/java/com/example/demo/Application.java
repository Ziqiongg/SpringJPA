package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.StudentIdCardRepository;
import com.example.demo.repository.StudentRepository;
import com.github.javafaker.Faker;
import org.hibernate.bytecode.enhance.internal.tracker.SortedFieldTracker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository){
        return args -> {

            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17,55));

            student.addBook(
                    new Book("Clean Code", LocalDateTime.now().minusDays(4) ));
            student.addBook(
                    new Book("Think and Grow Rich", LocalDateTime.now()));
            student.addBook(
                    new Book("Spring Data JPA", LocalDateTime.now().minusYears(1) ));



            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            student.setStudentIdCard(studentIdCard);

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L,1L), student, new Course("Computer Science","IT"),LocalDateTime.now()
            ));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L,2L),student, new Course("Spring Data JPA","IT"),LocalDateTime.now().minusDays(18)
            ));


            //student.enrolToCourse(new Course("Computer Science","IT"));

            //student.enrolToCourse(new Course("Spring Data JPA","IT"));


            studentRepository.save(student);

            studentIdCardRepository.save(studentIdCard);
            studentRepository.findById(1L)
                    .ifPresent(System.out::println);
            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("fetch book lazy...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() + "borrowed" + book.getBookName());
                        });
                    });


            //studentRepository.deleteById(1L);


            /*
            //using faker to create lots of students
            generateStudents(studentRepository);
            //using page, in the StudentRepository, change to PagingAndSortingRepository, size 5 means each page 5 student
            PageRequest pageRequest = PageRequest.of(0,5, Sort.by("firstName").ascending());
            Page<Student> page = studentRepository.findAll(pageRequest);
            System.out.println(page);

             */





            //Using sorting
            //sorting(studentRepository);




            /* Using @query
            Student maria = new Student("Maria","Jones","maria.jones@gmail.com", 21);
            Student ahmed = new Student("Ahmed","Ali","Ahmed.Ali@gmail.com", 22);
            studentRepository.saveAll(List.of(maria,ahmed));

            studentRepository
                    .findStudentByEmail("Ahmed.Ali@gmail.com")
                    .ifPresentOrElse(System.out::println,() -> System.out.println("Student with email Ahmed.Ali@gmail.com not found"));

            //the findStudentByFirstNameEqualsAndAgeEquals return a List of Student, then use for each to print each one
            studentRepository.findStudentByFirstNameEqualsAndAgeIsGreaterThanEqualNATIVE(
                    "Maria"
                    ,18)
                    .forEach(System.out::println);

            System.out.println(studentRepository.deleteStudentById(2L));

             */


            /*
            System.out.println(studentRepository.count());

            //2L means id two and the id type is Long,
            // if id2 exists, print the student(id = 2), otherwise, print "no found"
            studentRepository
                    .findById(2L)
                    .ifPresentOrElse(System.out::println, () -> {
                System.out.println("Student with ID2 not found");
            } );

            studentRepository
                    .findById(3L)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Student with ID3 not found"));


            List<Student> studentList = studentRepository.findAll();
            studentList.forEach(System.out::println);

            studentRepository.deleteById(1L);
            System.out.println(studentRepository.count());

             */


        };
    }

    private void sorting(StudentRepository studentRepository) {
        //sorting
        Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
        studentRepository
                .findAll(sort)
                .forEach(student -> System.out.println(student.getFirstName()));

        Sort sort2 = Sort.by("firstName").ascending()
                        .and(Sort.by("age").descending());
        studentRepository
                .findAll(sort2)
                .forEach(student -> System.out.println(student.getFirstName() + " " + student.getAge()));
    }

    private void generateStudents(StudentRepository studentRepository) {
        // using java faker to create more students
        //1. inject java faker dependency in maven
        //2 using loop to add
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17,55)
                    );
            studentRepository.save(student);
        }
    }
}
