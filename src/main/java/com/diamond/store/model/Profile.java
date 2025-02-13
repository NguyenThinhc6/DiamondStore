package com.diamond.store.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    private String profileId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    private Image profileImage;
}
