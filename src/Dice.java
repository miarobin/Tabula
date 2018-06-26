import java.util.ArrayList;

public class Dice implements DiceInterface
{
	private Die dieOne;
	private Die dieTwo;

	public Dice()
	{
		dieOne = new Die();
		dieTwo = new Die();
	}

	public boolean haveRolled()
	{
		if(dieOne.hasRolled() && dieTwo.hasRolled())
			return true;
		else
			return false;
	}

	public void roll()
	{
		dieOne.roll();
		dieTwo.roll();
	}

	public ArrayList<Integer> getValues() throws NotRolledYetException
	{
		if(dieOne.hasRolled() && dieTwo.hasRolled())
		{
			ArrayList<Integer> dieValues = new ArrayList<Integer>();
			if(dieOne.getValue() == dieTwo.getValue())
			{
				for(int i=0; i<4; i++)
				{
					dieValues.add(dieOne.getValue());
				}
			}
			else
			{
				dieValues.add(dieOne.getValue());
				dieValues.add(dieTwo.getValue());
			}
			return dieValues;
		}
		else
		{
			throw new NotRolledYetException("Please roll both dice.");
		}
	}

	public void clear()
	{
		dieOne.clear();
		dieTwo.clear();
	}

	public ArrayList<DieInterface> getDice()
	{
		ArrayList<DieInterface> dice = new ArrayList<DieInterface>();
		dice.add(dieOne);
		dice.add(dieTwo);

		return dice;
	}
}
