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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/access")
@CrossOrigin(origins = "http://localhost:8082", 
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