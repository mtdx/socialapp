package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

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

    @Size(max = 20)
    @Column(name = "jhi_type", length = 20)
    private String type;

    @Column(name = "error_code")
    private Integer errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "exception_code")
    private String exceptionCode;

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

    public String getType() {
        return type;
    }

    public TwitterError type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
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

    public String getExceptionCode() {
        return exceptionCode;
    }

    public TwitterError exceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
        return this;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
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
            ", errorMessage='" + getErrorMessage() + "'" +
            ", exceptionCode='" + getExceptionCode() + "'" +
            ", message='" + getMessage() + "'" +
            ", rateLimitStatus='" + getRateLimitStatus() + "'" +
            ", statusCode='" + getStatusCode() + "'" +
            "}";
    }
}
