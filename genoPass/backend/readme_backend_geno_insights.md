# Backend - Control de Ingresos PRO

Backend desarrollado para el challenge técnico de Geno Insights. El objetivo principal fue productivizar una aplicación que originalmente guardaba toda la información en `localStorage`, agregando una API real con persistencia, almacenamiento de fotos, autenticación básica y lógica de visitas/credenciales.

La aplicación mantiene una única base de código y una única base de datos, pero el backend está organizado como un **monolito modular simple**, separando responsabilidades por dominio: `guard`, `visitor`, `visit`, `storage`, `config` y `errorHandler`.

## Stack técnico

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security Crypto
- PostgreSQL
- Supabase Storage
- Railway
- Gradle

## Objetivo

El backend centraliza la lógica del sistema de control de ingresos para que los datos no dependan del navegador del guardia. Permite registrar visitantes, almacenar sus fotos, generar credenciales, validar QR, consultar visitas activas e históricas y administrar guardias.

## Funcionalidades implementadas

- Registro de visitantes con DNI, nombre completo, empresa, sector y foto.
- Almacenamiento de fotos fuera de la base de datos mediante Supabase Storage.
- Persistencia de visitantes, guardias y visitas en PostgreSQL.
- Login básico de guardias mediante usuario y PIN.
- Hash de PINs usando BCrypt.
- Panel administrativo para crear guardias y resetear PINs.
- Generación de credenciales asociadas a visitas.
- Código QR único por credencial/visita.
- Escaneo de QR para validar ingreso o salida según el estado de la visita.
- Búsqueda de visitante por DNI.
- Consulta de credencial activa por DNI.
- Consulta de visitas del día.
- Consulta de historial completo.
- Exportación del historial de visitas a Excel.
- Manejo centralizado de errores mediante `@RestControllerAdvice`.
- Configuración CORS para frontend local y deployado.
- Uso de DTOs para requests y responses principales.

## Arquitectura

El proyecto está organizado por dominios funcionales:

```txt
backend/
├── src/main/java/com/geno_insights/scolombo/
│   ├── config/
│   │   ├── CorsConfig.java
│   │   ├── PhotoStorage.java
│   │   └── SecurityConfig.java
│   ├── errorHandler/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ApiError.java
│   │   └── exceptions/
│   ├── guard/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── storage/
│   ├── visitor/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── visit/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── ScolomboApplication.java
├── src/main/resources/
│   └── application.yaml
├── build.gradle
└── settings.gradle
```

## Monolito modular

El backend puede considerarse un **monolito modular simple**. No está dividido en microservicios ni en módulos Gradle independientes, pero sí separa el código por dominios y responsabilidades.

Cada dominio concentra sus propios controladores, servicios, modelos, DTOs y repositorios. Esto permite mantener una estructura clara sin agregar complejidad innecesaria para el alcance del challenge.

Los módulos principales son:

- `guard`: autenticación básica de guardias y administración de usuarios guardia.
- `visitor`: registro, búsqueda y conteo de visitantes.
- `visit`: generación de credenciales, control de visitas, QR, historial y exportación.
- `storage`: implementación concreta para almacenamiento de fotos.
- `config`: configuración transversal de CORS, seguridad y abstracciones.
- `errorHandler`: excepciones de dominio y manejo centralizado de errores.

## Decisiones técnicas

### Separación entre visitante y visita

Se separó el concepto de `Visitor` del concepto de `Visit`.

Un visitante representa los datos estables de una persona: DNI, nombre, empresa, sector y foto. Una visita representa un ingreso concreto, con horario de entrada, horario de salida y token QR.

Esta separación permite reutilizar los datos de un visitante cuando vuelve a ingresar, evitando cargar la misma información repetidas veces.

### Uso de DTOs

Se agregaron DTOs para desacoplar la API de las entidades internas de JPA.

Ejemplos:

- `GuardLoginDto`
- `LoginResponse`
- `CreateVisitorDto`
- `VisitorResponse`
- `GenerateCredentialRequest`
- `VisitResponse`

Esto evita exponer directamente detalles internos del modelo y permite controlar mejor la forma de los datos enviados al frontend.

### Manejo centralizado de errores

Se implementó un `GlobalExceptionHandler` con `@RestControllerAdvice` para convertir excepciones de dominio en respuestas HTTP consistentes.

Errores contemplados:

- PIN inválido.
- Guardia inexistente.
- Guardia ya existente.
- Visitante inexistente.
- Visitante ya ingresado.
- Visita no encontrada.
- Visita ya cerrada.
- Credencial activa inexistente.
- Credencial activa duplicada.
- Error al exportar historial.
- Error interno genérico.

