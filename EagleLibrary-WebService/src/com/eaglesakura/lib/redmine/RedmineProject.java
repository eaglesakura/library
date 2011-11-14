package com.eaglesakura.lib.redmine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;

import com.eaglesakura.lib.redmine.Ticket.Data;

public class RedmineProject {
    String name = null;
    Redmine setting = null;

    public RedmineProject(Redmine setting, String name) {
        this.setting = setting;
        this.name = name;
    }

    /**
     * プロジェクトの基本URLを取得する。
     * @return
     */
    String getProjectUrl() {
        return setting.getUrl() + "projects/" + name + "/";
    }

    /**
     * プロジェクトに関連するすべてのチケットを取得する。
     * @return
     * @throws IOException
     */
    public List<Ticket> getAllTickets() throws IOException {
        String url = getProjectUrl() + "issues.json?key=" + setting.apiKey;

        Data[] datas = JSON.decode(new ByteArrayInputStream(setting.getUrlData(url)), Ticket.Data[].class);
        List<Ticket> result = new ArrayList<Ticket>();

        for (Data data : datas) {
            result.add(new Ticket(data));
        }
        return result;
    }
}
