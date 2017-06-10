package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.ninja.socialapp.domain.enumeration.CompetitorStatus;

/**
 * A Competitor.
 */
@Entity
@Table(name = "competitor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "competitor")
public class Competitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CompetitorStatus status;

    @NotNull
    @Size(min = 2, max = 20)
    @Pattern(regexp = "(^\\d+$)")
    @Column(name = "userid", length = 20, nullable = false)
    private String userid;

    @NotNull
    @Size(min = 4, max = 15)
    @Pattern(regexp = "(^[a-zA-Z0-9_]*$)")
    @Column(name = "username", length = 15, nullable = false)
    private String username;

    @Column(name = "likes")
    private Long likes;

    @Column(name = "jhi_cursor")
    private Long cursor;

    @Column(name = "jhi_stop")
    private Boolean stop;

    @Column(name = "jhi_reset")
    private Boolean reset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitorStatus getStatus() {
        return status;
    }

    public Competitor status(CompetitorStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CompetitorStatus status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public Competitor userid(String userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public Competitor username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getLikes() {
        return likes;
    }

    public Competitor likes(Long likes) {
        this.likes = likes;
        return this;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getCursor() {
        return cursor;
    }

    public Competitor cursor(Long cursor) {
        this.cursor = cursor;
        return this;
    }

    public void setCursor(Long cursor) {
        this.cursor = cursor;
    }

    public Boolean isStop() {
        return stop;
    }

    public Competitor stop(Boolean stop) {
        this.stop = stop;
        return this;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public Boolean isReset() {
        return reset;
    }

    public Competitor reset(Boolean reset) {
        this.reset = reset;
        return this;
    }

    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Competitor competitor = (Competitor) o;
        if (competitor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), competitor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Competitor{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", userid='" + getUserid() + "'" +
            ", username='" + getUsername() + "'" +
            ", likes='" + getLikes() + "'" +
            ", cursor='" + getCursor() + "'" +
            ", stop='" + isStop() + "'" +
            ", reset='" + isReset() + "'" +
            "}";
    }
}
