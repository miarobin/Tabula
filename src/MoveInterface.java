/**
 * MoveInterface represents the use of a single Die to move a single piece.
 *
 * Requires a constructor with no parameters.
 *
 **/

public interface MoveInterface {

    /**
     * @param locationNumber represents the board position to move a piece from
     * in the range 0-24. 0 reresents off the board (the knocked location if there are pieces there, otherwise the off-board star location). A locationNumber of 1-24 refers to locations on the board with 1 being the first and 24 being the last.
     * @throws NoSuchLocationException if locationNumer is not in the range 0-24
     **/
    void setSourceLocation(int locationNumber) throws NoSuchLocationException;

    int getSourceLocation();

    /**
     *
     * @param diceValue represents the value of the dice to be used in the move 
     *
     * @throws IllegalMoveException if diceValue is not in the range 0-6 
     **/
    void setDiceValue(int diceValue) throws IllegalMoveException;

    int getDiceValue();
}
