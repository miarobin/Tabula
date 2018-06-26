public class IllegalDiceException extends Exception
{
  private String comment;

  public IllegalDiceException(String comment)
  {
    this.comment = comment;
  }

  public String toString()
  {
    return "IllegalDiceException" + comment;
  }
}
