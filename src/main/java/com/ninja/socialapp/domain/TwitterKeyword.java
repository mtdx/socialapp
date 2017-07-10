package com.ninja.socialapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.ninja.socialapp.domain.enumeration.KeywordStatus;

/**
 * A TwitterKeyword.
 */
@Entity
@Table(name = "twitter_keyword")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twitterkeyword")
public class TwitterKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private KeywordStatus status;

    @NotNull
    @Size(min = 3)
    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "competitors")
    private Integer competitors;

    @Column(name = "page")
    private Integer page;

    @Column(name = "jhi_stop")
    private Boolean stop;

    @Column(name = "jhi_reset")
    private Boolean reset;

    @Column(name = "created")
    private Instant created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KeywordStatus getStatus() {
        return status;
    }

    public TwitterKeyword status(KeywordStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(KeywordStatus status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public TwitterKeyword keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCompetitors() {
        return competitors;
    }

    public TwitterKeyword competitors(Integer competitors) {
        this.competitors = competitors;
        return this;
    }

    public void setCompetitors(Integer competitors) {
        this.competitors = competitors;
    }

    public Integer getPage() {
        return page;
    }

    public TwitterKeyword page(Integer page) {
        this.page = page;
        return this;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Boolean isStop() {
        return stop;
    }

    public TwitterKeyword stop(Boolean stop) {
        this.stop = stop;
        return this;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public Boolean isReset() {
        return reset;
    }

    public TwitterKeyword reset(Boolean reset) {
        this.reset = reset;
        return this;
    }

    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    public Instant getCreated() {
        return created;
    }

    public TwitterKeyword created(Instant created) {
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
        TwitterKeyword twitterKeyword = (TwitterKeyword) o;
        if (twitterKeyword.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterKeyword.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterKeyword{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", keyword='" + getKeyword() + "'" +
            ", competitors='" + getCompetitors() + "'" +
            ", page='" + getPage() + "'" +
            ", stop='" + isStop() + "'" +
            ", reset='" + isReset() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
