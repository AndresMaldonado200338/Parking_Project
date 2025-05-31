package edu.uptc.swii.parkingapp.employeeService.application.queries;

public class FindAllEmployeesQuery {
    // Puedes agregar parámetros de paginación/filtrado si es necesario
    private int page;
    private int size;
    private String filter;

    public FindAllEmployeesQuery() {
        this.page = 0;
        this.size = 10;
        this.filter = "";
    }

    public FindAllEmployeesQuery(int page, int size, String filter) {
        this.page = page;
        this.size = size;
        this.filter = filter;
    }

    // Getters
    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getFilter() {
        return filter;
    }
}