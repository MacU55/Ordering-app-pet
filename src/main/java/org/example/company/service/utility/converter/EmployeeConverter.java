package org.example.company.service.utility.converter;

import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.response.ResponseEmployee;
import org.example.company.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter extends ConverterImpl<Employee, ResponseEmployee, RequestEmployee> {

    public EmployeeConverter() {}

    @Override
    public ResponseEmployee convertToDTO(Employee employee) {
        return new ResponseEmployee(employee.getUuid(), employee.getName(), employee.getEmail());
    }

    @Override
    public Employee convertToEntity(RequestEmployee requestEmployee) {
        return new Employee(requestEmployee.name(), requestEmployee.salary() );
    }
}
