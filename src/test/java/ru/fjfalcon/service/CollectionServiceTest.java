package ru.fjfalcon.service;


import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import ru.fjfalcon.model.Department;
import ru.fjfalcon.model.Employee;

public abstract class CollectionServiceTest {

    protected CollectionService collectionService;
    protected CollectionServiceTestDataProvider dataProvider;


    @Before
    public abstract void setUp();

    @Test
    public void generateDepartmentMap() {
        Map<Department, List<Employee>> map = collectionService.generateDepartmentMap(dataProvider.getEmployees());

        assert(map.equals(dataProvider.getDepartmentsMap()));
    }

    @Test
    public void countByWorkDaysDepartmentAndEmployee() {
        Map<Integer, Map<Department, List<Employee>>> map =
                collectionService.countByWorkDaysDepartmentAndEmployee(dataProvider.getEmployees());

        assert(map.equals(dataProvider.getWorkDaysCountMap()));
    }

    @Test
    public void departmentsSalary() {
        Map<Department, BigDecimal> map = collectionService.departmentsSalary(dataProvider.getEmployees());

        assert(map.equals(dataProvider.getDepartmentsSalaryMap()));
    }

    @Test
    public void groupByDepartmentWithEmployeeAndSalary() {
        Map<Department, Map<Employee, BigDecimal>> map =
                collectionService.groupByDepartmentWithEmployeeAndSalary(dataProvider.getEmployees());

        assert map.equals(dataProvider.getDepartmentWithEmployeeAndSalaryMap());
    }

    @Test
    public void groupByDepartmentNameWithEmployeeNameAndSalaryMap() {
        Map<String, Map<String, BigDecimal>> map =
                collectionService.groupByDepartmentNameWithEmployeeNameAndSalayMap(dataProvider.getEmployees());

        assert map.equals(dataProvider.getDepartmentNameWithEmployeeNameAndSalayMap());
    }

    @Test
    public void departmentSalaryForAgeMoreThan30() {
        Map<Department, BigDecimal> map = collectionService.departmentSalaryForAgeMoreThan30(dataProvider.getEmployees());

        assert map.equals(dataProvider.getDepartmentsSalaryMoreThan30Map());
    }
}