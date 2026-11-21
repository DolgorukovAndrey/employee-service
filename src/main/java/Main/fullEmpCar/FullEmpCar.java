package Main.fullEmpCar;

import Main.entity.Career;
import Main.entity.Employee;

public class FullEmpCar {
    private Employee employee;
    private Career career;

    public FullEmpCar(){}
    public FullEmpCar(Employee employee, Career career){
        this.employee = employee;
        this.career = career;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
