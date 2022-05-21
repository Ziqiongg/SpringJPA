package com.example.demo.model;


import javax.persistence.*;

@Entity(name = "StudentIdCard")
@Table(name = "student_id_card",
        uniqueConstraints = {
                @UniqueConstraint(name = "student_id_card_number_unique",
                        columnNames = "card_number")}//email is unique
)
public class StudentIdCard {

    @Id
    @SequenceGenerator(
            name = "student_card_id_sequence",
            sequenceName = "student_card_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_card_id_sequence"

    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(name = "card_number",
    nullable = false,
    length = 15
    )
    private String cardNumber;

    //from the student entity, referencedColumnName = "id" the id here is from student entity
    //join a new table, and it is one to one mapping
    //cascade = CascadeType.ALL means when you add a StudentIdCard, you will auto-add Student to the student table
    //fetch = FetchType.EAGER means when you want something from StudentIdCard, it will load StudentIdCard and Student,
    // fetch = FetchType.LAZY means only load the thing you want, many-one or many-many use it
    //foreignKey = @ForeignKey(name = "student_id_fk") change the foreign key name
    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "student_id_fk")
    )
    private Student student;


    public StudentIdCard(String cardNumber, Student student) {
        this.cardNumber = cardNumber;
        this.student = student;
    }

    public StudentIdCard() {
    }

    public StudentIdCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "StudentIdCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", student=" + student +
                '}';
    }
}
