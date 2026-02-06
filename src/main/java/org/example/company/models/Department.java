package org.example.company.models;

import lombok.Getter;

@Getter
public enum Department {

    STORE("Store", 1),
    LAB("Laboratory", 2),
    ACCOUNTING("Accounting", 3),
    ADMINISTRATION("Administration", 4);

    private final String displayName;
    private final int code;

    Department(String displayName, int code) {
        this.displayName = displayName;
        this.code = code;
    }

    public static Department getByCode(int code) {
        for (Department department : values()) {
            if (department.getCode() == code) {
                return department;
            }
        }
        throw new IllegalArgumentException("Invalid Department Code: " + code);
    }

    public DepartmentInfo getInfo() {
        return switch (this) {
            case STORE -> new DepartmentInfo(displayName, code, "Sales floor and warehouse");
            case LAB -> new DepartmentInfo(displayName, code, "Research and testing");
            case ACCOUNTING -> new DepartmentInfo(displayName, code, "Finance and accounting");
            case ADMINISTRATION -> new DepartmentInfo(displayName, code, "Management and HR");
        };
    }

    public record DepartmentInfo(String displayName, int code, String description) {}
}
