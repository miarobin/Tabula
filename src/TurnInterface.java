import java.util.List;

/**
 * TurnInterface represents a series of moves which constitute a turn by a player.
 *
 * Requires a constructor with no parameters.
 *
 **/

public interface TurnInterface {

    /**
     * @param move to be added after the moves already defined in the current turn
     *
     * @throws IllegalTurnException if there are already four or more moves in the turn
     */
    void addMove(MoveInterface move) throws IllegalTurnException;

    List<MoveInterface> getMoves();
}
