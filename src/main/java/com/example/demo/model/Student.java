package com.example.demo.model;


import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Student")
//match the table name
@Table(name = "student",
        uniqueConstraints = {
        @UniqueConstraint(name = "student_email_unique", columnNames = "email") //email is unique
        }
)
public class Student {

    //generate id sequence and can't change, match column, allocationSize = 1 means each time increase by 1
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"

    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;


    //the type is not VARCHAR, it's TEXT and NOT NULL, match the column
    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "age",
            nullable = false
    )
    private Integer age;

    //"student" here refer from StudentCard -- student variable
    //orphanRemoval = true means you can delete the student, default = false
    //in the StudentCard, if orphanRemoval = true, will delete card and student both
    @OneToOne(mappedBy = "student", orphanRemoval = true,cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private StudentIdCard studentIdCard;


    //mappedBy = "student" is from Book entity--- student
    //orphanRemoval = true, if you move student, the book will be moved
    @OneToMany(
            mappedBy = "student",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY

    )
    private List<Book> books = new ArrayList<>();


    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "student"    //the student here is found by EnrolmentId --- student
    )
    private List<Enrolment> enrolments = new ArrayList<>();


    /*  We don't need course anymore, we can use
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "enrolment",
            joinColumns = @JoinColumn(
                    name = "student_id",
                    foreignKey = @ForeignKey(name = "enrolment_student_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id",
                    foreignKey = @ForeignKey(name = "enrolment_course_id_fk")
            )
    )
    private List<Course> courses = new ArrayList<>();

     */



    //auto generate id, don't need to pass in
    public Student(String firstName, String lastName, String email, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Book> getBooks() {
        return books;
    }

    public StudentIdCard getStudentIdCard() {
        return studentIdCard;
    }

    public void setStudentIdCard(StudentIdCard studentIdCard) {
        this.studentIdCard = studentIdCard;
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }


    // add and remove enrolment
    public void addEnrolment(Enrolment enrolment){
        if (!enrolments.contains(enrolment)){
            enrolments.add(enrolment);
        }
    }

    public void removeEnrolment(Enrolment enrolment){
        if (enrolments.contains(enrolment)){
            enrolments.remove(enrolment);
        }
    }


    //add book
    public void addBook(Book book){
        if (!this.books.contains(book)){
            this.books.add(book);
            book.setStudent(this);
        }
    }

    public void remove(Book book){
        if (this.books.contains(book)){
            this.books.remove(book);
            book.setStudent(null);
        }
    }


    /*
    //give things both sides
    public void enrolToCourse(Course course){
        courses.add(course);
        course.getStudents().add(this);
    }
    public void unEnrolmentCourse(Course course){
        courses.remove(course);
        course.getStudents().remove(this);
    }

     */




    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
