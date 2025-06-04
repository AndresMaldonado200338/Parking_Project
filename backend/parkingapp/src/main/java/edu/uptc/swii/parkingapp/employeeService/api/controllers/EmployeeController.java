package edu.uptc.swii.parkingapp.employeeService.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.uptc.swii.parkingapp.employeeService.application.handlers.EmployeeQueryHandler;
import edu.uptc.swii.parkingapp.employeeService.application.queries.FindAllEmployeesQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeRequestDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeResponseDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.StatusDTO;
import edu.uptc.swii.parkingapp.employeeService.api.mappers.EmployeeMapper;
import edu.uptc.swii.parkingapp.employeeService.application.commands.CreateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.DisableEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.UpdateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.handlers.EmployeeCommandHandler;
import edu.uptc.swii.parkingapp.employeeService.application.handlers.EmployeeQueryHandler;
import edu.uptc.swii.parkingapp.employeeService.application.queries.FindAllEmployeesQuery;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/employee")
@Tag(name = "Employee Service", description = "Microservicio para la gestión de empleados")
@CrossOrigin(origins = "http://127.0.0.1:5500", 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH})
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

    @Operation(summary = "Crear empleado", description = "Crea un nuevo empleado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado creado exitosamente", content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida (ej. documento duplicado, datos inválidos)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "El tipo de empleado no existe", content = @Content())
    })
    @PostMapping("/createemployee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {
        CreateEmployeeCommand command = mapper.toCommand(request);
        EmployeeResponseDTO response = commandHandler.handleCreateEmployee(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar empleado", description = "Actualiza los datos de un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente", content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida (ej. documento inválido, datos incompletos)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "El empleado no existe", content = @Content())
    })
    @PutMapping("/updateemployee/{document}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "Documento del empleado a actualizar", required = true) @PathVariable String document,
            @Valid @RequestBody EmployeeRequestDTO request) {
        UpdateEmployeeCommand command = mapper.toUpdateCommand(document, request);
        EmployeeResponseDTO response = commandHandler.handleUpdateEmployee(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar todos los empleados", description = "Obtiene los datos de todos los empleados registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente", content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
    })
    @GetMapping("/findallemployees")
    public ResponseEntity<List<EmployeeResponseDTO>> findAllEmployees() {
        List<EmployeeResponseDTO> response = queryHandler.handle(new FindAllEmployeesQuery());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deshabilitar empleado", description = "Deshabilita un empleado cambiando su estado a inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado deshabilitado exitosamente", content = @Content(schema = @Schema(implementation = StatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "El empleado no existe", content = @Content())
    })
    @PatchMapping("/disableemployee/{document}")
    public ResponseEntity<StatusDTO> disableEmployee(
            @Parameter(description = "Documento del empleado a deshabilitar", required = true) @PathVariable String document) {
        StatusDTO response = commandHandler.handleDisableEmployee(new DisableEmployeeCommand(document));
        return ResponseEntity.ok(response);
    }
}