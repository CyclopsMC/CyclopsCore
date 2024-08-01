package org.cyclops.cyclopscore.blockentity;

/**
 * Block entities must implement this interface when using {@link BlockEntityTickerDelayed}.
 * This is to enable the required state to be kept during tick delays.
 * @author rubensworks
 */
public interface IBlockEntityDelayedTickable {

    /**
     * @return The minimum amount of ticks between two consecutive sent packets.
     */
    public int getUpdateBackoffTicks();

    /**
     * Send a world update for the coordinates of this block entity.
     * This will always send lag-safe updates, so calling this many times per tick will
     * not cause multiple packets to be sent, more info in the class javadoc.
     */
    public void sendUpdate();

    /**
     * Send an immediate world update for the coordinates of this block entity.
     * This does the same as {@link IBlockEntityDelayedTickable#sendUpdate()} but will
     * ignore the update backoff.
     */
    public default void sendImmediateUpdate() {
        sendUpdate();
        setUpdateBackoff(0);
    }

    /**
     * @return If an update should be sent.
     */
    public boolean shouldSendUpdate();

    /**
     * Reset the send update flag.
     */
    public void unsetSendUpdate();

    /**
     * Set the update backoff delay.
     * @param updateBackoff The new delay in ticks.
     */
    public void setUpdateBackoff(int updateBackoff);

    /**
     * @return The current update backoff in ticks.
     */
    public int getUpdateBackoff();

    /**
     * Reduce the update backoff tick value by one.
     */
    public default void reduceUpdateBackoff() {
        setUpdateBackoff(getUpdateBackoff() - 1);
    }
}
