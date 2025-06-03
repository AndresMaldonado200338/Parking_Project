# Instrucciones para Endpoints del Servicio de Login (LoginService)

## Descripción
Microservicio para registro y autenticación de usuarios mediante JWT.

## Endpoints

### 1. Registrar nuevo usuario (registeruser)

**Método**: `POST`

**URL**: `/login/createuser`

**Headers**:
Content-Type: application/json

**Cuerpo de solicitud (Request Body)**:
```json
{
  "userID": 12345,
  "password": "miClaveSegura123"
}
``` 

- `userID` (Long obligatorio) identificador de usuario, debe ser un valor numérico.

- `password` (String obligatorio) debe tener un valor mínimo de 8 caracteres.

**Respuesta exitosa**
200 OK
"User created successfully"

**Errores**
- `400 Bad Request`:

no debe ser nulo, no debe estar vacío (campos vacíos)

La contraseña debe tener al menos 8 caracteres (Contraseña menor a 8 caracteres)

- `403 Forbidden`:

userID no es totalmente numérico

userID ya existe

### 1. Autenticar usuario (authuser)

**Método**: `POST`

**URL**: `/login/authuser`

**Headers**:
Content-Type: application/json

**Cuerpo de solicitud (Request Body)**:
```json
{
  "userID": 12345,
  "password": "miClaveSegura123"
}
``` 

- `userID` (Long obligatorio) identificador de usuario, debe ser un valor numérico.

- `password` (String obligatorio) debe tener un valor mínimo de 8 caracteres.

**Respuesta exitosa**
200 OK
```json
{
    "message": "Login successful",
    "token": "token-secreto"
}
```

- `token-secreto` Valor de token generado por Jwt

**Respuesta fallida** (Implícita 200 OK)
```Json
{
    "message": "Invalid credentials",
    "token": null
}
```
No se recibieron todas las credenciales o están incompletas, contraseña incorrecta, userID incompleto

**Errores**

- `403 Forbidden`:

userID no es totalmente numérico