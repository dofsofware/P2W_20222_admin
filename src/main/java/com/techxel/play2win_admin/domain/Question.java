package com.techxel.play2win_admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "quiz", nullable = false)
    private String quiz;

    @NotNull
    @Size(max = 50)
    @Column(name = "reponse_1", length = 50, nullable = false)
    private String reponse1;

    @NotNull
    @Size(max = 50)
    @Column(name = "reponse_2", length = 50, nullable = false)
    private String reponse2;

    @NotNull
    @Size(max = 50)
    @Column(name = "reponse_3", length = 50, nullable = false)
    private String reponse3;

    @NotNull
    @Size(max = 50)
    @Column(name = "reponse_4", length = 50, nullable = false)
    private String reponse4;

    @NotNull
    @Size(max = 50)
    @Column(name = "bonne_reponse", length = 50, nullable = false)
    private String bonneReponse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuiz() {
        return this.quiz;
    }

    public Question quiz(String quiz) {
        this.setQuiz(quiz);
        return this;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getReponse1() {
        return this.reponse1;
    }

    public Question reponse1(String reponse1) {
        this.setReponse1(reponse1);
        return this;
    }

    public void setReponse1(String reponse1) {
        this.reponse1 = reponse1;
    }

    public String getReponse2() {
        return this.reponse2;
    }

    public Question reponse2(String reponse2) {
        this.setReponse2(reponse2);
        return this;
    }

    public void setReponse2(String reponse2) {
        this.reponse2 = reponse2;
    }

    public String getReponse3() {
        return this.reponse3;
    }

    public Question reponse3(String reponse3) {
        this.setReponse3(reponse3);
        return this;
    }

    public void setReponse3(String reponse3) {
        this.reponse3 = reponse3;
    }

    public String getReponse4() {
        return this.reponse4;
    }

    public Question reponse4(String reponse4) {
        this.setReponse4(reponse4);
        return this;
    }

    public void setReponse4(String reponse4) {
        this.reponse4 = reponse4;
    }

    public String getBonneReponse() {
        return this.bonneReponse;
    }

    public Question bonneReponse(String bonneReponse) {
        this.setBonneReponse(bonneReponse);
        return this;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", quiz='" + getQuiz() + "'" +
            ", reponse1='" + getReponse1() + "'" +
            ", reponse2='" + getReponse2() + "'" +
            ", reponse3='" + getReponse3() + "'" +
            ", reponse4='" + getReponse4() + "'" +
            ", bonneReponse='" + getBonneReponse() + "'" +
            "}";
    }
}
