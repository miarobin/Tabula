import java.util.Random;
public class Die implements DieInterface
{
	private static Random random = new Random();
	private boolean hasRolled;
	private int dieValue;


	public Die()
	{
		hasRolled = false;
		dieValue = 0;
	}

	//Has the die been rolled?
	public boolean hasRolled()
	{
		if(hasRolled)
		{
			return true;
		}
		else
			return false;
	}

	public void roll()
	{
		dieValue = random.nextInt(NUMBER_OF_SIDES_ON_DIE) + 1;
		hasRolled = true;
	}

	public int getValue() throws NotRolledYetException
	{
		if(hasRolled)
			return dieValue;
		else
			throw new NotRolledYetException("Dice must be rolled.");
	}

	public void setValue(int value)
	{
		dieValue = value;
	}

	public void clear()
	{
		dieValue = 0;
		hasRolled = false;
	}

	public void setSeed(long seed)
	{
		random.setSeed(seed);
	}
}
