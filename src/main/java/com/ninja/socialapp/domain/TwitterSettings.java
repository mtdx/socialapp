package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TwitterSettings.
 */
@Entity
@Table(name = "twitter_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twittersettings")
public class TwitterSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "max_likes", nullable = false)
    private Integer maxLikes;

    @Column(name = "has_default_profile_image")
    private Boolean hasDefaultProfileImage;

    @Column(name = "has_no_description")
    private Boolean hasNoDescription;

    @NotNull
    @Min(value = 0)
    @Column(name = "account_age_less_than", nullable = false)
    private Integer accountAgeLessThan;

    @NotNull
    @Min(value = 1)
    @Column(name = "min_activity", nullable = false)
    private Integer minActivity;

    @Column(name = "following_to_followers_ratio")
    private Integer followingToFollowersRatio;

    @Column(name = "likes_to_tweets_ratio")
    private Integer likesToTweetsRatio;

    @NotNull
    @Min(value = 0)
    @Column(name = "not_like_tweets_older_than", nullable = false)
    private Integer notLikeTweetsOlderThan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxLikes() {
        return maxLikes;
    }

    public TwitterSettings maxLikes(Integer maxLikes) {
        this.maxLikes = maxLikes;
        return this;
    }

    public void setMaxLikes(Integer maxLikes) {
        this.maxLikes = maxLikes;
    }

    public Boolean isHasDefaultProfileImage() {
        return hasDefaultProfileImage;
    }

    public TwitterSettings hasDefaultProfileImage(Boolean hasDefaultProfileImage) {
        this.hasDefaultProfileImage = hasDefaultProfileImage;
        return this;
    }

    public void setHasDefaultProfileImage(Boolean hasDefaultProfileImage) {
        this.hasDefaultProfileImage = hasDefaultProfileImage;
    }

    public Boolean isHasNoDescription() {
        return hasNoDescription;
    }

    public TwitterSettings hasNoDescription(Boolean hasNoDescription) {
        this.hasNoDescription = hasNoDescription;
        return this;
    }

    public void setHasNoDescription(Boolean hasNoDescription) {
        this.hasNoDescription = hasNoDescription;
    }

    public Integer getAccountAgeLessThan() {
        return accountAgeLessThan;
    }

    public TwitterSettings accountAgeLessThan(Integer accountAgeLessThan) {
        this.accountAgeLessThan = accountAgeLessThan;
        return this;
    }

    public void setAccountAgeLessThan(Integer accountAgeLessThan) {
        this.accountAgeLessThan = accountAgeLessThan;
    }

    public Integer getMinActivity() {
        return minActivity;
    }

    public TwitterSettings minActivity(Integer minActivity) {
        this.minActivity = minActivity;
        return this;
    }

    public void setMinActivity(Integer minActivity) {
        this.minActivity = minActivity;
    }

    public Integer getFollowingToFollowersRatio() {
        return followingToFollowersRatio;
    }

    public TwitterSettings followingToFollowersRatio(Integer followingToFollowersRatio) {
        this.followingToFollowersRatio = followingToFollowersRatio;
        return this;
    }

    public void setFollowingToFollowersRatio(Integer followingToFollowersRatio) {
        this.followingToFollowersRatio = followingToFollowersRatio;
    }

    public Integer getLikesToTweetsRatio() {
        return likesToTweetsRatio;
    }

    public TwitterSettings likesToTweetsRatio(Integer likesToTweetsRatio) {
        this.likesToTweetsRatio = likesToTweetsRatio;
        return this;
    }

    public void setLikesToTweetsRatio(Integer likesToTweetsRatio) {
        this.likesToTweetsRatio = likesToTweetsRatio;
    }

    public Integer getNotLikeTweetsOlderThan() {
        return notLikeTweetsOlderThan;
    }

    public TwitterSettings notLikeTweetsOlderThan(Integer notLikeTweetsOlderThan) {
        this.notLikeTweetsOlderThan = notLikeTweetsOlderThan;
        return this;
    }

    public void setNotLikeTweetsOlderThan(Integer notLikeTweetsOlderThan) {
        this.notLikeTweetsOlderThan = notLikeTweetsOlderThan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterSettings twitterSettings = (TwitterSettings) o;
        if (twitterSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterSettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterSettings{" +
            "id=" + getId() +
            ", maxLikes='" + getMaxLikes() + "'" +
            ", hasDefaultProfileImage='" + isHasDefaultProfileImage() + "'" +
            ", hasNoDescription='" + isHasNoDescription() + "'" +
            ", accountAgeLessThan='" + getAccountAgeLessThan() + "'" +
            ", minActivity='" + getMinActivity() + "'" +
            ", followingToFollowersRatio='" + getFollowingToFollowersRatio() + "'" +
            ", likesToTweetsRatio='" + getLikesToTweetsRatio() + "'" +
            ", notLikeTweetsOlderThan='" + getNotLikeTweetsOlderThan() + "'" +
            "}";
    }
}
