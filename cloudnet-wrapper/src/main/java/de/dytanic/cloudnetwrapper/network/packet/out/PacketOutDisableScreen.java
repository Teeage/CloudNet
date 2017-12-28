/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnetwrapper.network.packet.out;

import de.dytanic.cloudnet.lib.network.protocol.packet.Packet;
import de.dytanic.cloudnet.lib.network.protocol.packet.PacketRC;
import de.dytanic.cloudnet.lib.service.ServiceId;
import de.dytanic.cloudnet.lib.utility.document.Document;

/**
 * Created by Tareko on 25.08.2017.
 */
public class PacketOutDisableScreen extends Packet {

    public PacketOutDisableScreen(ServiceId serviceId)
    {
        super(PacketRC.CN_WRAPPER + 10, new Document("serviceId", serviceId));
    }
}