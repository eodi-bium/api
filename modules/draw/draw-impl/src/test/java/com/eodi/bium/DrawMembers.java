package com.eodi.bium;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DrawMembers {

    private final List<String> members;

    public DrawMembers() {
        this.members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.members.add(UUID.randomUUID().toString());
        }
    }

    public List<String> getMembers() {
        return new ArrayList<>(members);
    }
}
