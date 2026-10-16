-- ========================================================
-- SISTEMA DE BASE DE DATOS HALCÓN - NEXUS-TEC (ITTOL / TECNM)
-- Script de Creación y Estructura Relacional de Alta Fidelidad
-- ========================================================

-- Tabla: halcon_users (Estudiantes y Comunidad Académica)
CREATE TABLE IF NOT EXISTS halcon_users (
    id VARCHAR(36) PRIMARY KEY,
    institutional_email VARCHAR(120) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    student_id_code VARCHAR(20) NOT NULL UNIQUE,
    career VARCHAR(100) NOT NULL,
    semester INT NOT NULL DEFAULT 1,
    halcon_credits_balance DECIMAL(10,2) NOT NULL DEFAULT 100.00,
    karma_rating DECIMAL(3,2) NOT NULL DEFAULT 5.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: skill_swaps (Marketplace de Habilidades)
CREATE TABLE IF NOT EXISTS skill_swaps (
    swap_id VARCHAR(36) PRIMARY KEY,
    requester_student_id VARCHAR(36) NOT NULL,
    provider_student_id VARCHAR(36),
    offered_skill VARCHAR(200) NOT NULL,
    requested_skill VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    estimated_hours INT DEFAULT 2,
    agreed_halcon_credits DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    scheduled_date TIMESTAMP,
    meeting_location VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_swap_requester FOREIGN KEY (requester_student_id) REFERENCES halcon_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_swap_provider FOREIGN KEY (provider_student_id) REFERENCES halcon_users(id) ON DELETE SET NULL
);

-- Tabla: escrow_transactions (Custodia Segura de Créditos Halcón)
CREATE TABLE IF NOT EXISTS escrow_transactions (
    transaction_id VARCHAR(36) PRIMARY KEY,
    related_swap_id VARCHAR(36) NOT NULL,
    payer_student_id VARCHAR(36) NOT NULL,
    payee_student_id VARCHAR(36) NOT NULL,
    credits_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_DEPOSIT',
    security_hash VARCHAR(64) NOT NULL,
    dispute_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    released_at TIMESTAMP,
    CONSTRAINT fk_escrow_swap FOREIGN KEY (related_swap_id) REFERENCES skill_swaps(swap_id) ON DELETE RESTRICT,
    CONSTRAINT fk_escrow_payer FOREIGN KEY (payer_student_id) REFERENCES halcon_users(id),
    CONSTRAINT fk_escrow_payee FOREIGN KEY (payee_student_id) REFERENCES halcon_users(id)
);

-- Tabla: panic_alerts (Telemetría y Botón de Pánico Campus)
CREATE TABLE IF NOT EXISTS panic_alerts (
    alert_id VARCHAR(36) PRIMARY KEY,
    student_id VARCHAR(36) NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    campus_building_zone VARCHAR(150) NOT NULL,
    alert_level VARCHAR(20) NOT NULL DEFAULT 'HIGH',
    alert_status VARCHAR(30) NOT NULL DEFAULT 'TRIGGERED',
    silent_mode BOOLEAN NOT NULL DEFAULT FALSE,
    emergency_type VARCHAR(50) NOT NULL,
    notes TEXT,
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    CONSTRAINT fk_panic_student FOREIGN KEY (student_id) REFERENCES halcon_users(id) ON DELETE CASCADE
);

-- Tabla: study_groups (Flash Study Groups)
CREATE TABLE IF NOT EXISTS study_groups (
    group_id VARCHAR(36) PRIMARY KEY,
    subject_name VARCHAR(150) NOT NULL,
    topic_description TEXT NOT NULL,
    creator_student_id VARCHAR(36) NOT NULL,
    max_capacity INT NOT NULL DEFAULT 5,
    meeting_location VARCHAR(150) NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 60,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_creator FOREIGN KEY (creator_student_id) REFERENCES halcon_users(id) ON DELETE CASCADE
);

-- Tabla de Asociación: study_group_members
CREATE TABLE IF NOT EXISTS study_group_members (
    group_id VARCHAR(36) NOT NULL,
    student_id VARCHAR(36) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id, student_id),
    CONSTRAINT fk_sgm_group FOREIGN KEY (group_id) REFERENCES study_groups(group_id) ON DELETE CASCADE,
    CONSTRAINT fk_sgm_student FOREIGN KEY (student_id) REFERENCES halcon_users(id) ON DELETE CASCADE
);

-- Índices de Rendimiento
CREATE INDEX idx_swaps_status ON skill_swaps(status);
CREATE INDEX idx_escrow_status ON escrow_transactions(status);
CREATE INDEX idx_panic_status ON panic_alerts(alert_status);
CREATE INDEX idx_study_status ON study_groups(status);
