package rteberheim;

import java.awt.Dimension;

import heineman.Klondike;
import heineman.klondike.FoundationController;
import heineman.klondike.KlondikeDeckController;
import heineman.klondike.WastePileController;
import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardImages;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class FortyThieves extends Solitaire{

	Deck deck;
	Pile wastePile;
	Pile foundationPiles[] = new Pile[9];
	BuildablePile tableauPiles[] = new BuildablePile[11];
	
	DeckView deckView;
	PileView wastePileView;
	PileView foundationViews[] = new PileView[9];
	BuildablePileView tableauViews[] = new BuildablePileView[11];
	IntegerView scoreView;
	IntegerView numLeftView;
	
	public FortyThieves() {
		super();
	}
	
	@Override
	public String getName() {
		return "rteberheim-FortyThieves";
	}

	@Override
	public boolean hasWon() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initialize() {
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		
		
		//Set up each of the ten tableaus with 4 face up cards each
		for (int pileNum=1; pileNum <= 10; pileNum++) {
			for (int num = 1; num <= 4; num++) {
				Card c = deck.get();
				c.setFaceUp (true);
				tableauPiles[pileNum].add (c);
			}

		
			
		}

		updateNumberCardsLeft (-40);
		
		
	}
	private void initializeControllers() {
		deckView.setMouseAdapter(new FortyThievesDeckController (this, deck, wastePile));
		deckView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Now for each BuildablePile.
		for (int i = 1; i <= 10; i++) {
			tableauViews[i].setMouseAdapter (new FortyThievesBuildablePileController (this, tableauViews[i]));
			tableauViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			tableauViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Now for each Foundation.
		for (int i = 1; i <= 8; i++) {
			foundationViews[i].setMouseAdapter (new FortyThievesFoundationController (this, foundationViews[i]));
			foundationViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			foundationViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// WastePile
		wastePileView.setMouseAdapter (new FortyThievesWastePileController (this, wastePileView));
		wastePileView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
		wastePileView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Ensure that any releases (and movement) are handled by the non-interactive widgets
		numLeftView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numLeftView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numLeftView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Finally, cover the Container for any events not handled by a widget:
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));		
	}

	private void initializeView() {
		CardImages ci = getCardImages();

		deckView = new DeckView(deck);
		deckView.setBounds(20,100, ci.getWidth(), ci.getHeight());
		
		container.addWidget (deckView);

		// create BuildablePileViews, one after the other (default to 13 full cards -- more than we'll need)
		for (int pileNum = 1; pileNum <= 10; pileNum++) {
			tableauViews[pileNum] = new BuildablePileView (tableauPiles[pileNum]);
			tableauViews[pileNum].setBounds (29*pileNum + (pileNum-1)*ci.getWidth(), ci.getHeight() + 110, ci.getWidth(), 13*ci.getHeight());
			container.addWidget (tableauViews[pileNum]);
			
		}

		// create PileViews, one after the other.
		for (int pileNum = 1; pileNum <=8; pileNum++) {
			foundationViews[pileNum] = new PileView (foundationPiles[pileNum]);
			foundationViews[pileNum].setBounds (20*(pileNum+3) + ci.getWidth()*(pileNum+2), 100, ci.getWidth(), ci.getHeight());
			container.addWidget (foundationViews[pileNum]);
		}

		wastePileView = new PileView (wastePile);
		wastePileView.setBounds (20*2 + ci.getWidth(), 100, ci.getWidth(), ci.getHeight());
		container.addWidget (wastePileView);

		scoreView = new IntegerView (getScore());
		scoreView.setName("ScoreView");
		scoreView.setBounds (20*3+3*ci.getWidth(), -10, 160, 80);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setName("NumLeftView");
		numLeftView.setFontSize (18);
		numLeftView.setBounds (20, 20, ci.getWidth(), 80);
		container.addWidget (numLeftView);
		
		
	}

	private void initializeModel(int seed) {
		deck = new Deck("deck");
		deck.create(seed);
		model.addElement (deck);   // add to our model (as defined within our superclass).

		// each of the columns appears here
		for (int i = 1; i<=10; i++) {
			tableauPiles[i] = new BuildablePile ("pile" + i);
			model.addElement (tableauPiles[i]);
		} 

		// develop foundations
		for (int i = 1; i<=8; i++) {
			foundationPiles[i] = new Pile ("foundation" + i);
			model.addElement (foundationPiles[i]);
		}

		wastePile = new Pile ("waste");
		model.addElement (wastePile);

		// initial score is set to ZERO (every Solitaire game by default has a score) and there are 52 cards left.
		// NOTE: These will be added to the model by solitaire Base Class.
		this.updateNumberCardsLeft(104);
		this.updateScore(0);
		// Lastly, as part of the mode, we will eventually provide a way to register the 
		// type of allowed moves. This feature will be necessary for SolitaireSolvers
		
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(1100, 500);
	}
	
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		
		Main.generateWindow(new FortyThieves(), Deck.OrderBySuit);		
	}
}
