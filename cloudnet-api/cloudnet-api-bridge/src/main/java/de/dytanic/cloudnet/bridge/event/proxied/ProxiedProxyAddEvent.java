/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnet.bridge.event.proxied;

import de.dytanic.cloudnet.lib.server.info.ProxyInfo;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.plugin.Event;

/**
 * Calls if a proxy server was add into the network
 */
@AllArgsConstructor
public class ProxiedProxyAddEvent extends ProxiedCloudEvent {

    private ProxyInfo proxyInfo;

    public ProxyInfo getProxyInfo()
    {
        return proxyInfo;
    }
}