package eu.cloudnetservice.cloudnet.v2.bridge.event.proxied;

import eu.cloudnetservice.cloudnet.v2.lib.player.OfflinePlayer;

/**
 * This event is called whenever an {@link OfflinePlayer} is updated and the data is
 * forwarded to this service.
 */
public class ProxiedOfflinePlayerUpdateEvent extends ProxiedCloudEvent {

    private final OfflinePlayer offlinePlayer;

    public ProxiedOfflinePlayerUpdateEvent(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    /**
     * @return the newly updated player instance.
     */
    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}