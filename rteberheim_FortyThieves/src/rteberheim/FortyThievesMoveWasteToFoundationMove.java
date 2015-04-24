package rteberheim;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Pile;

/**
 * Represents the move of a card from the waste pile to the foundation
 * @author:  George T. Heineman (heineman@cs.wpi.edu)
 */
public class FortyThievesMoveWasteToFoundationMove extends ks.common.model.Move {
	/** The Waste Pile. */
	protected Pile waste;
	
	/** The Card being dragged. */
	protected Card draggingCard;
	
	/** The Foundation Pile. */
	protected Pile foundation;	
/**
 * MoveWasteToFoundationMove constructor.
 * <p>
 * If <code>draggingCard</code> is null, then the move has not yet been initiated, and
 * a card is to be removed from waste and added to foundation.
 * <p>
 * If <code>draggingCard</code> is not null, then waste has already been modified, and
 * the card removed from the top of the waste pile is <code>draggingCard</code>.
 */
public FortyThievesMoveWasteToFoundationMove(Pile waste, Card draggingCard, Pile foundation) {
	super();

	this.waste = waste;
	this.draggingCard = draggingCard;
	this.foundation = foundation;
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
	// Verify we can do the move.
	if (valid (theGame) == false)
		return false;

	// EXECUTE:
	// Deal with both situations
	if (draggingCard == null)
		foundation.add (waste.get());
	else
		foundation.add (draggingCard);

	// advance score
	theGame.updateScore (1);
	return true;
}
/**
 * Move the card back to the waste pile.
 * <p>
 * Note: in Undo, we always ignore the 'draggingCard' because the move has already
 * happened.
 */
public boolean undo(ks.common.games.Solitaire game) {

	// VALIDATE:
	if (foundation.empty()) return false;

	// EXECUTE:
	// remove card and move to waste.
	waste.add (foundation.get());

	// reverse score advance
	game.updateScore (-1);
	return true;
}
/**
 * Action for Klondike: WastePile card dragged to Foundation Pile.
 * <p>
 * @param d ks.common.games.Solitaire
 */
public boolean valid(ks.common.games.Solitaire game) {
	// VALIDATION:
	boolean validation = false;

	// If draggingCard is null, then no action has yet taken place.
	Card c;
	if (draggingCard == null) {
		if (waste.empty()) return false;   // NOTHING TO EXTRACT!
		c = waste.peek();
	} else {
		c = draggingCard;
	}
	
	// moveWasteToFoundation(waste,pile) : not foundation.empty() and not waste.empty() and 
	if (!foundation.empty() && (c.getRank() == foundation.rank() + 1) && (c.getSuit() == foundation.suit()))
		validation = true;

		// moveWasteToFoundation(waste,pile) : foundation.empty() and card.rank() == ACE
	if (foundation.empty() && (c.getRank() == Card.ACE))
		validation = true;

	return validation;
}
}
