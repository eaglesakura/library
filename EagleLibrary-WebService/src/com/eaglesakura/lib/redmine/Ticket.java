package com.eaglesakura.lib.redmine;

import java.util.HashMap;
import java.util.Map;

/**
 * Redmine上のチケットを示す。
 * @author SAKURA
 *
 */
public class Ticket {

    public static class Data {
        /**
         * 開始日
         */
        public String start_date = null;

        /**
         * コメント
         */
        public String description = null;

        /**
         * 親チケット番号
         */
        public Integer parent_id = null;

        /**
         * このチケットのID
         */
        public Integer id = null;

        /**
         * チケットのタイトル
         */
        public String subject = null;

        /**
         * 更新日
         */
        public String updated_on = null;

        /**
         * 進捗率
         */
        public Integer done_ratio = null;

        /**
         * プロジェクトのID
         */
        public String project_id = null;

        /**
         * チケット作成日時
         */
        public String created_on = null;

        /**
         * 割り当てられているユーザーのID
         */
        public Integer assigned_to_id = null;

        /**
         * チケット作成者ID
         */
        public Integer author_id = null;

        /**
         * 優先度ID
         */
        public Integer priority_id = null;

        /**
         * 現在のステータスを取得する。
         */
        public Integer status_id = null;
    }

    Data json = null;

    public Ticket(Data origin) {
        this.json = origin;
    }

    /**
     * チケットタイトルを取得する。
     * @return
     */
    public String getSubject() {
        return json.subject;
    }

    /**
     * チケット内容を取得する。
     * @return
     */
    public String getDescription() {
        return json.description;
    }

    /**
     * チケット作成日時を取得する
     * @return
     */
    public String getCreatedDate() {
        return json.created_on;
    }

    /**
     * チケット更新日時を取得する。
     * @return
     */
    public String getUpdatedDate() {
        return json.updated_on;
    }

    /**
     * 親チケット番号を取得する。
     * @return
     */
    public Integer getParentID() {
        return json.parent_id;
    }

    /**
     * チケット番号を取得する。
     * @return
     */
    public Integer getID() {
        return json.id;
    }

    /**
     * 進捗率を取得する。
     * @return
     */
    public Integer getDoneRatio() {
        return json.done_ratio;
    }

    /**
     * プロジェクトのIDを取得する。
     * @return
     */
    public String getProjectID() {
        return json.project_id;
    }

    /**
     * 割り当てられたユーザーのIDを取得する。
     * @return
     */
    public Integer getAssignedToId() {
        return json.assigned_to_id;
    }

    /**
     * チケットの作成者IDを取得する。
     * @return
     */
    public Integer getAuthorId() {
        return json.author_id;
    }

    /**
     * 優先度IDを取得する。
     * @return
     */
    public Integer getPriorityId() {
        return json.priority_id;
    }

    /**
     * 現在のステータスを取得する。
     * @return
     */
    public Integer getStatusId() {
        return json.status_id;
    }

    /**
     * 新規
     * 進行中
     * 解決
     * フィードバック
     * 終了
     * 却下
     * @author SAKURA
     *
     */
    public enum StatusID {
        New {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getText() {
                return "新規";
            }
        },

        Running {
            @Override
            public int getId() {
                return 2;
            }

            @Override
            public String getText() {
                return "進行中";
            }
        },
        Solution {
            @Override
            public int getId() {
                return 3;
            }

            @Override
            public String getText() {
                return "解決";
            }
        },
        Feedback {
            @Override
            public int getId() {
                return 4;
            }

            @Override
            public String getText() {
                return "フィードバック";
            }
        },
        End {
            @Override
            public int getId() {
                return 5;
            }

            @Override
            public String getText() {
                return "終了";
            }
        },
        Reject {
            @Override
            public int getId() {
                return 6;
            }

            @Override
            public String getText() {
                return "却下";
            }
        };

        public abstract String getText();

        public abstract int getId();
    }

    static final Map<Integer, StatusID> statusMap = new HashMap<Integer, StatusID>();

    static {

        {
            statusMap.put(StatusID.New.getId(), StatusID.New);
            statusMap.put(StatusID.Running.getId(), StatusID.Running);
            statusMap.put(StatusID.Solution.getId(), StatusID.Solution);
            statusMap.put(StatusID.Feedback.getId(), StatusID.Feedback);
            statusMap.put(StatusID.End.getId(), StatusID.End);
            statusMap.put(StatusID.Reject.getId(), StatusID.Reject);

        }
    }

    public StatusID getStatusIDWrapper() {
        return statusMap.get(getStatusId());
    }

    public static StatusID getStatusIDWrapper(Integer id) {
        return statusMap.get(id);
    }
}
