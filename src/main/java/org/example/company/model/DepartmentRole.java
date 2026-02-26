//package org.example.company.model;
//
//import lombok.Getter;
//
//@Getter
//public enum RoleTypes {
//
//    STORE("Store", 1),
//    LAB("Laboratory", 2),
//    ACCOUNTING("Accounting", 3),
//    ADMIN("Administration", 4);
//
//    private final String displayName;
//    private final int code;
//
//    RoleTypes(String displayName, int code) {
//        this.displayName = displayName;
//        this.code = code;
//    }
//
//    public static RoleTypes getByCode(int code) {
//        for (RoleTypes departmentRole : values()) {
//            if (departmentRole.getCode() == code) {
//                return departmentRole;
//            }
//        }
//        throw new IllegalArgumentException("Invalid RoleTypes Code: " + code);
//    }
//
//    public DepartmentInfo getInfo() {
//        return switch (this) {
//            case STORE -> new DepartmentInfo(displayName, code, "Sales floor and warehouse");
//            case LAB -> new DepartmentInfo(displayName, code, "Research and testing");
//            case ACCOUNTING -> new DepartmentInfo(displayName, code, "Finance and accounting");
//            case ADMIN -> new DepartmentInfo(displayName, code, "Management and HR");
//        };
//    }
//
//    public record DepartmentInfo(String displayName, int code, String description) {}
//}
