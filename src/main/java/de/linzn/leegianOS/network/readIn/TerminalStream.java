/*
 * Copyright (C) 2018. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 */

package de.linzn.leegianOS.network.readIn;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.leegianOS.LeegianOSApp;
import de.linzn.leegianOS.internal.objectDatabase.clients.SkillClient;
import de.linzn.leegianOS.internal.processor.MainProcessor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class TerminalStream implements IncomingDataListener {

    private LeegianOSApp leegianOSApp;

    public TerminalStream(LeegianOSApp leegianOSApp) {
        this.leegianOSApp = leegianOSApp;
    }

    @Override
    public void onEvent(String channel, UUID uuid, byte[] bytes) {
        // TODO Auto-generated method stub
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        String values = null;
        try {
            values = in.readUTF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String finalValues = values;
        Runnable runnable = () -> {
            SkillClient skillClient = leegianOSApp.skillClientList.get(uuid);
            MainProcessor mainProcessor = new MainProcessor(skillClient, finalValues);
            mainProcessor.processing();
        };
        this.leegianOSApp.heartbeat.runTaskAsynchronous(runnable);


    }

}
