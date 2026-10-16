# Modelo de Datos Halcón - Nexus-Tec (ITTOL)

Diagrama Entidad-Relación y especificaciones de la base de datos relacional para el ecosistema Nexus-Tec en el Instituto Tecnológico de Toluca (TecNM).

---

## 📊 Diagrama Entidad-Relación (ERD)

```mermaid
erDiagram
    HALCON_USERS ||--o{ SKILL_SWAPS : solicita
    HALCON_USERS ||--o{ ESCROW_TRANSACTIONS : paga
    HALCON_USERS ||--o{ PANIC_ALERTS : emite
    HALCON_USERS ||--o{ STUDY_GROUPS : coordina
    STUDY_GROUPS ||--|{ STUDY_GROUP_MEMBERS : incluye
    HALCON_USERS ||--|{ STUDY_GROUP_MEMBERS : participa
    SKILL_SWAPS ||--|| ESCROW_TRANSACTIONS : respalda

    HALCON_USERS {
        string id PK
        string institutional_email
        string full_name
        string student_id_code
        string career
        decimal halcon_credits_balance
        decimal karma_rating
    }

    SKILL_SWAPS {
        string swap_id PK
        string requester_student_id FK
        string provider_student_id FK
        string offered_skill
        string requested_skill
        decimal agreed_halcon_credits
        string status
    }

    ESCROW_TRANSACTIONS {
        string transaction_id PK
        string related_swap_id FK
        string payer_student_id FK
        string payee_student_id FK
        decimal credits_amount
        string status
        string security_hash
    }

    PANIC_ALERTS {
        string alert_id PK
        string student_id FK
        decimal latitude
        decimal longitude
        string campus_building_zone
        string alert_level
        string alert_status
        boolean silent_mode
    }

    STUDY_GROUPS {
        string group_id PK
        string subject_name
        string topic_description
        string creator_student_id FK
        int max_capacity
        string meeting_location
        string status
    }
```
