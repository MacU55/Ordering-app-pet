package org.example.company.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.company.dto.request.RequestEmployee;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Department department;

    public Employee(String name, double salary, String email, Department department) {
        this.name = name;
        this.salary = salary;
        this.email = email;
        this.department = department;
    }

    @ManyToMany(mappedBy = "employees")
    private Set<Order> orders = new HashSet<>();

    public void updateEmployee(RequestEmployee r) {
        if(r.name() != null) this.name = r.name();
        if(r.salary() != null) this.salary = r.salary();
        if(r.email() != null) this.email = r.email();
        if(r.department() != 0) this.department = Department.getByCode(r.department());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee other)) return false;
        return id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
