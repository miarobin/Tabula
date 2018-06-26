import java.util.Random;

/**
 * DieInterface represents a single die.
 *
 * Requires a constructor with no parameters. Initially the die has no value until it is rolled.
 *
 * A single static java.util.Random object should be the source of all randomisation.
 *
 **/

public interface DieInterface{

    /**
     * note that you should not redefine this constant in the implementing class
     * but you can use it in the implementing class. 
     */
    static final int NUMBER_OF_SIDES_ON_DIE = 6;


    /**
     * @return false when first constructed or cleared, then true once rolled (unless it is then cleared)
     **/
    boolean hasRolled();

    /**
     * rolls the die to give a it a value in the range 1-6
     */
    void roll();

    /**
     *
     * @return the visible face of the die, a value in the range 1-6
     *
     * @throws NotRolledYetException if the die has not been rolled or has been cleared since the last roll
     **/
    int getValue() throws NotRolledYetException;

    /**
     * set the face value of the die: only needed when recreating a game state
     *
     * @param value the new value of the die. If it is not in an acceptable range then afterwards hasRolled() should return false.
     **/
    void setValue(int value);
    
    /**
     * clears the die so it has no value until it is rolled again
     **/
    void clear();

    /**
     * sets the seed for the random number generator used by all dice
     *
     * @param seed the seed value to use for randomisation
     **/
    void setSeed(long seed);
}
