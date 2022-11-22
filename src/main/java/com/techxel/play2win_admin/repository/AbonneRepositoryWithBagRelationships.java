package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Abonne;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AbonneRepositoryWithBagRelationships {
    Optional<Abonne> fetchBagRelationships(Optional<Abonne> abonne);

    List<Abonne> fetchBagRelationships(List<Abonne> abonnes);

    Page<Abonne> fetchBagRelationships(Page<Abonne> abonnes);
}
