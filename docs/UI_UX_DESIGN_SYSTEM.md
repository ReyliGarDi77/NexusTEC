# Sistema de Diseño UI/UX - Nexus-Tec Halcones ITTOL

Guía de estilo y lenguaje de diseño inmersivo para la plataforma móvil **Nexus-Tec**, combinando la herencia institucional del **TecNM** con los estándares más refinados de **Apple Human Interface Guidelines (HIG)** y **Material You**.

---

## 🎨 1. Identidad Cromática Institucional & Alta Gama

| Token de Color | Valor Hex | Uso e Impacto Semántico |
| :--- | :--- | :--- |
| `tecnm_navy_primary` | `#1B396A` | **Azul Marino TecNM:** Autoridad académica, estabilidad y confianza institucional. |
| `tecnm_gold_accent` | `#D4AF37` | **Oro Halcón:** Acabado de joyería metálica para saldos de Créditos Halcón y acciones primarias. |
| `canvas_dark_obsidian` | `#080C14` | **Lienzo Negro Obsidiana:** Fondo OLED puro que maximiza el contraste y ahorra energía de batería. |
| `glass_surface_base` | `#CC131D31` | **Superficie Glassmorphic:** Tarjetas flotantes con 85% de opacidad y difuminado translúcido. |
| `halcon_cyan_tech` | `#00F0FF` | **Cian Tecnológico:** Acento de modernidad para chips activos, categorías y telemetría. |
| `panic_crimson` | `#EF4444` | **Carmesí Emergencia:** Alerta visual inequívoca, botón de pánico y anillos de radar pulsante. |
| `escrow_emerald` | `#10B981` | **Esmeralda Custodia:** Fondos asegurados, liberaciones exitosas y estados verificados. |

---

## 🪟 2. Especificación Glassmorphism (Efecto Vidrio Esmerilado)

- **Radio de Curvatura (Superelipses):** `20dp` a `24dp` en tarjetas flotantes; `100dp` en píldoras y botones de acción.
- **Borde de Alta Precisión:** Trazo sutil de `1.2dp` con color `#4D3B82F6` (resaltado especular que simula reflexión física de luz).
- **Elevación y Profundidad:**
  - Reposo: `elevation="4dp"`
  - Enfoque / Hover: `elevation="8dp"`
  - Modales y Emergencias: `elevation="16dp"`

---

## 📳 3. Microinteracciones Táctiles y Animaciones

### A. Pulsación Radar de Emergencia (Botón de Pánico)
- **Concepto:** Anillos concéntricos expansivos con interpolación desacelerada (`decelerate_interpolator`).
- **Escala:** De `1.0x` a `1.8x` con desvanecimiento alfa de `0.8` a `0.0` durante `1200ms` en bucle continuo.
- **Haptic Engine:** Impacto háptico pesado (`HEAVY_EMERGENCY`) inmediato al contacto del usuario.

### B. Transiciones de Estado en Escrow
- **Concepto:** Animación de balance elástico (`overshoot_interpolator`) en la tarjeta dorada de créditos.
- **Feedback:** Pulso de éxito (`SUCCESS_NOTIFICATION`) con destello de borde esmeralda al liberar fondos al prestador.

### C. Navegación Fluida
- **Slide & Fade:** Desplazamiento vertical suave (`slide_in_up.xml` / `slide_out_down.xml`) con duración de `280ms` emulando las hojas modulares de iOS.

