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

    interface KeyMapper<T, K> {
        K getKey(T object);
    }

    interface CollisionResolver<T> {
        T resolve(T obj1, T obj2);
    }

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
        return groupWithSalary(employees, new KeyMapper<Employee, Employee>() {
            @Override
            public Employee getKey(Employee object) {
                return object;
            }
        }, new KeyMapper<Department, Department>() {
            @Override
            public Department getKey(Department object) {
                return object;
            }
        }, new CollisionResolver<BigDecimal>() {
            @Override
            public BigDecimal resolve(BigDecimal obj1, BigDecimal obj2) {
                return obj1.add(obj2);
            }
        });
    }

    @Override
    public Map<String, Map<String, BigDecimal>> groupByDepartmentNameWithEmployeeNameAndSalayMap(
            List<Employee> employees) {
        return groupWithSalary(employees, new KeyMapper<Employee, String>() {
            @Override
            public String getKey(Employee object) {
                return object.getName();
            }
        }, new KeyMapper<Department, String>() {
            @Override
            public String getKey(Department object) {
                return object.getName();
            }
        }, new CollisionResolver<BigDecimal>() {
            @Override
            public BigDecimal resolve(BigDecimal obj1, BigDecimal obj2) {
                return obj1.add(obj2);
            }
        });
    }

    private <D, E>  Map<D, Map<E, BigDecimal>>groupWithSalary(List<Employee> employees,
                                                              KeyMapper<Employee, E> employeeMapper,
                                                              KeyMapper<Department, D> departmentMapper,
                                                              CollisionResolver<BigDecimal> collisionResolver) {

        Map<Department, List<Employee>> departmentMap = generateDepartmentMap(employees);
        Map<D, Map<E, BigDecimal>> departmentEmployeeSalaryMap =
                new HashMap<D, Map<E, BigDecimal>>();

        for (Department dep: departmentMap.keySet()) {
            D departmentKey = departmentMapper.getKey(dep);
            Map<E, BigDecimal> salaryMap = new HashMap<E, BigDecimal>();

            for (Employee employee: departmentMap.get(dep)) {
                E employeeKey = employeeMapper.getKey(employee);
                BigDecimal currentSalary = salaryMap.get(employeeKey);
                BigDecimal newSalary = (currentSalary == null) ?
                        countSalary(employee) :
                        collisionResolver.resolve(currentSalary, countSalary(employee));
                salaryMap.put(employeeKey, newSalary);
            }
            departmentEmployeeSalaryMap.put(departmentKey, salaryMap);
        }
        return departmentEmployeeSalaryMap;
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

