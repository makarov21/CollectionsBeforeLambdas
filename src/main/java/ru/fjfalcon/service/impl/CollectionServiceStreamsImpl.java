package ru.fjfalcon.service.impl;

import ru.fjfalcon.model.Department;
import ru.fjfalcon.model.Employee;
import ru.fjfalcon.model.WorkDay;
import ru.fjfalcon.service.CollectionService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class CollectionServiceStreamsImpl implements CollectionService {
    @Override
    public Map<Department, List<Employee>> generateDepartmentMap(List<Employee> employees) {
        return employees.stream().collect(groupingBy(Employee::getDepartment));
    }

    @Override
    public Map<Integer, Map<Department, List<Employee>>> countByWorkDaysDepartmentAndEmployee(List<Employee> employees) {
        return employees.stream().collect(
                groupingBy(
                        e -> e.getWorkDayList().size(),
                        groupingBy(Employee::getDepartment)
                )
        );
    }

    @Override
    public Map<Department, BigDecimal> departmentsSalary(List<Employee> employees) {
        //Avoid optionals without start reduce value ??
        //Do with groupingBy ??
        return generateDepartmentMap(employees).entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> {
                    return entry.getValue().stream()
                            .map(Employee::getWorkDayList)
                            .flatMap(List::stream)
                            .map(WorkDay::getSalary)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                })
        );
    }

    @Override
    public Map<Department, Map<Employee, BigDecimal>> groupByDepartmentWithEmployeeAndSalary(List<Employee> employees) {
         return employees.stream().collect(
                groupingBy(
                        Employee::getDepartment,
                        Collectors.toMap(
                                employee -> employee,
                                this::countSalary
                        )
                )
        );
    }

    @Override
    public Map<String, Map<String, BigDecimal>> groupByDepartmentNameWithEmployeeNameAndSalayMap(List<Employee> employees) {
        return employees.stream().collect(
                groupingBy(
                        employee -> employee.getDepartment().getName(),
                        Collectors.toMap(
                                Employee::getName,
                                this::countSalary,
                                BigDecimal::add
                        )
                )
        );
    }

    @Override
    public Map<Department, BigDecimal> departmentSalaryForAgeMoreThan30(List<Employee> employees) {
        return departmentsSalary(
                employees.stream().filter(employee -> employee.getAge() > 30).collect(toList())
        );
    }

    private BigDecimal countSalary(Employee employee) {
        return employee.getWorkDayList().stream()
                .map(WorkDay::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
