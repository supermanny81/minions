package com.boothj5.minions.slangslate;

import com.boothj5.minions.Minion;
import com.boothj5.minions.MinionsException;
import com.boothj5.minions.MinionsRoom;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SlangslateMinion extends Minion {

    private Map<String, String> lastMessages = new HashMap<>();

    @Override
    public String getHelp() {
        return "[term|user] - Translate internet slang term or user's last message";
    }

    @Override
    public void onMessage(MinionsRoom muc, String from, String message) throws MinionsException {
        lastMessages.put(from, message);
    }

    @Override
    public void onCommand(MinionsRoom muc, String from, String message) throws MinionsException {
        String arg = message.trim();

        if ("".equals(arg)) {
            muc.sendMessage(from + " nothing doesn't mean anything.");

        } else if (lastMessages.containsKey(arg)) {
            String origMessage = lastMessages.get(arg);
            String transMessage = "";

            String[] words = origMessage.split(" ");
            for (String origWord : words) {
                String transWord = translate(origWord);
                transMessage += transWord != null ? transWord + " " : origWord + " ";
            }

            muc.sendMessage(arg + " said: " + transMessage);

        } else {
            String translated = translate(arg);
            String responseMessage = translated != null ? arg + " = " + translated : "Soz " + from + ", idk";
            muc.sendMessage(responseMessage);
        }
    }

    private String translate(String slang) {
        try {
            String slangLower = slang.toLowerCase();
            HttpClient client = HttpClientBuilder.create().build();

            HttpGet get = new HttpGet("http://www.noslang.com/search.php?st=" + slangLower + "&submit=Search");
            HttpResponse response = client.execute(get);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            // <a name="yh"></a><abbr title="yeah"><b>
            String findStart = "<a name=\"" + slangLower + "\"></a><abbr title=\"";
            String findEnd = "\"><b>" + slangLower + "</b>";
            int foundStart = responseString.indexOf(findStart);
            int start = foundStart + findStart.length();

            String startRemoved = responseString.substring(start);
            int end = startRemoved.indexOf(findEnd);

            return startRemoved.substring(0, end);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
