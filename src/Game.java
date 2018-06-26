import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import javafx.util.Pair;
public class Game implements GameInterface
{
  PlayerInterface playerOne;
  PlayerInterface playerTwo;
  String playerOneType;
  String playerTwoType;
  int turnCount;
  BoardInterface board;

  public Game()
  {
    playerOne = null;
    playerTwo = null;
    playerOneType = "";
    playerTwoType = "";
    turnCount = 0;
    board = new Board();
  }

  public static void main(String[] args)
  {
    Game game = new Game();
    System.out.println("Welcome to Tabula!");
    System.out.println("");
    boolean printMenu = true;
    while(printMenu)
    {
      printMenu = game.menu();
    }
    System.out.println("Bye.");
  }

  public boolean menu()
  {
    System.out.println("Please enter a number to select an option.");
    System.out.println("[1] Set the players.");
    System.out.println("[2] Start a new game.");
    System.out.println("[3] Load a game from a file.");
    System.out.println("If you are in the middle of a paused game...");
    System.out.println("[4] Continue the game.");
    System.out.println("[5] Save the game.");
    System.out.println("[6] Exit the program.");

    Scanner reader = new Scanner(System.in);
    int option = 0;
    boolean inputEntered = false;
    while(inputEntered == false)
    {
        try
        {
            option = reader.nextInt();
            if(option > 6 || option < 1)
            {
                System.out.println("Input out of range. Try again.");
                inputEntered = false;
            }
            else
            {
                inputEntered = true;
            }
        }
        catch(java.util.InputMismatchException exception)
        {
            System.out.println("Did not enter a number. Try again.");
            inputEntered = false;
        }
        reader.nextLine();
    }


    if(option == 1)
    {
      System.out.println("Enter 'h' for a human player, or 'c' for a computer player.");
      System.out.println("Select player one.");

      try
      {
        String playerOneTypeTry = reader.nextLine();
        PlayerInterface playerOneCreate = createPlayer(playerOneTypeTry);
        System.out.println("Select player two.");
        String playerTwoTypeTry = reader.nextLine();
        PlayerInterface playerTwoCreate = createPlayer(playerTwoTypeTry);
        setPlayer(Colour.GREEN, playerOneCreate);
        setPlayer(Colour.BLUE, playerTwoCreate);
        playerOneType = playerOneTypeTry;
        playerTwoType = playerTwoTypeTry;
      }
      catch(InvalidPlayerException exception)
      {
        System.out.println(exception);
        return true;
      }
      return true;
    }
    if(option == 2)
    {
      try
      {
          board = new Board();
          turnCount = 0;
          Colour winner = play();
          if(winner!= null)
          {
              System.out.println("Winner is: " + winner);
              System.out.println();
              System.out.println();
          }
      }
      catch(PlayerNotDefinedException exception)
      {
        System.out.println(exception);
      }
      return true;
    }
    if(option == 3)
    {
      System.out.println("Enter the filename. (.txt file type is added automatically)");
      String filename = reader.nextLine();
      try
      {
        loadGame(filename);
      }
      catch(IOException exception)
      {
        System.out.println(exception);
        System.out.println("No such file name or invalid file");
      }
      return true;
    }
    if(option == 4)
    {
      try
      {
        Colour winner = play();
        if(winner!= null)
        {
            System.out.println("Winner is: " + winner);
            System.out.println();
            System.out.println();
        }
      }
      catch(PlayerNotDefinedException exception)
      {
        System.out.println(exception);
      }
      return true;
    }
    if(option == 5)
    {
      System.out.println("Enter the filename you would like to save the file as. (.txt file type added automatically)");
      String filename = reader.nextLine();
      try
      {
        saveGame(filename);
      }
      catch(IOException exception)
      {
        System.out.println(exception);
        System.out.println("Invalid filename. May already be in use.");
      }
      return true;
    }
    if(option == 6)
    {
      System.out.println("Thanks for playing!");
      reader.close();
      return false;
    }
    else
    {
      System.out.println("Invalid number inputted. Please try again.");
      reader.nextLine();
      return true;
    }
  }

  public PlayerInterface createPlayer(String playerType) throws InvalidPlayerException
  {
    if(playerType.trim().equals("h"))
    {
      PlayerInterface humanPlayer = new HumanConsolePlayer();
      return humanPlayer;
    }
    else if(playerType.trim().equals("c"))
    {
      PlayerInterface computerPlayer = new ComputerPlayer();
      return computerPlayer;
    }
    else
    {
      throw new InvalidPlayerException("Failed to create player.");
    }
  }

