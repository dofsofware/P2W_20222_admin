package com.techxel.play2win_admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Gains.
 */
@Entity
@Table(name = "gains")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gains implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 12, max = 12)
    @Column(name = "telephone", length = 12, nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "minute", nullable = false)
    private Double minute;

    @NotNull
    @Column(name = "megabit", nullable = false)
    private Double megabit;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @ManyToMany(mappedBy = "gains")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gains" }, allowSetters = true)
    private Set<Abonne> abonnes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gains id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Gains telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getMinute() {
        return this.minute;
    }

    public Gains minute(Double minute) {
        this.setMinute(minute);
        return this;
    }

    public void setMinute(Double minute) {
        this.minute = minute;
    }

    public Double getMegabit() {
        return this.megabit;
    }

    public Gains megabit(Double megabit) {
        this.setMegabit(megabit);
        return this;
    }

    public void setMegabit(Double megabit) {
        this.megabit = megabit;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Gains createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Abonne> getAbonnes() {
        return this.abonnes;
    }

    public void setAbonnes(Set<Abonne> abonnes) {
        if (this.abonnes != null) {
            this.abonnes.forEach(i -> i.removeGains(this));
        }
        if (abonnes != null) {
            abonnes.forEach(i -> i.addGains(this));
        }
        this.abonnes = abonnes;
    }

    public Gains abonnes(Set<Abonne> abonnes) {
        this.setAbonnes(abonnes);
        return this;
    }

    public Gains addAbonne(Abonne abonne) {
        this.abonnes.add(abonne);
        abonne.getGains().add(this);
        return this;
    }

    public Gains removeAbonne(Abonne abonne) {
        this.abonnes.remove(abonne);
        abonne.getGains().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gains)) {
            return false;
        }
        return id != null && id.equals(((Gains) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gains{" +
            "id=" + getId() +
            ", telephone='" + getTelephone() + "'" +
            ", minute=" + getMinute() +
            ", megabit=" + getMegabit() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
