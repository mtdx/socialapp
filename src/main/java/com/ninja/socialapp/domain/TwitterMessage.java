package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TwitterMessage.
 */
@Entity
@Table(name = "twitter_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twittermessage")
public class TwitterMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "account_name", length = 20, nullable = false)
    private String accountName;

    @NotNull
    @Size(max = 160)
    @Column(name = "account_description", length = 160, nullable = false)
    private String accountDescription;

    @NotNull
    @Pattern(regexp = "(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    @Column(name = "account_url", nullable = false)
    private String accountUrl;

    @NotNull
    @Size(max = 60)
    @Column(name = "account_location", length = 60, nullable = false)
    private String accountLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public TwitterMessage accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public TwitterMessage accountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
        return this;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public TwitterMessage accountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
        return this;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public String getAccountLocation() {
        return accountLocation;
    }

    public TwitterMessage accountLocation(String accountLocation) {
        this.accountLocation = accountLocation;
        return this;
    }

    public void setAccountLocation(String accountLocation) {
        this.accountLocation = accountLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterMessage twitterMessage = (TwitterMessage) o;
        if (twitterMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterMessage{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", accountDescription='" + getAccountDescription() + "'" +
            ", accountUrl='" + getAccountUrl() + "'" +
            ", accountLocation='" + getAccountLocation() + "'" +
            "}";
    }
}
