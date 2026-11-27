package Main.fullEmpCar;

import Main.entity.Career;
import Main.entity.Employee;
import jakarta.validation.Valid;

public class FullEmpCar {
    @Valid
    private Employee employee;

    @Valid
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
