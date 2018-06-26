public class InvalidPlayerException extends java.lang.Exception
{
  private String comment;
  InvalidPlayerException(String comment)
  {
    this.comment = comment;
  }

  public String toString()
  {
    return comment;
  }
}
