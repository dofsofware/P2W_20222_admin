package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Abonne;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AbonneRepositoryWithBagRelationshipsImpl implements AbonneRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Abonne> fetchBagRelationships(Optional<Abonne> abonne) {
        return abonne.map(this::fetchGains);
    }

    @Override
    public Page<Abonne> fetchBagRelationships(Page<Abonne> abonnes) {
        return new PageImpl<>(fetchBagRelationships(abonnes.getContent()), abonnes.getPageable(), abonnes.getTotalElements());
    }

    @Override
    public List<Abonne> fetchBagRelationships(List<Abonne> abonnes) {
        return Optional.of(abonnes).map(this::fetchGains).orElse(Collections.emptyList());
    }

    Abonne fetchGains(Abonne result) {
        return entityManager
            .createQuery("select abonne from Abonne abonne left join fetch abonne.gains where abonne is :abonne", Abonne.class)
            .setParameter("abonne", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Abonne> fetchGains(List<Abonne> abonnes) {
        return entityManager
            .createQuery("select distinct abonne from Abonne abonne left join fetch abonne.gains where abonne in :abonnes", Abonne.class)
            .setParameter("abonnes", abonnes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
