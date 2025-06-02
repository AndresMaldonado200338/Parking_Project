package edu.uptc.swii.parkingapp.accessControlService.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.uptc.swii.parkingapp.accessControlService.application.handlers.AccessControlQueryHandler;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessRequestDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessResponseDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByDateDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByEmployeeDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.mappers.AccessControlMapper;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckInCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckOutCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.AllEmployeesByDateQuery;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.EmployeeByDatesQuery;
import edu.uptc.swii.parkingapp.accessControlService.application.handlers.AccessControlCommandHandler;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/access")
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

    @PostMapping("/usercheckin")
    public ResponseEntity<AccessResponseDTO> registerCheckIn(
            @Valid @RequestBody AccessRequestDTO request) {
        UserCheckInCommand command = mapper.toCheckInCommand(request);
        AccessResponseDTO response = commandHandler.handleCheckIn(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/usercheckout")
    public ResponseEntity<AccessResponseDTO> registerCheckOut(
            @Valid @RequestBody AccessRequestDTO request) {
        UserCheckOutCommand command = mapper.toCheckOutCommand(request);
        AccessResponseDTO response = commandHandler.handleCheckOut(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allemployeesbydate")
    public ResponseEntity<List<ReportByDateDTO>> generateReportByDate(
            @RequestParam String date) {
        List<ReportByDateDTO> response = queryHandler.handle(new AllEmployeesByDateQuery(date));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employeebydates")
    public ResponseEntity<List<ReportByEmployeeDTO>> generateReportByEmployeeAndDates(
            @RequestParam String employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<ReportByEmployeeDTO> response = queryHandler.handle(
            new EmployeeByDatesQuery(employeeId, startDate, endDate));
        return ResponseEntity.ok(response);
    }
}