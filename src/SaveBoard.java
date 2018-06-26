import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class SaveBoard
{
  public SaveBoard()
  {
  }

  public void saveBoardToFile(BoardInterface board, String filename, String playerOneType, String playerTwoType, int turnCount) throws IOException
  {
    FileWriter fileWriter = new FileWriter((filename + ".txt"));
    BufferedWriter writer = new BufferedWriter(fileWriter);

    try
    {
      writer.write(playerOneType);
      writer.newLine();
      writer.write(playerTwoType);
      writer.newLine();
      writer.write(("" + turnCount));
      writer.newLine();
      writer.write(("" + board.NUMBER_OF_LOCATIONS));
      writer.newLine();
      writer.write(("" + board.PIECES_PER_PLAYER));
      writer.newLine();

      writer.write(locationToString(board.getStartLocation()));
      writer.newLine();
      writer.write(locationToString(board.getKnockedLocation()));
      writer.newLine();
      writer.write(locationToString(board.getEndLocation()));
      writer.newLine();

      for(int i= 1; i<=board.NUMBER_OF_LOCATIONS; i++)
      {
        try
        {
          LocationInterface location = board.getBoardLocation(i);
          writer.write(locationToString(location));
          writer.newLine();
        }
        catch(NoSuchLocationException exception)
        {
          System.out.println(exception);
          System.out.println("Trying to get an invalid location while saving board.");
        }
      }

      writer.flush();
      System.out.println("Saved to file.");
    }
    catch(IOException exception)
    {
      System.out.println(exception);
      System.out.println("There was a problem saving the file.");
    }

    finally
    {
      try
      {
        if(writer != null)
        {
          writer.close();
        }
        if(fileWriter != null)
        {
          fileWriter.close();
        }
      }
      catch(IOException exception)
      {
        System.out.println(exception);
      }
    }
  }

  public String locationToString(LocationInterface location)
  {
    String locationStr = new String();
    String mixed = "";
    if(location.isMixed())
    {
      mixed = "true";
    }
    else
    {
      mixed = "false";
    }
    locationStr = location.getName() + "," + mixed + ",";
    for(Colour colour : Colour.values())
    {
      locationStr = locationStr + colour.toString() + "," + location.numberOfPieces(colour) + ",";
    }

    return locationStr;
  }
}
