# Arquitectura de Software Nexus-Tec - ITTOL (TecNM)

Documento de especificación arquitectónica para el ecosistema móvil y modular de **Nexus-Tec**, desarrollado para la comunidad estudiantil de los **Halcones del Instituto Tecnológico de Toluca**.

---

## 🏛️ 1. Diagrama de Capas (Clean Architecture + MVVM)

```mermaid
graph TD
    subgraph UI_Layer ["Capa de Presentación (Android UI / XML)"]
        Activity["MainActivity"]
        Frag1["SkillSwapFragment"]
        Frag2["EscrowFragment"]
        Frag3["PanicButtonFragment"]
        Frag4["StudyGroupsFragment"]
    end

    subgraph ViewModel_Layer ["Capa de Presentación Reactiva (MVVM)"]
        VM1["SkillSwapViewModel"]
        VM2["EscrowViewModel"]
        VM3["PanicButtonViewModel"]
        VM4["StudyGroupsViewModel"]
        Observable["ObservableValue (LiveData Pattern)"]
    end

    subgraph Domain_Layer ["Capa de Negocio y Dominio"]
        StatePattern["Patrón State (EscrowContext / EscrowState)"]
        ObserverPattern["Patrón Observer (PanicAlertManager / Observers)"]
        UseCases["Casos de Uso (CreateSwap, ProcessEscrow, TriggerAlert, JoinGroup)"]
    end

    subgraph Data_Layer ["Capa de Datos y Red"]
        Repos["Repositorios (SkillSwap, Escrow, Panic, StudyGroup)"]
        Retrofit["RetrofitClient & NexusApiService"]
        MockService["MockNexusApiService (ITTOL Dataset)"]
    end

    subgraph Backend_Microservices ["Backend Modular"]
        MS1[":8081 Skill-Swap Service"]
        MS2[":8082 Escrow Ledger Service"]
        MS3[":8083 Campus Dispatch Service"]
        MS4[":8084 Classroom Locator Service"]
    end

    Activity --> Frag1 & Frag2 & Frag3 & Frag4
    Frag1 --> VM1
    Frag2 --> VM2
    Frag3 --> VM3
    Frag4 --> VM4
    VM1 & VM2 & VM3 & VM4 --> Observable
    VM1 & VM2 & VM3 & VM4 --> UseCases
    UseCases --> StatePattern & ObserverPattern & Repos
    Repos --> Retrofit
    Retrofit --> MockService
    Retrofit -.-> MS1 & MS2 & MS3 & MS4
```

---

## 🔄 2. Diagrama de Secuencia: Ciclo de Vida Escrow (Patrón State)

```mermaid
sequenceDiagram
    autonumber
    actor EstudiantePayer as Estudiante Solicitante
    participant EscrowVM as EscrowViewModel
    participant UseCase as ProcessEscrowPaymentUseCase
    participant StateContext as EscrowContext (State Machine)
    participant Repo as EscrowRepository
    actor EstudiantePayee as Estudiante Prestador

    EstudiantePayer->>EscrowVM: Inicia contratación de asesoría (Ej. 25 CH)
    EscrowVM->>UseCase: initiateEscrow(swapId, payerId, payeeId, 25.0)
    UseCase->>StateContext: new EscrowContext() -> PendingDepositState
    UseCase->>StateContext: deposit(25.0) -> Transición a InEscrowHoldState
    StateContext-->>StateContext: Bloquea fondos y genera Hash SHA-256
    UseCase->>Repo: createEscrowHold()
    Repo-->>EscrowVM: Transacción en custodia confirmada
    Note over StateContext,EstudiantePayee: Se realiza la asesoría presencial en campus ITTOL
    EstudiantePayer->>EscrowVM: Confirma satisfacción y pulsa "Liberar Fondos"
    EscrowVM->>UseCase: confirmAndRelease(secretCode)
    UseCase->>StateContext: releaseCredits() -> Transición a CompletedReleasedState
    StateContext-->>Repo: Acredita 25 CH en billetera del prestador
    EscrowVM-->>EstudiantePayer: Saldo actualizado y feedback háptico exitoso
```

---

## 🚨 3. Diagrama de Secuencia: Botón de Pánico (Patrón Observer)

```mermaid
sequenceDiagram
    autonumber
    actor Estudiante as Estudiante en Riesgo
    participant UI as PanicButtonFragment
    participant VM as PanicButtonViewModel
    participant Manager as PanicAlertManager (Subject)
    participant Caseta as CampusSecurityStationObserver
    participant Prefectura as EmergencyNotificationCenterObserver
    participant Medicos as CampusHealthServiceObserver

    Estudiante->>UI: Tap en Botón Central SOS (Pánico)
    UI->>UI: Dispara retroalimentación háptica intensa y pulso radar
    UI->>VM: triggerEmergency(coordenadas GPS, nivel CRITICAL)
    VM->>Manager: notifyAlertTriggered(PanicAlert)
    par Despacho Paralelo a Observadores
        Manager->>Caseta: onPanicAlertReceived() -> Despacha patrulla a Edificio D
        Manager->>Prefectura: onPanicAlertReceived() -> Notificación Push & SMS de urgencia
        Manager->>Medicos: onPanicAlertReceived() -> Unidad médica en alerta
    end
    VM-->>UI: Estado de alarma confirmado y telemetría en vivo
```

