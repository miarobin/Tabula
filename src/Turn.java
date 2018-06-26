import java.util.ArrayList;
public class Turn implements TurnInterface
{
  private ArrayList<MoveInterface> moves;

  public Turn()
  {
    moves = new ArrayList<MoveInterface>();
  }

  public void addMove(MoveInterface move) throws IllegalTurnException
  {
    if(moves.size()>4)
    {
      throw new IllegalTurnException("Too many moves in this turn");
    }
    else
      moves.add(move);
  }

  public ArrayList<MoveInterface> getMoves()
  {
    return moves;
  }

}
