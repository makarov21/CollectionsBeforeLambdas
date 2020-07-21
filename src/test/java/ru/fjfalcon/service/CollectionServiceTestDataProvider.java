package ru.fjfalcon.service;

import ru.fjfalcon.model.Department;
import ru.fjfalcon.model.Employee;
import ru.fjfalcon.model.WorkDay;

import java.math.BigDecimal;
import java.util.*;

public class CollectionServiceTestDataProvider {
    private final Department it = new Department("IT");
    private final Department accounting = new Department("Accounting");
    private final Department sales = new Department("Sales");

    private final WorkDay workDay10 = new WorkDay(BigDecimal.TEN);
    private final WorkDay workDay1 = new WorkDay(BigDecimal.ONE);
    private final WorkDay workDay30 = new WorkDay(new BigDecimal(30));

    private final Employee antony = new Employee("Antony", 21, it, Arrays.asList(workDay1,workDay1,workDay1,workDay1,workDay1,workDay1));
    private final Employee philip = new Employee("Philip", 32, accounting, Arrays.asList(workDay10,workDay10,workDay10,workDay10,workDay1));
    private final Employee dexter = new Employee("Dexter", 29, sales, Arrays.asList(workDay10,workDay10,workDay10,workDay10,workDay30));
    private final Employee max = new Employee("Max", 45, sales, Arrays.asList(workDay30, workDay30));
    private final Employee annie = new Employee("Annie", 18, it, Collections.singletonList(workDay1));

    public List<Employee> getEmployees() {
        return Arrays.asList(antony, philip, dexter, max, annie);
    }

    public Map<Department, List<Employee>> getDepartmentsMap() {
        return new HashMap<>() {{
            put(it, Arrays.asList(antony, annie));
            put(accounting, Collections.singletonList(philip));
            put(sales, Arrays.asList(dexter, max));
        }};
    }

    public Map<Integer, Map<Department, List<Employee>>> getWorkDaysCountMap() {
        return new HashMap<>() {{
            put(6, new HashMap<>(){{put(it, Collections.singletonList(antony));}});
            put(5, new HashMap<>(){{
                put(accounting, Collections.singletonList(philip));
                put(sales, Collections.singletonList(dexter));
            }});
            put(2, new HashMap<>(){{put(sales, Collections.singletonList(max));}});
            put(1, new HashMap<>(){{put(it, Collections.singletonList(annie));}});
        }};
    }

    public Map<Department, BigDecimal> getDepartmentsSalaryMap() {
        return new HashMap<>() {{
            put(it, new BigDecimal(7));
            put(accounting, new BigDecimal(41));
            put(sales, new BigDecimal(130));
        }};
    }

    public Map<Department, Map<Employee, BigDecimal>> getDepartmentWithEmployeeAndSalaryMap() {
        return new HashMap<>() {{
            put(it, new HashMap<>(){{
                put(antony, new BigDecimal(6));
                put(annie, BigDecimal.ONE);
            }});
            put(accounting, new HashMap<>(){{
                put(philip, new BigDecimal(41));
            }});
            put(sales, new HashMap<>(){{
                put(dexter, new BigDecimal(70));
                put(max, new BigDecimal(60));
            }});
        }};
    }

    public Map<String, Map<String, BigDecimal>> getDepartmentNameWithEmployeeNameAndSalayMap() {
        return new HashMap<>() {{
            put(it.getName(), new HashMap<>(){{
                put(antony.getName(), new BigDecimal(6));
                put(annie.getName(), BigDecimal.ONE);
            }});
            put(accounting.getName(), new HashMap<>(){{
                put(philip.getName(), new BigDecimal(41));
            }});
            put(sales.getName(), new HashMap<>(){{
                put(dexter.getName(), new BigDecimal(70));
                put(max.getName(), new BigDecimal(60));
            }});
        }};
    }

    public Map<Department, BigDecimal> getDepartmentsSalaryMoreThan30Map() {
        return new HashMap<>() {{
            put(accounting, new BigDecimal(41));
            put(sales, new BigDecimal(60));
        }};
    }
}
