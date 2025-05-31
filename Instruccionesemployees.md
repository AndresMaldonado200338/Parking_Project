Post
localhost:8081/employee/createemployee
{
  "document": "123456789",
  "firstname": "Juan",
  "lastname": "Pérez",
  "email": "juan.perez@example.com",
  "phone": "+573001234567"
}

Actualizar
localhost:8081/employee/updateemployee/123456789
{
  "firstname": "Juan Carlos",
  "lastname": "Pérez Gómez",
  "email": "juan.c.perez@example.com",
  "phone": "+573001234568"
}


Listar todos
localhost:8081/employee/findallemployees
[
  {
    "document": "123456789",
    "firstname": "Juan Carlos",
    "lastname": "Pérez Gómez",
    "email": "juan.c.perez@example.com",
    "phone": "+573001234568",
    "status": true
  },
  {
    "document": "987654321",
    "firstname": "María",
    "lastname": "González",
    "email": "maria.gonzalez@example.com",
    "phone": "+573002345678",
    "status": true
  }
]

Desactivar Empleado
localhost:8081/employee/disableemployee/{document}

{
  "status": false,
  "message": "Employee is inactive"
}