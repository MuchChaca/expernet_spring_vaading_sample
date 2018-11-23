/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreams.GiletsJaune;

import com.dreams.giletsjaune.model.Joueur;
import com.dreams.giletsjaune.service.JoueurRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class JoueurEditor extends VerticalLayout implements KeyNotifier  {
    
    private final JoueurRepository repository;

	
	private Joueur joueur;

	
	TextField nom = new TextField("nom");
	TextField prenom = new TextField("prenom");
        TextField adresse = new TextField("adresse");
        TextField genre = new TextField("genre");
        TextField score = new TextField("score");

	/* Action buttons */
	// TODO why more code?
	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Joueur> binder = new Binder<>(Joueur.class);
	private ChangeHandler changeHandler;

	@Autowired
	public JoueurEditor(JoueurRepository repository) {
		this.repository = repository;


		// build layout
		
		add(nom, prenom,adresse,genre,score, actions);
binder.forField(score)
                .withConverter(new StringToIntegerConverter(""))
               
                .bind(Joueur::getScore, Joueur::setScore);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		save.getElement().getThemeList().add("primary");
		delete.getElement().getThemeList().add("error");

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editJoueur(joueur));
		setVisible(false);
	}

	void delete() {
		repository.delete(joueur);
		changeHandler.onChange();
	}

	void save() {
		repository.save(joueur);
		changeHandler.onChange();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editJoueur(Joueur c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			joueur = repository.findById(c.getId()).get();
		}
		else {
			joueur = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(joueur);

		setVisible(true);

		// Focus first name initially
		nom.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
