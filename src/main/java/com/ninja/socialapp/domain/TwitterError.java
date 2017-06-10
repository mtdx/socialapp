package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.ninja.socialapp.domain.enumeration.TwitterErrorType;

/**
 * A TwitterError.
 */
@Entity
@Table(name = "twitter_error")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twittererror")
public class TwitterError implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private TwitterErrorType type;

    @Column(name = "error_code")
    private Integer errorCode;

    @Size(min = 4, max = 15)
    @Pattern(regexp = "(^[a-zA-Z0-9_]*$)")
    @Column(name = "jhi_account", length = 15)
    private String account;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "message")
    private String message;

    @Size(max = 20)
    @Column(name = "rate_limit_status", length = 20)
    private String rateLimitStatus;

    @Column(name = "status_code")
    private Integer statusCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TwitterErrorType getType() {
        return type;
    }

    public TwitterError type(TwitterErrorType type) {
        this.type = type;
        return this;
    }

    public void setType(TwitterErrorType type) {
        this.type = type;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public TwitterError errorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getAccount() {
        return account;
    }

    public TwitterError account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TwitterError errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public TwitterError message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRateLimitStatus() {
        return rateLimitStatus;
    }

    public TwitterError rateLimitStatus(String rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
        return this;
    }

    public void setRateLimitStatus(String rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public TwitterError statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterError twitterError = (TwitterError) o;
        if (twitterError.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterError.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterError{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", errorCode='" + getErrorCode() + "'" +
            ", account='" + getAccount() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", message='" + getMessage() + "'" +
            ", rateLimitStatus='" + getRateLimitStatus() + "'" +
            ", statusCode='" + getStatusCode() + "'" +
            "}";
    }
}
