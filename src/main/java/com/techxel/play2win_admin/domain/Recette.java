package com.techxel.play2win_admin.domain;

import com.techxel.play2win_admin.domain.enumeration.ChoixDuGain;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recette.
 */
@Entity
@Table(name = "recette")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recette implements Serializable {

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
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "choix_du_gain", nullable = false)
    private ChoixDuGain choixDuGain;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recette id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Recette telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Recette createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Recette montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public ChoixDuGain getChoixDuGain() {
        return this.choixDuGain;
    }

    public Recette choixDuGain(ChoixDuGain choixDuGain) {
        this.setChoixDuGain(choixDuGain);
        return this;
    }

    public void setChoixDuGain(ChoixDuGain choixDuGain) {
        this.choixDuGain = choixDuGain;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recette)) {
            return false;
        }
        return id != null && id.equals(((Recette) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recette{" +
            "id=" + getId() +
            ", telephone='" + getTelephone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", montant=" + getMontant() +
            ", choixDuGain='" + getChoixDuGain() + "'" +
            "}";
    }
}
