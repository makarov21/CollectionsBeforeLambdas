package ru.fjfalcon.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import ru.fjfalcon.model.Department;
import ru.fjfalcon.model.Employee;
import ru.fjfalcon.model.WorkDay;
import ru.fjfalcon.service.CollectionService;

public class CollectionServiceImpl implements CollectionService {
    @Override
    public Map<Department, List<Employee>> generateDepartmentMap(List<Employee> employees) {
        Map<Department, List<Employee>> departmentMap = new HashMap<Department, List<Employee>>();
        for (Employee employee: employees) {
            putInListIfAbsent(departmentMap, employee.getDepartment(), employee);
        }
        return departmentMap;
    }

    @Override
    public Map<Integer, Map<Department, List<Employee>>> countByWorkDaysDepartmentAndEmployee(List<Employee> employees) {
        Map<Integer, List<Employee>> workDaysEmployeeMap = new HashMap<Integer, List<Employee>>();
        for (Employee employee: employees) {
            putInListIfAbsent(workDaysEmployeeMap, employee.getWorkDayList().size(), employee);
        }

        Map<Integer, Map<Department, List<Employee>>> workDaysMap =
                new HashMap<Integer, Map<Department, List<Employee>>>();
        for (Integer key: workDaysEmployeeMap.keySet()) {
            workDaysMap.put(key, generateDepartmentMap(workDaysEmployeeMap.get(key)));
        }
        return workDaysMap;
    }

    @Override
    public Map<Department, BigDecimal> departmentsSalary(List<Employee> employees) {
        Map<Department, BigDecimal> departmentsSalaryMap = new HashMap<Department, BigDecimal>();
        for (Employee employee: employees) {
            BigDecimal currentSalary = departmentsSalaryMap.get(employee.getDepartment());
            BigDecimal newSalary =
                    (currentSalary == null) ? countSalary(employee) : currentSalary.add(countSalary(employee));
            departmentsSalaryMap.put(employee.getDepartment(), newSalary);
        }
        return departmentsSalaryMap;
    }

    @Override
    public Map<Department, Map<Employee, BigDecimal>> groupByDepartmentWithEmployeeAndSalary(List<Employee> employees) {
        Map<Department, List<Employee>> departmentMap = generateDepartmentMap(employees);
        Map<Department, Map<Employee, BigDecimal>> departmentEmployeeSalaryMap =
                new HashMap<Department, Map<Employee, BigDecimal>>();

        for (Department key: departmentMap.keySet()) {
            Map<Employee, BigDecimal> salaryMap = new HashMap<Employee, BigDecimal>();
            for (Employee employee: departmentMap.get(key)) {
                salaryMap.put(employee, countSalary(employee));
            }
            departmentEmployeeSalaryMap.put(key, salaryMap);
        }
        return departmentEmployeeSalaryMap;
    }

    @Override
    public Map<String, Map<String, BigDecimal>> groupByDepartmentNameWithEmployeeNameAndSalayMap(
            List<Employee> employees) {

        Map<Department, Map<Employee, BigDecimal>> departmentEmployeeSalaryMap =
                groupByDepartmentWithEmployeeAndSalary(employees);
        Map<String, Map<String, BigDecimal>> departmentNameEmployeeNameSalaryMap =
                new HashMap<String, Map<String, BigDecimal>>();

        for (Department department: departmentEmployeeSalaryMap.keySet()) {
            Map<Employee, BigDecimal> salaryMap = departmentEmployeeSalaryMap.get(department);
            Map<String, BigDecimal> nameSalaryMap = new HashMap<String, BigDecimal>();
            for (Employee employee: salaryMap.keySet()) {
                //Names collision
                BigDecimal currentSalary = nameSalaryMap.get(employee.getName());
                BigDecimal newSalary = (currentSalary == null) ?
                        salaryMap.get(employee) :
                        salaryMap.get(employee).add(currentSalary);
                nameSalaryMap.put(employee.getName(), newSalary);
            }
            departmentNameEmployeeNameSalaryMap.put(department.getName(), nameSalaryMap);
        }

        return departmentNameEmployeeNameSalaryMap;
    }

    @Override
    public Map<Department, BigDecimal> departmentSalaryForAgeMoreThan30(List<Employee> employees) {
        List<Employee> oldEmployees = new ArrayList<Employee>();
        for (Employee employee: employees) {
            if (employee.getAge() > 30) {
                oldEmployees.add(employee);
            }
        }
        return departmentsSalary(oldEmployees);
    }

    private BigDecimal countSalary(Employee employee) {
        BigDecimal salary = new BigDecimal(0);
        for (WorkDay day: employee.getWorkDayList()) {
            salary = salary.add(day.getSalary());
        }
        return salary;
    }

    private <K,V> void putInListIfAbsent(Map<K,List<V>> map, K key, V value) {
        List<V> currentList = map.get(key);
        if (currentList == null) {
            map.put(key, new ArrayList<V>(Collections.singletonList(value)));
        } else {
            currentList.add(value);
        }
    }
}