Las respuestas de error devuelven un objeto `ApiError` con un mensaje claro para el frontend.

### Almacenamiento de fotos

La foto del visitante no se guarda directamente en la base de datos. Se sube a un bucket de Supabase Storage y en la entidad se almacena la URL resultante.

Además, se definió una interfaz `PhotoStorage`:

```java
public interface PhotoStorage {
    String uploadVisitorPhoto(MultipartFile photo);
}
```

Esto permite desacoplar el dominio de la implementación concreta de almacenamiento. En el futuro, Supabase podría reemplazarse por S3, Cloudinary u otro proveedor sin modificar la lógica principal del sistema.

### Seguridad básica

Se implementó login de guardias con usuario y PIN. Los PINs se almacenan hasheados usando BCrypt mediante un `PasswordEncoder` configurado como bean.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Para el alcance del challenge, esto cumple con el requisito de contar con un mecanismo básico de autenticación. Para producción real, sería recomendable evolucionarlo a sesiones o JWT con roles.

### Reglas de generación de credencial

La generación de credenciales contempla reglas de validación mediante una interfaz de estrategia:

```java
public interface GenerateCredentialRuleStrategy {
    void validate(GenerateCredentialRequest request, Visitor visitor);
}
```

Esto permite agregar nuevas reglas de negocio sin sobrecargar el servicio principal de visitas.

## Configuración CORS

El backend permite requests desde el frontend local y desde las URLs deployadas en Vercel.

Orígenes permitidos actualmente:

```txt
http://localhost:5173
https://geno-insights-challenge.vercel.app
https://geno-insights-challenge-3980xnt41-santino2005s-projects.vercel.app
```

Métodos permitidos:

```txt
GET, POST, PUT, DELETE, OPTIONS
```

## Variables de entorno

El backend utiliza variables de entorno para no subir credenciales sensibles al repositorio.

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
SUPABASE_URL=
SUPABASE_SERVICE_KEY=
SUPABASE_BUCKET=
```

| Variable | Descripción |
|---|---|
| `DB_URL` | URL JDBC de conexión a PostgreSQL. |
| `DB_USERNAME` | Usuario de la base de datos. |
| `DB_PASSWORD` | Contraseña de la base de datos. |
| `SUPABASE_URL` | URL del proyecto de Supabase. |
| `SUPABASE_SERVICE_KEY` | Service key usada por el backend para subir fotos. |
| `SUPABASE_BUCKET` | Nombre del bucket donde se almacenan las fotos. |

## Cómo correr el backend localmente

1. Clonar el repositorio:

```bash
git clone https://github.com/Santino2005/geno-insights-challenge.git
cd geno-insights-challenge/backend
```

2. Configurar las variables de entorno necesarias.

3. Ejecutar la aplicación:

```bash
./gradlew bootRun
```

4. La API queda disponible en:

```txt
http://localhost:8080
```

## Build

Para compilar el proyecto:

```bash
./gradlew build
```

Para compilar sin ejecutar tests:

```bash
./gradlew build -x test
```

## Endpoints

### Guard

#### Login de guardia

```http
POST /guard/login
Content-Type: application/json
```

Body:

```json
{
  "username": "guard1",
  "pin": "1234"
}
```

Respuesta:

```json
{
  "username": "guard1",
  "message": "Login exitoso"
}
```

---

### Admin Guard

#### Crear guardia

```http
POST /admin/guard/create?username=guard1&pin=1234
```

Respuesta:

```txt
Guard creado correctamente
```

#### Resetear PIN

```http
POST /admin/guard/reset-pin?username=guard1&newPin=4321
```

Respuesta:

```txt
PIN actualizado
```

---

### Visitor

#### Buscar visitante por DNI

```http
GET /visitor/{dni}
```

Respuesta:

```json
{
  "dni": "12345678",
  "fullName": "Juan Pérez",
  "company": "Acme",
  "sector": "OPERACIONES",
  "photoUrl": "https://..."
}
```

#### Registrar visitante

```http
POST /visitor
Content-Type: multipart/form-data
```

Campos:

| Campo | Tipo | Descripción |
|---|---|---|
| `dni` | `String` | DNI del visitante. |
| `fullName` | `String` | Nombre completo. |
| `company` | `String` | Empresa del visitante. |
| `sector` | `Sector` | Sector al que ingresa. |
| `photo` | `MultipartFile` | Foto del visitante. Opcional según el endpoint. |

Ejemplo usando `curl`:

```bash
curl -X POST http://localhost:8080/visitor \
  -F "dni=12345678" \
  -F "fullName=Juan Pérez" \
  -F "company=Acme" \
  -F "sector=OPERACIONES" \
  -F "photo=@visitor.jpg"
