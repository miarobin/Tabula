import java.util.List;
import java.util.Set;

/**
 * BoardInterface represents the board state in the game of tabula (not including dice and players).
 *
 * Requires a constructor with no parameters which creates and initialises all of the locations for the start of the game.
 *
 **/
public interface BoardInterface extends Cloneable{

    /**
     * note that you should not redefine this constant in the implementing class
     * but you can (and should) use it in the implementing class.
     */
    static final int PIECES_PER_PLAYER = 15;

    /**
     * note that you should not redefine this constant in the implementing class
     * but you can (and should) use it in the implementing class.
     */
    static final int NUMBER_OF_LOCATIONS = 24;

    public void setName(String name);

    /**
     * @return the Location off the board where all pieces start the game. This will be a mixed location.
     **/
    LocationInterface getStartLocation();

    /**
     * @return the Location off the board where pieces get to when they have gone all the way round the board. This will be a mixed location.
     **/
    LocationInterface getEndLocation();

    /**
     * @return the Location where pieces go to when they are knocked off the board by an opposing piece. This will be a mixed location.
     **/
    LocationInterface getKnockedLocation();

    /**
     * @return the Location corresponding to a numbered position on the board. This will not be a mixed location.
     *
     * @param locationNumber the number of the location going from 1-24
     *
     * @throws NoSuchLocationException when position is not in the range 1-24
     **/
    LocationInterface getBoardLocation(int locationNumber) throws NoSuchLocationException;

    /**
     * @param colour the colour to move
     *
     * @param move the move to make
     *
     * @return true if and only if, from the current board state it would be legal for the given colour to make the given move.
     **/
    boolean canMakeMove(Colour colour, MoveInterface move);

    /**
     * Update the Board state by making the given move for the given colour, including any knocking off.
     *
     * @param colour the colour to move
     *
     * @param move the move to make
     *
     * @throws IllegalMoveException if and only if the move is not legal.
     **/
    void makeMove(Colour colour, MoveInterface move) throws IllegalMoveException;

    /**
     * Update the Board state by making the all of the moves in the given turn in order, including any knocking off, based on the given diceValues.
     *
     * @param colour the colour to move
     *
     * @param turn the turn to take
     *
     * @param diceValues the values of the dice available in no particular order. There will be repeated values in the list if a double is thrown
     *
     * @throws IllegalTurnException if and only if the turns in the move are not legal for the diceValues give. Each of the moves has to be legal, and the diceValues in the moves of the turn must match the diceValues parameter. The number of moves in the turn must be no less than the maximum possible number of legal moves: all available dice must be used. If IllegalTurnException is thrown then the board state remains unchanged.
     **/
    void takeTurn(Colour colour, TurnInterface turn, List<Integer> diceValues) throws IllegalTurnException;

    /**
     * @param colour the colour to check
     *
     * @return true if and only if the given colour has won
     **/
    boolean isWinner(Colour colour);

    /**
     * @return the colour of the winner if there is one, otherwise null
     **/
    Colour winner();

    /**
     * @return true if and only if the Board is in a valid state (do not need to check whether or not it could be reached by a valid sequence of moves)
     **/
    boolean isValid();

    /**
     * @param colour the colour to move next
     *
     * @param diceValues the dice values available to use
     *
     * @return a list of moves that the given colour can make from the current board state with (any one of) the given diceValues
     **/
    Set<MoveInterface> possibleMoves(Colour colour, List<Integer> diceValues);

    /**
     * @return a copy of the board that can be passed to players to work with
     */
    BoardInterface clone();

    /**
     * Overrides toString() from Object with a suitable String representation of the board state for displaying via the console to a human
     **/
    String toString();
}
