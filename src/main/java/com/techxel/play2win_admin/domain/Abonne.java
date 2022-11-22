package com.techxel.play2win_admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techxel.play2win_admin.domain.enumeration.Niveau;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Abonne.
 */
@Entity
@Table(name = "abonne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Abonne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "identifiant", length = 20, nullable = false, unique = true)
    private String identifiant;

    @NotNull
    @Size(min = 12, max = 12)
    @Column(name = "telephone", length = 12, nullable = false, unique = true)
    private String telephone;

    @NotNull
    @Size(min = 4, max = 70)
    @Column(name = "mot_de_passe", length = 70, nullable = false, unique = true)
    private String motDePasse;

    @NotNull
    @Column(name = "score", nullable = false)
    private Double score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private Niveau niveau;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "derniere_paticipation")
    private ZonedDateTime dernierePaticipation;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @Column(name = "code_ractivation")
    private String codeRactivation;

    @ManyToMany
    @JoinTable(
        name = "rel_abonne__gains",
        joinColumns = @JoinColumn(name = "abonne_id"),
        inverseJoinColumns = @JoinColumn(name = "gains_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "abonnes" }, allowSetters = true)
    private Set<Gains> gains = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Abonne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public Abonne identifiant(String identifiant) {
        this.setIdentifiant(identifiant);
        return this;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Abonne telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Abonne motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Double getScore() {
        return this.score;
    }

    public Abonne score(Double score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public Abonne niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Abonne createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getDernierePaticipation() {
        return this.dernierePaticipation;
    }

    public Abonne dernierePaticipation(ZonedDateTime dernierePaticipation) {
        this.setDernierePaticipation(dernierePaticipation);
        return this;
    }

    public void setDernierePaticipation(ZonedDateTime dernierePaticipation) {
        this.dernierePaticipation = dernierePaticipation;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Abonne actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getCodeRactivation() {
        return this.codeRactivation;
    }

    public Abonne codeRactivation(String codeRactivation) {
        this.setCodeRactivation(codeRactivation);
        return this;
    }

    public void setCodeRactivation(String codeRactivation) {
        this.codeRactivation = codeRactivation;
    }

    public Set<Gains> getGains() {
        return this.gains;
    }

    public void setGains(Set<Gains> gains) {
        this.gains = gains;
    }

    public Abonne gains(Set<Gains> gains) {
        this.setGains(gains);
        return this;
    }

    public Abonne addGains(Gains gains) {
        this.gains.add(gains);
        gains.getAbonnes().add(this);
        return this;
    }

    public Abonne removeGains(Gains gains) {
        this.gains.remove(gains);
        gains.getAbonnes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonne)) {
            return false;
        }
        return id != null && id.equals(((Abonne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abonne{" +
            "id=" + getId() +
            ", identifiant='" + getIdentifiant() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", score=" + getScore() +
            ", niveau='" + getNiveau() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", dernierePaticipation='" + getDernierePaticipation() + "'" +
            ", actif='" + getActif() + "'" +
            ", codeRactivation='" + getCodeRactivation() + "'" +
            "}";
    }
}