  public Colour getCurrentPlayer()
  {
    if(turnCount%2 == 0)
    {
      return Colour.GREEN;
    }
    return Colour.BLUE;
  }

  public Colour play() throws PlayerNotDefinedException
  {
      if(playerOne != null && playerTwo != null)
      {
          boolean isPaused = false;
          while(board.winner() == null && isPaused == false)
          {
              System.out.println("");
              System.out.println("Current board:");
              System.out.print(board.toString());
              System.out.println("");
              Colour currentPlayerCol = getCurrentPlayer();
              DiceInterface dice = new Dice();
              List<Integer> diceValues = null;
              dice.roll();
              try
              {
                  diceValues = dice.getValues();
              }
              catch(NotRolledYetException exception)
              {
                  System.out.println(exception);
                  System.out.println("Dice haven't rolled properly.");
              }
              TurnInterface turn = null;
              try
              {
                  System.out.println("");
                  System.out.println("It is " + currentPlayerCol.toString() + "'s turn.");
                  if(board.possibleMoves(currentPlayerCol, diceValues).size()>0)
                  {
                      if(turnCount %2 == 0)
                      {
                          turn = playerOne.getTurn(currentPlayerCol, board.clone(), diceValues);
                      }
                      else
                      {
                          turn = playerTwo.getTurn(currentPlayerCol, board.clone(), diceValues);
                      }
                      try
                      {
                          if(turn != null)
                          {
                              board.takeTurn(currentPlayerCol, turn, diceValues);
                              if(!board.isValid())
                              {
                                  System.out.println("Board is no longer valid. Other player wins");
                                  board = new Board();
                                  turnCount = 0;
                                  return currentPlayerCol.otherColour();
                              }
                          }
                          turnCount++;
                      }
                      catch(IllegalTurnException exception)
                      {
                          System.out.println(exception);
                          System.out.println("Invalid turn attempted. Player two wins!");
                          board = new Board();
                          turnCount = 0;
                          return currentPlayerCol.otherColour();
                      }
                  }
                  else
                  {
                      System.out.println("No possible moves available with dice: ");
                      for(Integer die : diceValues)
                      {
                          System.out.print(die + " ");
                      }
                      turnCount ++;
                  }
              }
              catch (PauseException exception)
              {
                  isPaused = true;
              }
          }
          if(isPaused == true)
          {
              return null;
          }
          else
          {
              board = new Board();
              turnCount = 0;
              return board.winner();
          }
      }
      else
      {
          throw new PlayerNotDefinedException("Define both players.");
      }
  }

  public void saveGame(java.lang.String filename) throws java.io.IOException
  {
    SaveBoard saveBoard = new SaveBoard();
    saveBoard.saveBoardToFile(board, filename, playerOneType, playerTwoType, turnCount);
  }

  public void loadGame(String fileName) throws java.io.IOException
  {
    LoadBoard loadBoard = new LoadBoard();
    Pair<BoardInterface, String[]> pair = loadBoard.loadBoardFromFile(fileName);
    board = pair.getKey();
    playerOneType = pair.getValue()[0];
    playerTwoType = pair.getValue()[1];
    turnCount = Integer.parseInt(pair.getValue()[2]);
    if(board!= null)
    {
      try
      {
        PlayerInterface playerOneCreate = createPlayer(playerOneType);
        PlayerInterface playerTwoCreate = createPlayer(playerTwoType);
        playerOne = playerOneCreate;
        playerTwo = playerTwoCreate;
      }
      catch(InvalidPlayerException exception)
      {
        System.out.println(exception);
        System.out.println("Invalid board. Try another or create a new one.");
      }

      setPlayer(Colour.GREEN, playerOne);
      setPlayer(Colour.BLUE, playerTwo);
    }
    else
    {
      System.out.println("Try a different board.");
    }
  }

  public void setPlayer(Colour colour, PlayerInterface player)
  {
    if(colour.equals(Colour.GREEN))
    {
      playerOne = player;
      System.out.println("Player set");
    }
    else if(colour.equals(Colour.BLUE))
    {
      playerTwo = player;
      System.out.println("Player set");
    }
    else
    {
      System.out.println("Failed to set player.");
    }
  }
}
