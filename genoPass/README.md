# 🏢 GenoPass PRO – Smart Access & Visitor Management

> **Enterprise Access Control, Dynamic QR Badges & Audit Trails**  
> 🇪🇸 [Español](#-español) | 🇬🇧 [English](#-english)

---

## 🌐 Deployments
- 🖥️ **Web:** [https://geno-insights-challenge.vercel.app](https://geno-insights-challenge.vercel.app)
- ⚙️ **API:** [https://sparkling-fulfillment-production-89e4.up.railway.app](https://sparkling-fulfillment-production-89e4.up.railway.app)
- 🗄️ **Storage:** [https://ztkizwgdqgqhofafpjde.supabase.co](https://ztkizwgdqgqhofafpjde.supabase.co)

---

## 🇪🇸 Español

### 📌 Problema & Solución
En plantas industriales, el control peatonal dependía de planillas en papel o datos volátiles en `localStorage`. Si el navegador se cerraba o cambiaba el turno, los registros se perdían, creando fallas de seguridad.

**GenoPass PRO** digitaliza el flujo en la nube:
- **Puesto de Guardia:** Registro ágil por DNI, captura de foto en vivo vía webcam y sector de destino.
- **Credenciales QR:** Emisión de gafetes con QR dinámico para reingresos y registro de egresos por escáner web.
- **Auditoría:** Historial centralizado y exportación automática a Excel (.xlsx).

### 🛠️ Stack & Justificación
- **Spring Boot (Java 21):** API REST robusta, validada con **JaCoCo (+80% test coverage)**.
- **React 19 & Tailwind:** UI veloz e intuitiva para pantallas táctiles y puestos de guardia.
- **PostgreSQL & Supabase:** Persistencia cloud confiable de visitas y fotos fuera del navegador.

---

## 🇬🇧 English

### 📌 Problem & Solution
Industrial facilities relied on paper logs or fragile browser storage. Cache resets or shift changes caused total log loss.

**GenoPass PRO** provides a secure cloud access platform:
- **Guard Station:** Instant DNI lookup, live webcam photo capture, and area routing.
- **QR Badges:** Dynamic passes for rapid check-in and camera-based exit check-out.
- **Audit Reports:** Centralized visit history with automated Excel (.xlsx) export.

### 🛠️ Stack & Rationale
- **Spring Boot 21:** Resilient backend architecture with **>80% JaCoCo test coverage**.
- **React 19:** High-performance responsive interface for desktop and kiosks.
- **PostgreSQL & Supabase:** Secure cloud data and photo persistence.
