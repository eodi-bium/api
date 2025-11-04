package com.eodi.bium.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member extends CreatedAt {

    @Id
    private String memberId;
    private String password;
    private String role;
    private String provider;
    private String nickname;

    @Builder
    private Member(String memberId, String password, String role, String provider,
        String nickname) {
        super();
        this.memberId = memberId;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.nickname = nickname;
    }
}
