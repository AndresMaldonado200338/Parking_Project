package edu.uptc.swii.parkingapp.accessControlService.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessRequestDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessResponseDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByDateDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByEmployeeDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.mappers.AccessControlMapper;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckInCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckOutCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.handlers.AccessControlCommandHandler;
import edu.uptc.swii.parkingapp.accessControlService.application.handlers.AccessControlQueryHandler;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.AllEmployeesByDateQuery;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.EmployeeByDatesQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import edu.uptc.swii.parkingapp.accessControlService.application.handlers.AccessControlCommandHandler;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/access")
@Tag(name = "Access Control Service", description = "Microservicio para el control de acceso de empleados")
@CrossOrigin(origins = "http://127.0.0.1:5500", 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AccessControlController {
    
    private final AccessControlCommandHandler commandHandler;
    private final AccessControlQueryHandler queryHandler;
    private final AccessControlMapper mapper;

    public AccessControlController(AccessControlCommandHandler commandHandler, 
                                AccessControlQueryHandler queryHandler,
                                AccessControlMapper mapper) {
        this.commandHandler = commandHandler;
        this.queryHandler = queryHandler;
        this.mapper = mapper;
    }

    @Operation(summary = "Registrar entrada empleado", description = "Registra la entrada de un empleado al parqueadero")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrada registrada exitosamente", content = @Content(schema = @Schema(implementation = AccessResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validación fallida (ej. empleado inactivo, check-in duplicado sin check-out previo)", content = @Content()),
        @ApiResponse(responseCode = "404", description = "El empleado no existe", content = @Content())
    })
    @PostMapping("/usercheckin")
    public ResponseEntity<AccessResponseDTO> registerCheckIn(
            @Valid @RequestBody AccessRequestDTO request) {
        UserCheckInCommand command = mapper.toCheckInCommand(request);
        AccessResponseDTO response = commandHandler.handleCheckIn(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar salida empleado", description = "Registra la salida de un empleado del parqueadero")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salida registrada exitosamente", content = @Content(schema = @Schema(implementation = AccessResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validación fallida (ej. empleado inactivo, check-out sin check-in previo)", content = @Content()),
        @ApiResponse(responseCode = "404", description = "El empleado no existe", content = @Content())
    })
    @PostMapping("/usercheckout")
    public ResponseEntity<AccessResponseDTO> registerCheckOut(
            @Valid @RequestBody AccessRequestDTO request) {
        UserCheckOutCommand command = mapper.toCheckOutCommand(request);
        AccessResponseDTO response = commandHandler.handleCheckOut(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generar reporte de empleados por fecha", description = "Obtiene un reporte de todos los empleados que ingresaron en una fecha específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(schema = @Schema(implementation = ReportByDateDTO.class))),
        @ApiResponse(responseCode = "400", description = "Fecha inválida o formato incorrecto", content = @Content())
    })
    @GetMapping("/allemployeesbydate")
    public ResponseEntity<List<ReportByDateDTO>> generateReportByDate(
            @Parameter(description = "Fecha en formato YYYY-MM-DD", example = "2025-06-02", required = true)
            @RequestParam String date) {
        List<ReportByDateDTO> response = queryHandler.handle(new AllEmployeesByDateQuery(date));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generar reporte de empleados por fechas", description = "Obtiene un reporte de un empleado específico entre dos fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(schema = @Schema(implementation = ReportByEmployeeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Fechas inválidas o formato incorrecto", content = @Content()),
    })
    @GetMapping("/employeebydates")
    public ResponseEntity<List<ReportByEmployeeDTO>> generateReportByEmployeeAndDates(
            @Parameter(description = "ID del empleado", example = "EMP12345", required = true)
            @RequestParam String employeeId,
            @Parameter(description = "Fecha inicio (yyyy-MM-dd)", example = "2025-06-01", required = true)
            @RequestParam String startDate,
            @Parameter(description = "Fecha fin (yyyy-MM-dd)", example = "2025-06-30", required = true)
            @RequestParam String endDate) {
        List<ReportByEmployeeDTO> response = queryHandler.handle(
            new EmployeeByDatesQuery(employeeId, startDate, endDate));
        return ResponseEntity.ok(response);
    }
}