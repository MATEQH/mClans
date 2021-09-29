package me.matthew.teams.handler.enums;

import lombok.Getter;

public enum Role {

    LEADER(3),
    CAPTAIN(2),
    MEMBER(1);

    @Getter
    private int width;

    Role(int width) {
        this.width = width;
    }
}
