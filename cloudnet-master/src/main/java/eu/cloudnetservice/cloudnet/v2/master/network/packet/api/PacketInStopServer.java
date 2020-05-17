package eu.cloudnetservice.cloudnet.v2.master.network.packet.api;

import eu.cloudnetservice.cloudnet.v2.lib.network.protocol.packet.Packet;
import eu.cloudnetservice.cloudnet.v2.lib.network.protocol.packet.PacketInHandler;
import eu.cloudnetservice.cloudnet.v2.lib.network.protocol.packet.PacketSender;
import eu.cloudnetservice.cloudnet.v2.master.CloudNet;
import eu.cloudnetservice.cloudnet.v2.master.network.components.MinecraftServer;

/**
 * Created by Tareko on 21.08.2017.
 */
public class PacketInStopServer implements PacketInHandler {

    public void handleInput(Packet packet, PacketSender packetSender) {
        String serverId = packet.getData().getString("serverId");
        MinecraftServer minecraftServer = CloudNet.getInstance().getServer(serverId);
        if (minecraftServer != null) {
            minecraftServer.getWrapper().stopServer(minecraftServer);
        }
    }
}