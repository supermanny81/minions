/*
 * Copyright 2015 James Booth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boothj5.minions;

import org.jivesoftware.smack.packet.Message;

class RoomMessageHandler extends MessageHandler {
    RoomMessageHandler(Message stanza, MinionStore minions, String minionsPrefix, MinionsRoom muc, String myNick) {
        super(stanza, minions, minionsPrefix, muc, myNick);
    }

    @Override
    void execute() {
        String from = org.jivesoftware.smack.util.StringUtils.parseResource(stanza.getFrom());
        minions.onRoomMessage(stanza.getBody(), from, muc);
    }
}
