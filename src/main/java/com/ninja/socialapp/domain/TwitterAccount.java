package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.ninja.socialapp.domain.enumeration.TwitterStatus;

/**
 * A TwitterAccount.
 */
@Entity
@Table(name = "twitter_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twitteraccount")
public class TwitterAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 254)
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @Column(name = "email", length = 254, nullable = false)
    private String email;

    @NotNull
    @Size(min = 4, max = 60)
    @Pattern(regexp = "(^[a-zA-Z0-9]*$)")
    @Column(name = "consumer_key", length = 60, nullable = false)
    private String consumerKey;

    @NotNull
    @Size(min = 4, max = 60)
    @Pattern(regexp = "(^[a-zA-Z0-9]*$)")
    @Column(name = "consumer_secret", length = 60, nullable = false)
    private String consumerSecret;

    @NotNull
    @Size(min = 4, max = 80)
    @Pattern(regexp = "(^[a-zA-Z0-9-]*$)")
    @Column(name = "access_token", length = 80, nullable = false)
    private String accessToken;

    @NotNull
    @Size(min = 4, max = 60)
    @Pattern(regexp = "(^[a-zA-Z0-9]*$)")
    @Column(name = "access_token_secret", length = 60, nullable = false)
    private String accessTokenSecret;

    @Size(min = 4, max = 15)
    @Pattern(regexp = "(^[a-zA-Z0-9_]*$)")
    @Column(name = "username", length = 15)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TwitterStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private Avatar avatar;

    @ManyToOne(optional = false)
    @NotNull
    private Header header;

    @ManyToOne(optional = false)
    @NotNull
    private Proxy proxy;

    @ManyToOne(optional = false)
    @NotNull
    private TwitterMessage message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public TwitterAccount email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public TwitterAccount consumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
        return this;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public TwitterAccount consumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
        return this;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public TwitterAccount accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public TwitterAccount accessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
        return this;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getUsername() {
        return username;
    }

    public TwitterAccount username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TwitterStatus getStatus() {
        return status;
    }

    public TwitterAccount status(TwitterStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TwitterStatus status) {
        this.status = status;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public TwitterAccount avatar(Avatar avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Header getHeader() {
        return header;
    }

    public TwitterAccount header(Header header) {
        this.header = header;
        return this;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public TwitterAccount proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public TwitterMessage getMessage() {
        return message;
    }

    public TwitterAccount message(TwitterMessage twitterMessage) {
        this.message = twitterMessage;
        return this;
    }

    public void setMessage(TwitterMessage twitterMessage) {
        this.message = twitterMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterAccount twitterAccount = (TwitterAccount) o;
        if (twitterAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterAccount{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", consumerKey='" + getConsumerKey() + "'" +
            ", consumerSecret='" + getConsumerSecret() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            ", accessTokenSecret='" + getAccessTokenSecret() + "'" +
            ", username='" + getUsername() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
