import java.util.*;

/**
 * DiceInterface represents a pair of dice as used in tabula.
 *
 * It should use the Die class so that all randomness comes from there
 *
 * Requires a constructor with no parameters. Initially the dice have no value until they are rolled
 **/

public interface DiceInterface {

    /**
     * @return true if and only if both of the dice have been rolled
     **/
    boolean haveRolled();

    /**
     * Roll both of the dice
     */
    void roll();

    /**
     * @return four numbers if there is a double, otherwise two
     *
     * @throws NotRolledYetException if either of the dice have not been rolled yet
     **/
    List<Integer> getValues() throws NotRolledYetException;

    
    /**
     * clear both of the dice so they have no value until they are rolled again
     **/
    void clear();

    /**
     * Get the individual dice in a list.
     *
     * @return the Die objects in a list, which will have length 2
     */
    List<DieInterface> getDice();

}
