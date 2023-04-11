package dev.matthew.clans.enums;

import lombok.Getter;

public enum Role {

    LEADER(3),
    CAPTAIN(2),
    MEMBER(1),
    NONE(0);

    @Getter
    private final int width;

    Role(int width) {
        this.width = width;
    }
}
