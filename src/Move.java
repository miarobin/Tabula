
public class Move implements MoveInterface
{
  private int diceValue;
  private int sourceLocation;


  public Move()
  {
    diceValue = 0;
    sourceLocation = 0;
  }

  public int getDiceValue()
  {
    return diceValue;
  }

  public void setDiceValue(int diceValue) throws IllegalMoveException
  {
    if(diceValue<=Die.NUMBER_OF_SIDES_ON_DIE && diceValue>0)
    {
      this.diceValue = diceValue;
    }
    else
    {
      throw new IllegalMoveException("Die out of range");
    }
  }

  public int getSourceLocation()
  {
    return sourceLocation;
  }

  public void setSourceLocation(int locationNumber) throws NoSuchLocationException
  {
    if(locationNumber>=0 && locationNumber<=BoardInterface.NUMBER_OF_LOCATIONS)
    {
      sourceLocation=locationNumber;
    }
    else
    {
      throw new NoSuchLocationException("Location number out of range");
    }
  }
}
