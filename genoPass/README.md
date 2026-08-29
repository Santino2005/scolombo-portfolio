# 🏢 GenoPass PRO – Smart Access & Visitor Management

> **Sistema Integral de Control de Accesos Corporativos, Emisión de Credenciales Digitales y Auditoría de Visitas.**

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot 4](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![React 19](https://img.shields.io/badge/React-19-blue?style=flat&logo=react)](https://react.dev/)
[![JaCoCo Coverage](https://img.shields.io/badge/Coverage->80%25-success?style=flat&logo=jacoco)](https://www.jacoco.org/jacoco/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 🌐 Deployments & Acceso en Vivo

- 🖥️ **Frontend (Vercel):** [https://geno-insights-challenge.vercel.app](https://geno-insights-challenge.vercel.app)
- ⚙️ **Backend API (Railway):** [https://sparkling-fulfillment-production-89e4.up.railway.app](https://sparkling-fulfillment-production-89e4.up.railway.app)
- 🗄️ **Cloud Storage (Supabase):** [https://ztkizwgdqgqhofafpjde.supabase.co](https://ztkizwgdqgqhofafpjde.supabase.co)

---

## 🎯 Sobre el Proyecto

**GenoPass PRO** es una solución full-stack concebida para modernizar la gestión de accesos y seguridad en plantas industriales y edificios corporativos. Convierte los registros manuales en papel o prototipos locales inseguros en una plataforma cloud robusta, escalable y en tiempo real.

Permite al personal de seguridad y a los visitantes registrar ingresos, capturar fotografías mediante la cámara web, generar credenciales con códigos QR dinámicos, escanear salidas instantáneamente y exportar auditorías completas en Excel.

---

## ✨ Características Principales

### 🛡️ Panel de Guardia & Command Center
- **Dashboard en Tiempo Real:** Métricas instantáneas de ingresos del día, visitantes activos dentro del edificio y total de personas registradas.
- **Búsqueda Inteligente por DNI:** Autocompletado de datos para visitantes frecuentes.
- **Captura Fotográfica en Vivo:** Widget de cámara web integrado con vista previa, encuadre de crosshair y confirmación de captura.
- **Asignación por Sectores:** Clasificación dinámica por áreas corporativas (*Operaciones, Administración, Logística, Almacén, Seguridad, Recepción, Mantenimiento*).

### 🎟️ Portal de Autogestión para Visitantes
- Kiosco digital para que el visitante emita su pase de acceso ingresando su DNI y sector de destino.
- Consulta y recuperación inmediata de credenciales activas.

### 🪪 Credencial Digital & Impresión Física
- Pase de acceso diseñado como gafete físico profesional con slot para cordón (lanyard), foto validada, sello de verificación, datos del titular y código QR de alta resolución.
- Soporte nativo para impresión y guardado como PDF optimizado vía `@media print`.

### 📷 Escáner de Salidas (QR Scanner)
- Lector de códigos QR vía cámara en tiempo real mediante `html5-qrcode`.
- Validación instantánea contra el backend y registro automático de la hora de egreso.

### 📊 Reportes & Auditoría (Exportación Excel)
- Generación y descarga de archivos `.xlsx` formateados con fecha, hora de ingreso/salida, datos del visitante, empresa, sector y estado de la visita mediante **Apache POI**.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend (React 19)                    │
│      Vite · React Router 7 · QRCode.react · Html5-QRCode    │
└──────────────────────────────┬──────────────────────────────┘
                               │  REST API (JSON / Multipart)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Backend (Spring Boot / Java 21)           │
│   Controllers ──► Services ──► Repositories ──► Spring JPA  │
│   BCrypt Auth · Apache POI Excel Export · Global Exceptions │
└──────────────────────┬──────────────────────────────┬───────┘
                       │                              │
                       ▼                              ▼
        ┌────────────────────────────┐  ┌───────────────────────────┐
        │    PostgreSQL Database     │  │   Supabase Cloud Storage  │
        │  (Guard, Visitor, Visits)  │  │   (Fotografías Visitantes)│
        └────────────────────────────┘  └───────────────────────────┘
```

---

## 🧪 Cobertura de Tests con JaCoCo (+80%)

El backend cuenta con una suite integral de pruebas unitarias y de integración que superan el **80% de cobertura de código requerida** validada automáticamente en el pipeline de build con el plugin de JaCoCo.

Para ejecutar los tests y generar el reporte:

```bash
cd backend
./gradlew test jacocoTestReport jacocoTestCoverageVerification
```

El reporte interactivo HTML se genera en:  
`backend/build/reports/jacoco/test/html/index.html`

---

## 🚀 Guía de Instalación y Ejecución Local

### 1️⃣ Prerrequisitos
- **Java 21** o superior instalado (`java -version`).
- **Node.js 18+** y **npm** (`node -v`, `npm -v`).
- **PostgreSQL** local o una instancia cloud (e.g. Supabase, Railway).

---

### 2️⃣ Configurar y Levantar el Backend

1. Ingresar a la carpeta de backend:
   ```bash
   cd backend
   ```

2. Configurar variables de entorno (crear un archivo `.env` o exportarlas en tu terminal):
   ```properties
   DB_URL=jdbc:postgresql://db.ztkizwgdqgqhofafpjde.supabase.co:5432/postgres?sslmode=require
   DB_USERNAME=postgres
   DB_PASSWORD=F4wZMz7cFiErTjje
   PORT=8080
   SUPABASE_URL=https://ztkizwgdqgqhofafpjde.supabase.co
   SUPABASE_SERVICE_KEY=your-supabase-service-key
   SUPABASE_BUCKET=visitors
   ```

3. Compilar y ejecutar:
   ```bash
   ./gradlew bootRun
   ```

   El backend iniciará en `http://localhost:8080`.

---

### 3️⃣ Configurar y Levantar el Frontend

1. Ingresar a la carpeta de frontend:
   ```bash
   cd frontend
   ```

2. Instalar dependencias:
   ```bash
   npm install
   ```

3. (Opcional) Configurar la URL del backend en `.env`:
   ```properties
   VITE_API_URL=http://localhost:8080
   ```

4. Iniciar el servidor de desarrollo:
   ```bash
   npm run dev
   ```

   El frontend abrirá en `http://localhost:5173`.

---

## 📡 Referencia de Endpoints API

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/guard/login` | Autenticación de guardia mediante usuario y PIN |
| `GET` | `/visitor/{dni}` | Consulta de datos de un visitante registrado por DNI |
| `POST` | `/visitor` | Alta de nuevo visitante (Multipart: DNI, nombre, empresa, sector y foto) |
| `GET` | `/visitor/count` | Total de visitantes registrados en base de datos |
| `POST` | `/visit` | Registro de ingreso y emisión de credencial con QR |
| `PUT` | `/visit/exit/{qrToken}` | Registro de egreso / cierre de visita mediante token QR |
| `GET` | `/visit/credential/{qrToken}` | Consulta de credencial por token |
| `GET` | `/visit/credential/active/{dni}` | Consulta de credencial activa por DNI |
| `GET` | `/visit/today` | Listado de visitas del día |
| `GET` | `/visit/history` | Historial completo de visitas |
| `GET` | `/visit/history/export` | Descarga de reporte de auditoría en formato Excel `.xlsx` |

---

## 👨‍💻 Autor

**Santino Colombo**  
*Full-Stack Developer | Estudiante de Ingeniería en Informática (4to Año)*  
[GitHub Profile](https://github.com/Santino2005) · [Portfolio](https://scolombo-portfolio3.vercel.app/)