```

#### Contar visitantes

```http
GET /visitor/count
```

Respuesta:

```json
10
```

---

### Visit

#### Generar credencial

```http
POST /visit
Content-Type: application/json
```

Body:

```json
{
  "dni": "12345678"
}
```

Respuesta:

```json
{
  "id": "...",
  "visitor": {
    "dni": "12345678",
    "fullName": "Juan Pérez",
    "company": "Acme",
    "sector": "OPERACIONES",
    "photoUrl": "https://..."
  },
  "entryTime": "2026-05-15T10:30:00",
  "exitTime": null,
  "qrToken": "..."
}
```

#### Escanear QR

```http
PUT /visit/scan/{qrToken}
```

Este endpoint valida el QR y actualiza el estado de la visita según la lógica del sistema.

#### Obtener credencial por QR

```http
GET /visit/credential/{qrToken}
```

#### Obtener credencial activa por DNI

```http
GET /visit/credential/active/{dni}
```

#### Obtener visitas del día

```http
GET /visit/today
```

#### Obtener historial de visitas

```http
GET /visit/history
```

#### Exportar historial a Excel

```http
GET /visit/history/export
```

Respuesta:

```txt
Archivo: visit-history.xlsx
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
```

## Sectores

Los visitantes se asocian a un sector mediante el enum `Sector`.

Sectores disponibles:

```txt
ADMINISTRACION
OPERACIONES
LOGISTICA
ALMACEN
SEGURIDAD
RECEPCION
MANTENIMIENTO
OTRO
```

## Modelo de dominio

### Visitor

Representa a una persona registrada en el sistema.

Campos principales:

- `id`
- `dni`
- `fullName`
- `company`
- `sector`
- `photoUrl`

### Visit

Representa una visita o credencial generada para un visitante.

Campos principales:

- `id`
- `visitor`
- `entryTime`
- `exitTime`
- `qrToken`

### Guard

Representa a un guardia autorizado a usar el sistema.

Campos principales:

- `id`
- `username`
- `hashedPin`

## Deploy

El backend está preparado para deployarse en Railway.

URL pública del backend:

```txt
[COMPLETAR]
```

Frontend asociado:

```txt
https://geno-insights-challenge.vercel.app
```

## Estado actual

El backend cumple el flujo principal del challenge:

- Los datos ya no viven en `localStorage`.
- Existe persistencia real en base de datos.
- Las fotos se almacenan fuera de la sesión del navegador.
- La app tiene autenticación básica para guardias.
- El frontend puede registrar visitantes, generar credenciales, consultar historial y validar QR.

## Mejoras posibles

Aunque el backend cubre el alcance principal del challenge, hay mejoras razonables para una versión productiva real:

- Implementar JWT o sesiones reales.
- Agregar roles diferenciados para guardia y administrador.
- Proteger endpoints administrativos desde backend, no solo desde frontend.
- Agregar migraciones con Flyway o Liquibase.
- Agregar tests unitarios e integrales.
- Documentar la API con Swagger/OpenAPI.
- Usar URLs firmadas si las fotos no deben ser públicas.
- Agregar auditoría de acciones por guardia.
- Mejorar validaciones de request con Bean Validation.
- Separar reglas de negocio más complejas en casos de uso.

## Preguntas antes de escalar a producción real

- ¿Quién puede crear guardias y resetear PINs?
- ¿El QR debe representar una visita puntual o una credencial permanente del visitante?
- ¿Durante cuánto tiempo deben conservarse las visitas históricas?
- ¿Las fotos deben ser públicas, privadas o temporales?
- ¿Qué nivel de auditoría necesita la empresa?
- ¿Debe haber múltiples plantas o sedes?
- ¿Deben existir roles distintos para guardias, administradores y supervisores?
- ¿Qué ocurre si no hay conexión al momento de registrar un ingreso?
- ¿Hace falta soporte offline o modo contingencia?
- ¿Cuantos permisos mas se pueden generar para un visitador?

## Nota sobre la consigna

La consigna sugería backend en Node o Python, pero se eligió Java con Spring Boot por familiaridad, robustez del ecosistema y rapidez para implementar una API mantenible con persistencia, validaciones, almacenamiento externo y separación por capas.

