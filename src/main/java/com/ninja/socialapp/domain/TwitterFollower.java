package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A TwitterFollower.
 */
@Entity
@Table(name = "twitter_follower")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twitterfollower")
public class TwitterFollower implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Pattern(regexp = "(^\\d+$)")
    @Column(name = "userid", length = 20, nullable = false)
    private String userid;

    @Min(value = 0)
    @Column(name = "account_age")
    private Integer accountAge;

    @Min(value = 0)
    @Column(name = "likes")
    private Integer likes;

    @Min(value = 0)
    @Column(name = "followers")
    private Integer followers;

    @Min(value = 0)
    @Column(name = "tweets")
    private Integer tweets;

    @Size(min = 4, max = 15)
    @Pattern(regexp = "(^[a-zA-Z0-9_]*$)")
    @Column(name = "username", length = 15)
    private String username;

    @Column(name = "jhi_like")
    private Boolean like;

    @Column(name = "follow")
    private Boolean follow;

    @Column(name = "updated")
    private Instant updated = Instant.now();

    @ManyToOne
    private Competitor competitor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public TwitterFollower userid(String userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getAccountAge() {
        return accountAge;
    }

    public TwitterFollower accountAge(Integer accountAge) {
        this.accountAge = accountAge;
        return this;
    }

    public void setAccountAge(Integer accountAge) {
        this.accountAge = accountAge;
    }

    public Integer getLikes() {
        return likes;
    }

    public TwitterFollower likes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getFollowers() {
        return followers;
    }

    public TwitterFollower followers(Integer followers) {
        this.followers = followers;
        return this;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getTweets() {
        return tweets;
    }

    public TwitterFollower tweets(Integer tweets) {
        this.tweets = tweets;
        return this;
    }

    public void setTweets(Integer tweets) {
        this.tweets = tweets;
    }

    public String getUsername() {
        return username;
    }

    public TwitterFollower username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isLike() {
        return like;
    }

    public TwitterFollower like(Boolean like) {
        this.like = like;
        return this;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean isFollow() {
        return follow;
    }

    public TwitterFollower follow(Boolean follow) {
        this.follow = follow;
        return this;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public Instant getUpdated() {
        return updated;
    }

    public TwitterFollower updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public TwitterFollower competitor(Competitor competitor) {
        this.competitor = competitor;
        return this;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterFollower twitterFollower = (TwitterFollower) o;
        if (twitterFollower.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterFollower.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterFollower{" +
            "id=" + getId() +
            ", userid='" + getUserid() + "'" +
            ", accountAge='" + getAccountAge() + "'" +
            ", likes='" + getLikes() + "'" +
            ", followers='" + getFollowers() + "'" +
            ", tweets='" + getTweets() + "'" +
            ", username='" + getUsername() + "'" +
            ", like='" + isLike() + "'" +
            ", follow='" + isFollow() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
