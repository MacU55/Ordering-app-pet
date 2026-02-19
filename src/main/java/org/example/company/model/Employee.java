package org.example.company.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    private String name;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private DepartmentRole departmentRole;

    public Employee(String name, UUID uuid, double salary, String email) {
        this.name = name;
        this.uuid = uuid;
        this.salary = salary;
        this.email = email;
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    @ManyToMany(mappedBy = "employees")
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(mappedBy = "employees")
    private Set<Role> roles = new HashSet<>();

    public void updateEmployee(RequestEmployee r) {
        if(r.name() != null) this.name = r.name();
        if(r.salary() != null) this.salary = r.salary();
//        if(r.department() != null) this.d
    }

    @PrePersist
    public void setUUID(){
        if(this.uuid == null) this.uuid = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
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
