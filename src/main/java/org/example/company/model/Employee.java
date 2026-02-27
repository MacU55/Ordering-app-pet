package org.example.company.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.company.dto.request.RequestEmployee;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false, unique = true)
    private String email;

    public Employee(String userName, double salary) {
        this.userName = userName;
        this.salary = salary;
        this.uuid = UUID.randomUUID();
    }

    @ManyToMany(mappedBy = "employees")
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(mappedBy = "employees")
    @ToString.Exclude
    private Set<Role> roleEntities = new HashSet<>();

    public void updateEmployee(RequestEmployee r) {
        if(r.name() != null) this.userName = r.name();
        if(r.salary() != null) this.salary = r.salary();
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
