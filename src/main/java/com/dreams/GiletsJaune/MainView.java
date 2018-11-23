/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreams.GiletsJaune;

import com.dreams.GiletsJaune.JoueurEditor;
import com.dreams.giletsjaune.model.Joueur;
import com.dreams.giletsjaune.service.JoueurRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {

    private final JoueurRepository repo;
    private final JoueurEditor editor;
    final Grid<Joueur> grid;
    private final Button addNewBtn;

    public MainView(JoueurRepository repo, JoueurEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Joueur.class);

        TextField filter = new TextField();
        this.addNewBtn = new Button("Nouveau Joueur", VaadinIcon.PLUS.create());

     
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "prenom", "nom","adresse","genre", "score");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
       
        
        filter.setPlaceholder("Filter par nom");

       
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listeJoueurs(e.getValue()));

        
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editJoueur(e.getValue());
        });

       
        addNewBtn.addClickListener(e -> editor.editJoueur(new Joueur()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listeJoueurs(filter.getValue());
        });

        // Initialize listing
        listeJoueurs(null);

        
    }

    private void listeJoueurs(String texte) {
        if (StringUtils.isEmpty(texte)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByNomStartsWithIgnoreCase(texte));
        }

    }

    private void listeJoueurs() {
        grid.setItems(repo.findAll());
    }

}
