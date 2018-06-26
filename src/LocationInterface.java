/**
 * LocationInterface represents a single location on the board, but not its position
 * Locations on the main part of the board may only contain a single colour of piece.
 *
 * Off-board locations, i.e. the starting location, the finishing location and the knocked-off location
 * can contain pieces of both colours and are referred to as mixed.
 *
 * Requires a constructor with one parameter (the name of the location), which creates a non-mixed location with no pieces.
 *
 */


public interface LocationInterface {

    String getName();
    void setName(String name);
    
    /**
     * @return true if and only if the location allows pieces of both colours
     */
    boolean isMixed();

    /**
     * @param isMixed true if and only if the location allows pieces of both colours
     */
    void setMixed(boolean isMixed);

    /**
     * @return true if and only if the location has no pieces in it
     */
    boolean isEmpty();

    /**
     * @param colour the colour of pieces to count
     * @return the number of pieces of that colour
     **/
    int numberOfPieces(Colour colour);

    /**
     * @param colour the colour of the piece to add
     * @return true if and only if a piece of that colour can be added (i.e. no IllegalMoveException)
     **/
    boolean canAddPiece(Colour colour);

    
    /**
     * @param colour the colour of the piece to add
     *
     * @throws IllegalMoveException if the location is not mixed and already contains two or more pieces 
     * of the other colour
     *
     * @return null if nothing has been knocked off, otherwise the colour of the piece that has been knocked off
     **/
    Colour addPieceGetKnocked(Colour colour) throws IllegalMoveException;

    /**
     * @param colour the colour of the piece to remove
     * @return true if and only if a piece of that colour can be removed (i.e. no IllegalMoveException)
     **/
    boolean canRemovePiece(Colour colour);
    
    /**
     * @param colour the colour of the piece to remove
     *
     * @throws IllegalMoveException if there are no pieces of that colout int the location
     *
     **/
    void removePiece(Colour colour) throws IllegalMoveException;

    /**
     * @return true if and only if the Location is in a valid state depending on the number of each colour and whether or not it is a mixed location
     */
    boolean isValid();
}
