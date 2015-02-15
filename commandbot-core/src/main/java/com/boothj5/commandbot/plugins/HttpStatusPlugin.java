package com.boothj5.commandbot.plugins;

import com.boothj5.commandbot.CommandBotException;
import com.boothj5.commandbot.CommandBotPlugin;
import com.boothj5.commandbot.CommandBotRoom;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HttpStatusPlugin implements CommandBotPlugin {
    public static final String COMMAND = "status";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getHelp() {
        return COMMAND + " [url] - Get the http status code for a URL.";
    }

    @Override
    public void onMessage(CommandBotRoom muc, String from, String message) throws CommandBotException {
        String[] split = StringUtils.split(message, " ");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(split[1]);
        try {
            HttpResponse response = client.execute(get);
            response.getEntity().getContent().close();
            muc.sendMessage("Status " + split[1] + ": " + response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            e.printStackTrace();
            muc.sendMessage("Could not connect " + split[1] + ": " + e.getMessage());
        }
    }
}
