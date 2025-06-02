# Instrucciones para Endpoints del Servicio de Control de Acceso (AccessControlService)

Este documento describe los endpoints disponibles para el microservicio de control de acceso. Asumiendo que el servicio corre en `localhost:8081` (o el puerto configurado para la aplicación principal).

---

## 1. Registrar Entrada de Empleado (Check-In)

Registra la hora de entrada de un empleado.

- **URL**: `/access/usercheckin`
- **Método**: `POST`
- **Content-Type**: `application/json`

### Request Body (`AccessRequestDTO`)

```json
{
  "employeeId": "EMP12345",
  "accessDateTime": "2025-06-02T09:00:00"
}
```

- `employeeId` (String, Obligatorio): Identificador del empleado. Debe tener entre 5 y 20 caracteres.
- `accessDateTime` (String, Opcional): Fecha y hora del acceso en formato ISO. Si se omite, se usa la hora actual del servidor.

### Respuesta Exitosa (200 OK - `AccessResponseDTO`)

```json
{
  "employeeId": "EMP12345",
  "eventType": "ENTRADA",
  "accessDateTime": "2025-06-02T09:00:00",
  "message": "Check-in registered successfully"
}
```

### Posibles Errores

- `400 Bad Request`: Validación fallida (ej. empleado inactivo, check-in duplicado sin check-out previo).
- `404 Not Found`: El empleado no existe.

---

## 2. Registrar Salida de Empleado (Check-Out)

Registra la hora de salida de un empleado.

- **URL**: `/access/usercheckout`
- **Método**: `POST`
- **Content-Type**: `application/json`

### Request Body (`AccessRequestDTO`)

```json
{
  "employeeId": "EMP12345",
  "accessDateTime": "2025-06-02T17:30:00"
}
```

- `employeeId` (String, Obligatorio): Identificador del empleado.
- `accessDateTime` (String, Opcional): Fecha y hora del acceso en formato ISO.

### Respuesta Exitosa (200 OK - `AccessResponseDTO`)

```json
{
  "employeeId": "EMP12345",
  "eventType": "SALIDA",
  "accessDateTime": "2025-06-02T17:30:00",
  "message": "Check-out registered successfully"
}
```

### Posibles Errores

- `400 Bad Request`: Validación fallida (ej. empleado inactivo, no hay un check-in previo correspondiente).
- `404 Not Found`: El empleado no existe.

---

## 3. Generar Reporte de Todos los Empleados por Fecha

Obtiene una lista de todos los registros de acceso (entradas y salidas) para una fecha específica.

- **URL**: `/access/allemployeesbydate`
- **Método**: `GET`
- **Query Parameter**:
  - `date` (String, Obligatorio): Fecha para el reporte en formato "YYYY-MM-DD".

### Ejemplo de Solicitud

`GET http://localhost:8081/access/allemployeesbydate?date=2025-06-02`

### Respuesta Exitosa (200 OK - `List<ReportByDateDTO>`)

```json
[
  {
    "employeeId": "EMP12345",
    "employeeName": "Employee: EMP12345",
    "entryTime": "2025-06-02T09:00:00",
    "exitTime": "2025-06-02T17:30:00",
    "duration": "08:30"
  },
  {
    "employeeId": "EMP67890",
    "employeeName": "Employee: EMP67890",
    "entryTime": "2025-06-02T08:30:00",
    "exitTime": "2025-06-02T17:00:00",
    "duration": "08:30"
  }
]
```

### Posibles Errores

- `400 Bad Request`: Formato de fecha incorrecto o falta el parámetro.

---

## 4. Generar Reporte de un Empleado por Rango de Fechas

Obtiene los registros de acceso y la duración total para un empleado específico dentro de un rango de fechas.

- **URL**: `/access/employeebydates`
- **Método**: `GET`
- **Query Parameters**:
  - `employeeId` (String, Obligatorio): Identificador del empleado.
  - `startDate` (String, Obligatorio): Fecha de inicio del rango en formato "YYYY-MM-DD".
  - `endDate` (String, Obligatorio): Fecha de fin del rango en formato "YYYY-MM-DD".

### Ejemplo de Solicitud

`GET http://localhost:8081/access/employeebydates?employeeId=EMP12345&startDate=2025-06-01&endDate=2025-06-07`

### Respuesta Exitosa (200 OK - `List<ReportByEmployeeDTO>`)

```json
[
  {
    "employeeId": "EMP12345",
    "employeeName": "Employee: EMP12345",
    "accessRecords": [
      {
        "entryTime": "2025-06-02T09:00:00",
        "exitTime": "2025-06-02T17:30:00",
        "duration": "08:30"
      },
      {
        "entryTime": "2025-06-03T09:05:00",
        "exitTime": "2025-06-03T17:35:00",
        "duration": "08:30"
      }
    ],
    "totalDuration": "17:00"
  }
]
```

### Posibles Errores

- `400 Bad Request`: Parámetros faltantes, formato incorrecto, o `startDate` posterior a `endDate`.

---

## Consideraciones Adicionales

- **Puerto del Servidor**: Asegúrate de que el puerto configurado es correcto (`localhost:8081`).
- **Nombres de Empleados en Reportes**: Actualmente se muestra como `"Employee: {employeeId}"`. Para nombres reales, se requiere integración con `EmployeeService`.
- **Formato de Fechas y Horas**: Usa el formato ISO para `LocalDateTime` y "YYYY-MM-DD" para fechas en parámetros de consulta.