package com.squalala.dz6android.eventbus;

/**
 * Created by Back Packer
 * Date : 04/10/15
 */
public class PostActionEvent {

    public ActionType actionType;

    public PostActionEvent(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public enum ActionType {
        HIDE_READ_ARTICLES,
        SET_ALL_POST_READ,
        DELETE_ALL_READ_POST,
        DELETE_ALL
    }
}
