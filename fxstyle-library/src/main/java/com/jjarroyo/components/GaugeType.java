package com.jjarroyo.components;

/**
 * Estilos y tipos de medidores IoT y dashboard soportados por JGauge.
 */
public enum GaugeType {
    /**
     * Tacómetro clásico circular de 270 grados (-135 a +135 deg).
     * Ideal para RPM, velocidad de eje, velocímetros de motor.
     */
    RADIAL_CLASSIC,

    /**
     * Medidor semicircular de 180 grados (-90 a +90 deg) con zonas continuas.
     * Ideal para temperatura, sensores térmicos en cámaras o motores.
     */
    SEMI_CIRCLE,

    /**
     * Medidor moderno con arco de progreso grueso y puntero exterior.
     * Ideal para porcentaje de batería (SOC), humedad relativa, nivel de llenado.
     */
    ARC_PROGRESS,

    /**
     * Medidor lineal horizontal con regla graduada, franja de zonas y cursor deslizante.
     * Ideal para voltajes de batería (12V, 24V, 48V), señales RSSI.
     */
    LINEAR,

    /**
     * Amperímetro con cero central (-30A a +30A) o escala de corriente continua.
     * Ideal para flujo de corriente en inversores, paneles solares y carga/descarga de baterías.
     */
    AMPERAGE,

    /**
     * Anemómetro / Medidor de viento (0 a 120 km/h o m/s).
     * Ideal para estaciones meteorológicas IoT y monitoreo eólico.
     */
    WIND_SPEED,

    /**
     * Manómetro industrial de presión (Bar / PSI / kPa).
     * Ideal para líneas de aire comprimido, presión hidráulica o vapor.
     */
    PRESSURE,

    /**
     * Medidor vertical de nivel de fluido o tanque (0 a 100%).
     * Ideal para tanques de combustible, silos, depósitos de agua y cisternas IoT.
     */
    LEVEL_VERTICAL
}
