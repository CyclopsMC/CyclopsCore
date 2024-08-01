package org.cyclops.commoncapabilities.api.capability.temperature;

/**
 * A default temperature implementation that has a fixed temperature that can't be changed.
 * @author rubensworks
 */
public class DefaultTemperature implements ITemperature {

    private final double temperature;

    public DefaultTemperature() {
        this(ITemperature.ZERO_CELCIUS);
    }

    public DefaultTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getMaximumTemperature() {
        return 0;
    }

    @Override
    public double getMinimumTemperature() {
        return 0;
    }

    @Override
    public double getDefaultTemperature() {
        return 0;
    }
}
