package org.cyclops.commoncapabilities.api.capability.temperature;

/**
 * Indicates if something has a temperature.
 * @author rubensworks
 */
public interface ITemperature {

    /**
     * Zero degrees Celcius in Kelvin.
     */
    public static double ZERO_CELCIUS = 273.15;

    /**
     * @return The current temperature in degrees Kelvin.
     */
    public double getTemperature();

    /**
     * @return The maximum temperature the target can have.
     */
    public double getMaximumTemperature();

    /**
     * @return The minimum temperature the target can have.
     */
    public double getMinimumTemperature();

    /**
     * A default temperature is active in the target's default state,
     * for example if a machine has just been created and has its default temperature.
     * @return The default temperature the target has.
     */
    public double getDefaultTemperature();

}
