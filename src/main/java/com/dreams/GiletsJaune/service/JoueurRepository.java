
package com.dreams.giletsjaune.service;
import com.dreams.giletsjaune.model.Joueur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.*;


public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    
    public List<Joueur> findByNomStartsWithIgnoreCase(String t);
}
