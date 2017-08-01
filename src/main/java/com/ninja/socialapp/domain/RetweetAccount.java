package com.ninja.socialapp.domain;

import com.ninja.socialapp.domain.enumeration.RetweetAccountStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

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
    @Size(min = 4, max = 15)
    @Pattern(regexp = "(^[a-zA-Z0-9_]*$)")
    @Column(name = "username", length = 15, nullable = false)
    private String username;

    @NotNull
    @Size(min = 2, max = 20)
    @Pattern(regexp = "(^\\d+$)")
    @Column(name = "tweet_id", length = 20, nullable = false)
    private String tweetId;

    @Column(name = "jhi_stop")
    private Boolean stop;

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

    public String getTweetId() {
        return tweetId;
    }

    public RetweetAccount tweetId(String tweetId) {
        this.tweetId = tweetId;
        return this;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
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
            ", username='" + getUsername() + "'" +
            ", tweetId='" + getTweetId() + "'" +
            ", stop='" + isStop() + "'" +
            "}";
    }
}
