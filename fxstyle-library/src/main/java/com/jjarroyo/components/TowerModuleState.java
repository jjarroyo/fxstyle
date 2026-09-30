package com.jjarroyo.components;

/**
 * Estados operativos para cada módulo de una torreta industrial (JTowerLight).
 */
public enum TowerModuleState {
    /**
     * Módulo apagado.
     */
    OFF,

    /**
     * Módulo encendido de forma continua.
     */
    ON,

    /**
     * Módulo parpadeando de manera intermitente.
     */
    BLINK
}
