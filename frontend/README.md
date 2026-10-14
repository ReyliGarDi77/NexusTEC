# Arquitectura Móvil Nexus-Tec (Android Java)

Arquitectura cliente para la aplicación móvil de **Nexus-Tec**, diseñada bajo los principios de **Clean Architecture** y el patrón de presentación **MVVM (Model-View-ViewModel)** orientado a objetos en Java.

---

## 🏛️ Estructura de Paquetes

```
frontend/
├── AndroidManifest.xml
├── build.gradle
├── ui/                     # Capa de Presentación (Vistas Android)
│   ├── base/               # BaseActivity, BaseFragment, BaseAdapter
│   ├── MainActivity.java   # Dashboard principal con navegación modular
│   ├── skillswap/          # SkillSwapActivity, Fragment y Adapter
│   ├── escrow/             # EscrowActivity, Fragment y Adapter
│   ├── panicbutton/        # PanicButtonActivity y Fragment
│   └── studygroups/        # StudyGroupsActivity, Fragment y Adapter
├── viewmodel/              # Capa de Lógica de Presentación y Estado
│   ├── BaseViewModel.java
│   ├── ObservableValue.java# Abstracción reactiva estilo LiveData / StateFlow
│   ├── SkillSwapViewModel.java
│   ├── EscrowViewModel.java
│   ├── PanicButtonViewModel.java
│   └── StudyGroupsViewModel.java
├── domain/                 # Capa de Negocio y Reglas de Dominio
│   ├── state/              # PATRÓN STATE: Ciclo de vida de Créditos Halcón en Escrow
│   │   ├── EscrowState.java
│   │   ├── PendingDepositState.java
│   │   ├── InEscrowHoldState.java
│   │   ├── CompletedReleasedState.java
│   │   ├── DisputedState.java
│   │   ├── RefundedState.java
│   │   └── EscrowContext.java
│   ├── observer/           # PATRÓN OBSERVER: Alertas de Emergencia (Botón de Pánico)
│   │   ├── PanicAlertObserver.java
│   │   ├── PanicAlertSubject.java
│   │   ├── PanicAlertManager.java
│   │   ├── CampusSecurityStationObserver.java
│   │   ├── EmergencyNotificationCenterObserver.java
│   │   └── CampusHealthServiceObserver.java
│   └── usecase/            # Casos de uso de la aplicación
│       ├── CreateSkillSwapUseCase.java
│       ├── ProcessEscrowPaymentUseCase.java
│       ├── TriggerPanicAlertUseCase.java
│       └── JoinStudyGroupUseCase.java
└── data/                   # Capa de Datos y Red
    ├── model/              # POJOs y Entidades del Sistema
    │   ├── HalconUser.java
    │   ├── SkillSwap.java
    │   ├── EscrowTransaction.java
    │   ├── PanicAlert.java
    │   └── StudyGroup.java
    ├── remote/             # Clientes Retrofit y contratos de API
    │   ├── ApiResponse.java
    │   ├── NexusApiService.java
    │   └── RetrofitClient.java
    └── repository/         # Patrón Repository
        ├── SkillSwapRepository.java
        ├── EscrowRepository.java
        ├── PanicAlertRepository.java
        └── StudyGroupRepository.java
```

---

## 🎨 Patrones de Diseño Implementados

### 1. Patrón State (Ciclo de Vida de Créditos en Escrow)
- **Propósito:** Controlar rigurosamente el estado de los fondos en custodia durante un intercambio de habilidades.
- **Participantes:**
  - `EscrowState` (Interfaz base).
  - `PendingDepositState` (Pendiente de depósito inicial).
  - `InEscrowHoldState` (Fondos asegurados y congelados en custodia).
  - `CompletedReleasedState` (Servicio finalizado con éxito, fondos acreditados al prestador).
  - `DisputedState` (Transacción congelada para mediación y arbitraje institucional).
  - `RefundedState` (Fondos devueltos al estudiante emisor).
  - `EscrowContext` (Contexto que administra transiciones y auditoría).

### 2. Patrón Observer (Emisión y Escucha de Alertas de Pánico)
- **Propósito:** Distribuir eventos críticos de seguridad en tiempo real a múltiples dependencias de campus en cuanto un estudiante presiona el Botón de Pánico.
- **Participantes:**
  - `PanicAlertSubject` / `PanicAlertManager` (Sujeto emisor y despachador concurrente).
  - `PanicAlertObserver` (Interfaz de escucha).
  - `CampusSecurityStationObserver` (Caseta de seguridad, despacho de patrullas y rastreo GPS).
  - `EmergencyNotificationCenterObserver` (Emisión de notificaciones push y SMS a contactos).
  - `CampusHealthServiceObserver` (Activación de ambulancia y paramédicos para emergencias médicas).

---

## 🚀 Compilación y Verificación

El proyecto está diseñado bajo especificaciones de **Java 17+** y el SDK de **Android 34**.
Para compilar las clases del proyecto en modo verificación:

```bash
cd frontend
javac -d ../bin $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```
