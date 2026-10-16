# Arquitectura de Microservicios Backend - Nexus-Tec (ITTOL)

Infraestructura de microservicios modulares desacoplados para la plataforma institucional **Nexus-Tec** del Instituto Tecnológico de Toluca (TecNM).

---

## 🏗️ Matriz de Microservicios

| Microservicio | Puerto / Ruta Base | Responsabilidad Principal | Patrón Arquitectónico |
| :--- | :--- | :--- | :--- |
| **Skill-Swap** | `:8081 /api/v1/skill-swap` | Marketplace de intercambio de habilidades académicas | REST CQRS + Repository |
| **Escrow (Créditos Halcón)** | `:8082 /api/v1/escrow` | Monedero estudiantil y custodia segura de créditos | State Pattern + Cryptographic Ledger |
| **Panic-Button** | `:8083 /api/v1/panic` | Telemetría GPS de emergencia y despacho a casetas de seguridad | Observer Pattern + Realtime WebSockets |
| **Flash Study Groups** | `:8084 /api/v1/study-groups`| Localización de salas y mesas de estudio espontáneas | Event-Driven + Capacity Gatekeeper |

---

## 🔒 Seguridad y Autenticación
- **Tokens Institucionales:** Autenticación de estudiantes mediante correo institucional `@toluca.tecnm.mx`.
- **Integridad Escrow:** Generación de firma hash SHA-256 para cada transacción bajo custodia antes de la liberación o mediación.
- **Canal de Emergencia:** Prioridad absoluta QoS en el microservicio de Botón de Pánico con redundancia de observadores en tiempo real.
