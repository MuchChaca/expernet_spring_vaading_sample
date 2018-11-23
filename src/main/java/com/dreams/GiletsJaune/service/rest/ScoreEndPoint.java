/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreams.GiletsJaune.service.rest;

import com.dreams.giletsjaune.model.Joueur;
import com.dreams.giletsjaune.service.JoueurRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreEndPoint {

    @Autowired
    private JoueurRepository repository;

    @RequestMapping("/score/{id}")
    public Integer getScore(@PathVariable("id") Long id) {
        if (id == null || !repository.existsById(id)) {
            return -1;
        }
        Optional<Joueur> j;
        try {
            j = repository.findById(id);
        } catch (Exception e) {
            return -1;

        }
        return j.get().getScore();
    }

    @RequestMapping("/score/{playerId}/{score}")
    public Integer setScore(@PathVariable("playerId") Long playerId, @PathVariable("score") Integer score) {
        if (playerId == null || !repository.existsById(playerId)) {
            return -1;
        }
        Optional<Joueur> j;
        try {
            j = repository.findById(playerId);
            j.get().setScore(score);
            repository.save(j.get());
        } catch (Exception e) {
            return -1;

        }
        return 0;
    }

}
