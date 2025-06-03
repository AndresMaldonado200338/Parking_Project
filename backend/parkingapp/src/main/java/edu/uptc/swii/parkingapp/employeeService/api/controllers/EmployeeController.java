package edu.uptc.swii.parkingapp.employeeService.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.uptc.swii.parkingapp.employeeService.application.handlers.EmployeeQueryHandler;
import edu.uptc.swii.parkingapp.employeeService.application.queries.FindAllEmployeesQuery;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeRequestDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeResponseDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.StatusDTO;
import edu.uptc.swii.parkingapp.employeeService.api.mappers.EmployeeMapper;
import edu.uptc.swii.parkingapp.employeeService.application.commands.CreateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.DisableEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.UpdateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.handlers.EmployeeCommandHandler;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    private final EmployeeCommandHandler commandHandler;
    private final EmployeeQueryHandler queryHandler;
    private final EmployeeMapper mapper;

    public EmployeeController(EmployeeCommandHandler commandHandler, 
                           EmployeeQueryHandler queryHandler,
                           EmployeeMapper mapper) {
        this.commandHandler = commandHandler;
        this.queryHandler = queryHandler;
        this.mapper = mapper;
    }

    @PostMapping("/createemployee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {
        CreateEmployeeCommand command = mapper.toCommand(request);
        EmployeeResponseDTO response = commandHandler.handleCreateEmployee(command);
        return ResponseEntity.ok(response);
    }

@PutMapping("/updateemployee/{document}")
public ResponseEntity<EmployeeResponseDTO> updateEmployee(
        @PathVariable String document,
        @Valid @RequestBody EmployeeRequestDTO request) {
    UpdateEmployeeCommand command = mapper.toUpdateCommand(document, request);
    EmployeeResponseDTO response = commandHandler.handleUpdateEmployee(command);
    return ResponseEntity.ok(response);
}

    @GetMapping("/findallemployees")
    public ResponseEntity<List<EmployeeResponseDTO>> findAllEmployees() {
        List<EmployeeResponseDTO> response = queryHandler.handle(new FindAllEmployeesQuery());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/disableemployee/{document}")
    public ResponseEntity<StatusDTO> disableEmployee(@PathVariable String document) {
        StatusDTO response = commandHandler.handleDisableEmployee(new DisableEmployeeCommand(document));
        return ResponseEntity.ok(response);
    }
    
}