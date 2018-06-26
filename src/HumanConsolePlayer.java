import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class HumanConsolePlayer implements PlayerInterface
{
  public HumanConsolePlayer()
  {
  }

  public TurnInterface getTurn(Colour colour, BoardInterface board, List<Integer> diceValuesList) throws PauseException
  {
    BoardInterface boardClone = board.clone();
    BoardInterface testBoard = board.clone();
    Scanner input = new Scanner(System.in);

    System.out.println("If you would like to pause the game, press 'p' followed by the Enter key.");
    System.out.println("Else press the Enter key to take your turn.");
    String pause = pause();
    if(pause.trim().equals("p"))
    {
      throw new PauseException("Game Paused by Player.");
    }
    System.out.println();

    TurnInterface turn = new Turn();

    ArrayList<Integer> diceValues = new ArrayList<Integer>();
    for(int i=0;i<diceValuesList.size(); i++)
    {
      diceValues.add(diceValuesList.get(i));
    }

    while(diceValues.size()>0)
    {
      System.out.print(board.toString());
      System.out.println("Please enter a number to choose a move from the list.");
      System.out.println("Your dice values are: ");
      for(Integer diceValue : diceValues)
      {
        System.out.print(diceValue + " ");
      }
      System.out.println();
      ArrayList<MoveInterface> possibleMoves = new ArrayList<MoveInterface>();
      if(board.getKnockedLocation().numberOfPieces(colour)>0)
      {
        for(MoveInterface item : board.possibleMoves(colour, diceValues))
        {
          if(item.getSourceLocation() == 0)
            possibleMoves.add(item);
        }
      }
      else
      {
        for(MoveInterface item : board.possibleMoves(colour, diceValues))
        {
          possibleMoves.add(item);
        }
      }

      int i = 1;
      for(MoveInterface move : possibleMoves)
      {
        if((move.getSourceLocation() + move.getDiceValue())<=board.NUMBER_OF_LOCATIONS)
          System.out.println("[" + i + "] Move a piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()) + " with dice (" + move.getDiceValue()+ ")");
        else if ((move.getSourceLocation() + move.getDiceValue())>board.NUMBER_OF_LOCATIONS)
        {
          System.out.println("[" + i + "] Move a piece from " + move.getSourceLocation() + " to the end, with dice (" + move.getDiceValue()+ ")");
        }
        i++;
      }

      try
      {
          int inputNo = 0;
          boolean isInput = false;
          while(isInput == false)
          {
              try
              {
                  inputNo = input.nextInt();
                  if(inputNo < 1 || inputNo > i)
                  {
                      System.out.println("Input out of range. Try again");
                      isInput = false;
                  }
                  else
                  {
                      isInput = true;
                  }
              }
              catch(java.util.InputMismatchException exception)
              {
                  System.out.println("Did not enter a number. Try again.");
                  isInput = false;
              }
              input.nextLine();
          }
          try
          {
              MoveInterface move = possibleMoves.get(inputNo-1);
              turn.addMove(move);
              board.makeMove(colour, move);
              diceValues.remove(Integer.valueOf(move.getDiceValue()));
          }
          catch(IllegalTurnException exception)
          {
              System.out.println(exception);
              System.out.println("You've taken an invalid turn. The other player automatically wins.");
              return null;
          }
          catch(IllegalMoveException exception)
          {
              System.out.println(exception);
              System.out.println("You've taken an invalid move. The other player automatically wins.");
              return null;
          }
      }
      catch(ArrayIndexOutOfBoundsException exception)
      {
        System.out.println("Enter a valid number.");
      }
    }

    try
    {
      System.out.println("Trying turn...");
      testBoard.takeTurn(colour, turn, diceValuesList);
    }
    catch (IllegalTurnException exceptionTurn)
    {
      System.out.println(exceptionTurn);
      System.out.println("Try entering another turn.");
      try
      {
        return getTurn(colour, boardClone, diceValuesList);
      }
      catch (PauseException exceptionPause)
      {
        throw new PauseException("Player paused game.");
      }
    }
    System.out.println("Turn taken.");
    return turn;
  }

  public int readInput(int maxNum)
  {
    Scanner input = new Scanner(System.in);
    try
    {
      int actionNo = input.nextInt();
      if(actionNo>maxNum || actionNo <1)
      {
        System.out.println("Your number is out of range. Enter another.");
        return readInput(maxNum);
      }
      else
      {
        return actionNo;
      }
    }
    catch(java.util.InputMismatchException exception)
    {
        System.out.println("Did not input a number. Try again.");
        return readInput(maxNum);
    }
  }

  public String pause()
  {
    Scanner input = new Scanner(System.in);
    String action = input.nextLine();
    return action;
  }
}
