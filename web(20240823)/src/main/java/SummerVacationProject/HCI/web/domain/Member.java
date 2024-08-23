package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "Members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickName")
    private String nickName;

    @Column(name = "tagLine")
    private String tagLine;

    @Column(name = "gameName")
    private String gameName;

    @Column(name = "birth")
    private String birth;

    @Column(name = "admin")
    private boolean admin;

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<BaseGallery> baseGalleriesList;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Post> postList;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Comment> commentList;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Recommendation> recommendationList;

    @PreRemove
    private void removeRelatedGalleries() {
        for (BaseGallery gallery : baseGalleriesList) {
            gallery.setCreator(null); // creator를 null로 설정
        }
        for (Post post : postList) {
            post.setAuthor(null);
        }
        for (Comment comment : commentList) {
            comment.setAuthor(null);
        }
        for (Recommendation recommendation : recommendationList) {
            recommendation.setMember(null);
        }
    }

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Member(String email, String password, String nickName, String tagLine, String gameName, String birth, boolean admin) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.tagLine = tagLine;
        this.gameName = gameName;
        this.birth = birth;
        this.admin = admin;
    }

    public void update (String nickName, String gameName, String tagLine, String birth) {
        this.nickName = nickName;
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.birth = birth;
    }

    public void changePW(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));    //ysh
        } else {
            return List.of(new SimpleGrantedAuthority("member"));
        }
    }

    @Override
    public String getUsername() {
        return nickName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
