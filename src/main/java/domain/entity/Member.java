package domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String profileImage;

    private String loginProvider;

    private String socialId;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner")
    private List<Persona> personas = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> followerList = new ArrayList<>();
}
