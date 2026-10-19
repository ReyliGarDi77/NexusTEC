# 🦅 Nexus-Tec ITTOL (TecNM)

> **Plataforma Inteligente de Vinculación Estudiantil, Marketplace Académico y Seguridad Universitaria**  
> Desarrollada para la comunidad de los **Halcones del Instituto Tecnológico de Toluca (TecNM)**.

[![TecNM](https://img.shields.io/badge/TecNM-Toluca-1B396A?style=for-the-badge&logo=android)](https://toluca.tecnm.mx)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-D4AF37?style=for-the-badge)](docs/ARCHITECTURE.md)
[![Design System](https://img.shields.io/badge/UI%2FUX-Apple%20HIG%20%2B%20Glassmorphism-00F0FF?style=for-the-badge)](docs/UI_UX_DESIGN_SYSTEM.md)
[![Build Status](https://img.shields.io/badge/Build-Passing-10B981?style=for-the-badge)]()

---

## 🏛️ 1. Arquitectura del Sistema

Nexus-Tec implementa una arquitectura desacoplada orientada al dominio (**Clean Architecture + MVVM** en Android/Java) con integración directa a microservicios en `/backend`:

```
NexusTEC/
├── backend/                       # Microservicios modulares desacoplados
│   ├── escrow/                    # EscrowLedgerService (Hashes SHA-256)
│   ├── flash-study-groups/        # ClassroomLocatorService (Aforo y Aulas)
│   ├── panic-button/              # CampusDispatchService (Telemetría y Caseta)
│   └── skill-swap/                # SkillSwapService (Marketplace)
├── frontend/                      # Cliente Móvil Android (Java 17 / 21)
│   ├── data/                      # Modelos, Remote API y Repositorios
│   ├── domain/                    # Casos de Uso, Patrón State y Patrón Observer
│   ├── res/                       # Sistema de Diseño TecNM & Apple HIG (XML)
│   ├── ui/                        # Activities, Fragments, Adapters y Navegación
│   ├── viewmodel/                 # MVVM con ObservableValue reactivo
│   └── test/                      # Suite Integral de Pruebas de Arquitectura
├── database/                      # Esquemas SQL Relacionales (halcon_schema.sql)
└── docs/                          # Especificaciones de Arquitectura y UI/UX
```

---

## 🚀 2. Módulos Principales

### 🔄 1. Skill-Swap (Marketplace de Conocimiento)
- Catálogo de habilidades e intercambio de asesorías entre estudiantes.
- Búsqueda en tiempo real por palabras clave y filtrado dinámico por carreras.
- Vinculación directa con acuerdos de **Créditos Halcón (CH)**.

### 🪙 2. Escrow (Créditos Halcón & Custodia Criptográfica)
- Implementación estricta del **Patrón State** (`PendingDepositState`, `InEscrowHoldState`, `CompletedReleasedState`, `DisputedState`, `RefundedState`).
- Retención segura de fondos con generación de recibos criptográficos en **SHA-256**.
- Débito y acreditación automática entre las billeteras virtuales de los estudiantes.

### 🚨 3. Botón de Pánico (Seguridad y Emergencias Campus)
- Emisión inmediata de alertas con telemetría GPS continua en el campus ITTOL.
- Implementación del **Patrón Observer** (`PanicAlertSubject` / `PanicAlertManager`) con notificación simultánea a:
  - **Caseta de Seguridad Universitaria #01-Principal** (Despacho de patrullas).
  - **Centro de Notificaciones Institucionales** (Difusión Push / SMS).
  - **Servicio Médico y Primeros Auxilios** (Protocolo de ambulancias).
- Modo silencioso/discreto y confirmación de cancelación de falsa alarma.

### 📚 4. Flash Study Groups (Mesas de Estudio Espontáneas)
- Localización de aulas, laboratorios y cubículos (Edificio D, Edificio F, LC1, Biblioteca).
- Control de aforo en tiempo real con bloqueo automático al alcanzar la capacidad máxima.
- Uniones instantáneas con confirmación y retroalimentación háptica.

---

## 🎨 3. Sistema de Diseño (Apple HIG & TecNM)

El sistema visual conjuga la identidad de los **Halcones del TecNM** con las directrices de **Apple Human Interface Guidelines (HIG)**:
- **Paleta Cromática:** Azul Marino TecNM (`#1B396A`), Oro Halcón (`#D4AF37`), Carmesí Emergencia (`#EF4444`), Esmeralda Custodia (`#10B981`) y Fondo OLED Obsidiana (`#080C14`).
- **Glassmorphism:** Tarjetas translúcidas con bordes especulares de `1.2dp` y elevación dinámica.
- **Haptic Feedback:** 5 niveles táctiles (`LIGHT_IMPACT`, `MEDIUM_IMPACT`, `HEAVY_EMERGENCY`, `SUCCESS_NOTIFICATION`, `WARNING_ALERT`).
- **Microinteracciones:** Radar pulsante concéntrico (`pulse_radar.xml`), botones con relieve táctil y transiciones elásticas.

---

## 🧪 4. Ejecución y Pruebas

Para compilar y ejecutar la suite integral de pruebas de arquitectura y la simulación interactiva:

```powershell
# Compilar todo el ecosistema (Frontend + Backend + Test Suite)
javac -d bin (Get-ChildItem -Path backend, frontend -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })

# Ejecutar la suite automatizada de pruebas de arquitectura
java -cp "bin;frontend;backend" test.NexusTecArchitectureTestSuite

# Ejecutar el flujo interactivo de la aplicación móvil
java -cp "bin;frontend;backend" ui.MainActivity
```
