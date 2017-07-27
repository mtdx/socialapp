package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.ninja.socialapp.domain.enumeration.RetweetAccountStatus;

/**
 * A RetweetAccount.
 */
@Entity
@Table(name = "retweet_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "retweetaccount")
public class RetweetAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RetweetAccountStatus status;

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

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "jhi_stop")
    private Boolean stop;

    @Column(name = "created")
    private Instant created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RetweetAccountStatus getStatus() {
        return status;
    }

    public RetweetAccount status(RetweetAccountStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(RetweetAccountStatus status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public RetweetAccount userid(String userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public RetweetAccount username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKeywords() {
        return keywords;
    }

    public RetweetAccount keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean isStop() {
        return stop;
    }

    public RetweetAccount stop(Boolean stop) {
        this.stop = stop;
        return this;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public Instant getCreated() {
        return created;
    }

    public RetweetAccount created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RetweetAccount retweetAccount = (RetweetAccount) o;
        if (retweetAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), retweetAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RetweetAccount{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", userid='" + getUserid() + "'" +
            ", username='" + getUsername() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", stop='" + isStop() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
