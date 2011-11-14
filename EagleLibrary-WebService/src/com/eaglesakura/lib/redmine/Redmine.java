package com.eaglesakura.lib.redmine;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.arnx.jsonic.JSON;

import com.eaglesakura.lib.redmine.Ticket.Data;
import com.eaglesakura.lib.redmine.Ticket.StatusID;
import com.eaglesakura.lib.util.EagleUtil;
import com.eaglesakura.lib.util.EagleUtil.HttpResult;

/**
 * Redmineでの個人設定を行う。
 * @author SAKURA
 *
 */
public class Redmine {
    String apiKey = null;
    String url = null;

    /**
     * Redmineの個人APIキーを設定する
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * APIキーを取得する。
     * @return
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Redmine本体のURLを取得する。
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Redmine本体のURLを設定する。
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * プロジェクトのURLから逆算する
     * @param projectUrl
     */
    public void setUrlByProjectUrl(String projectUrl) {
        projectUrl = projectUrl.substring(0, projectUrl.indexOf("/projects/") + 1);
        setUrl(projectUrl);
    }

    /**
     * URLから情報を取得する。
     * @param url
     * @return
     * @throws IOException
     */
    public byte[] getUrlData(String url) throws IOException {
        return EagleUtil.getURLData(url);
    }

    /**
     * チケットを取得する。
     * @param number
     * @return
     * @throws IOException
     */
    public Ticket getTicket(int number) throws IOException {
        String url = getUrl() + "issues/" + number + ".json?key=" + apiKey;
        byte[] urlData = getUrlData(url);
        {
            //            String json = new String(urlData);
            //            EagleUtil.log(json);
        }
        Data data = JSON.decode(new ByteArrayInputStream(urlData), Ticket.Data.class);
        return new Ticket(data);
    }

    public static class TicketPostData {
        public Data issue = null;

        public static class Data {
            public String project_id;
            public String id;
            public Integer done_ratio;
            public Integer status_id;
        }
    }

    /**
     * チケットを更新する。
     * @param ticket
     * @throws IOException
     */
    public void updateTicketDoneRatio(Ticket ticket, int ratio, String basicToken) throws IOException {
        String url = getUrl() + "issues/" + ticket.getID() + ".json";
        ratio = EagleUtil.minmax(0, 100, ratio);
        TicketPostData data = new TicketPostData();

        data.issue = JSON.decode(JSON.encode(ticket.json), TicketPostData.Data.class);
        data.issue.done_ratio = ratio;

        String postJson = JSON.encode(data, false);
        EagleUtil.setBasicAutorization(basicToken);
        HttpResult result = EagleUtil.putURLData(url, postJson.getBytes("UTF-8"), "application/json");
        EagleUtil.setBasicAutorization(null);
        if (result.status != 200) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * チケットを更新する。
     * @param ticket
     * @throws IOException
     */
    public void updateTicketStatusID(Ticket ticket, StatusID id, String basicToken) throws IOException {
        String url = getUrl() + "issues/" + ticket.getID() + ".json";
        TicketPostData data = new TicketPostData();

        data.issue = JSON.decode(JSON.encode(ticket.json), TicketPostData.Data.class);
        data.issue.status_id = id.getId();

        String postJson = JSON.encode(data, false);
        EagleUtil.setBasicAutorization(basicToken);
        HttpResult result = EagleUtil.putURLData(url, postJson.getBytes("UTF-8"), "application/json");
        EagleUtil.setBasicAutorization(null);
        if (result.status != 200) {
            throw new UnsupportedOperationException();
        }
    }
}
