Post
localhost:8080/employee/createemployee
{
  "document": "123456789",
  "firstname": "Juan",
  "lastname": "Pérez",
  "email": "juan.perez@example.com",
  "phone": "+573001234567"
}

Actualizar
PUT
localhost:8080/employee/updateemployee/123456789
{
    "document": "123456789",
    "firstname": "pepito",
    "lastname": "perez",
    "email": "nuevo.email@example.com",
    "phone": "+573001234569"
}


Listar todos
GET
localhost:8080/employee/findallemployees
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
PATCH
localhost:8080/employee/disableemployee/{document}

{
  "status": false,
  "message": "Employee is inactive"
}