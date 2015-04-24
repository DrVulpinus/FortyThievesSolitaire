package rteberheim;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Pile;

/**
 * Represents the move of a card from the waste pile to a buildable pile
 * @author:  George T. Heineman (heineman@cs.wpi.edu)
 */
public class FortyThievesMoveWasteToPileMove extends ks.common.model.Move {
	/** The waste pile. */
	protected Pile waste;

	/** The Destination column. */
	protected BuildablePile to;
	
	/** The card being dragged (if the move has already been made). */
	protected Card cardBeingDragged;
/**
 * MoveWasteToFoundationMove constructor comment.
 */
public FortyThievesMoveWasteToPileMove(Pile waste, Card cardBeingDragged, BuildablePile toPile) {
	super();

	this.waste = waste;
	this.to = toPile;
	this.cardBeingDragged = cardBeingDragged;
}
/**
 * Each move should knows how to execute itself.
 * <p>
 * Creation date: (10/21/01 3:33:39 PM)
 * @param ks.game.Solitaire   the game being played.
 * @return boolean
 * @since V1.6.2
 */
public boolean doMove (Solitaire theGame) {
	// VALIDATE:
	if (valid(theGame) == false)
		return false;

	// EXECUTE:
	// move card from waste to to Pile.
	if (cardBeingDragged == null)
		to.add (waste.get());
	else
		to.add (cardBeingDragged);
		
	return true;
}
/**
 * Move the card back to the waste pile (can only be one card)
 */
public boolean undo(ks.common.games.Solitaire game) {
	// VALIDATE:
	if (to.empty()) return false;

	// EXECUTE:
	// remove card and move to waste.
	waste.add (to.get());	
	return true;
}
/**
 * Action for Klondike: Move a card to from the Waste Pile to a Buildable Pile
 * @param game ks.games.Solitaire
 */
public boolean valid (Solitaire theGame) {
	
	// VALIDATION:
	boolean validation = false;

	//   moveWasteToPile (Waste from, BuildablePile to) : to.empty()
	if (to.empty())
		validation = true;

	if (cardBeingDragged == null) {
		// 	  moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and waste.rank() == to.rank() - 1 and to.peek().faceUp()
		if (!to.empty() && (waste.rank() == to.rank() - 1) && (waste.peek().oppositeColor(to.peek())) && to.peek().isFaceUp()) 
			validation = true;

	} else {
		// 	  moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and cardBeingDragged.rank() == to.rank() - 1 and to.peek().faceUp()
		if (!to.empty() && (cardBeingDragged.getRank() == to.rank() - 1) && (cardBeingDragged.oppositeColor(to.peek())) && to.peek().isFaceUp()) 
			validation = true;
	}
	return validation;
}
}
