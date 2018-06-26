import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.util.Pair;
public class LoadBoard
{
  public LoadBoard()
  {
  }

  public Pair<BoardInterface, String[]> loadBoardFromFile(String filename)
  {
    ArrayList<String> linesOfText = new ArrayList<String>();
    ArrayList<String[]> gameData = new ArrayList<String[]>();

    try
		{
			FileReader fileReader = new FileReader((filename + ".txt"));
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
			while((line = reader.readLine())!=null)
			{
				if(!line.isEmpty())
					linesOfText.add(line);
			}
			reader.close();
			fileReader.close();
		}
		catch(FileNotFoundException unFoundException)
		{
			System.out.println(unFoundException.toString());
			System.out.println("The file was not found.");
		}
		catch(IOException iOException)
		{
			System.out.println(iOException.toString());
			System.out.println("Failed to load board.");
		}

    for(String line : linesOfText)
    {
      gameData.add(line.split(","));
    }

    String playerOneType = gameData.get(0)[0];
    gameData.remove(0);
    String playerTwoType = gameData.get(0)[0];
    gameData.remove(0);
    String turnCount = gameData.get(0)[0];
    gameData.remove(0);

    String[] boardData = new String[3];
    boardData[0] = playerOneType;
    boardData[1] = playerTwoType;
    boardData[2] = turnCount;
    //board.NUMBER_OF_LOCATIONS = Integer.parseInt(gameData.get(0)[0]);
    gameData.remove(0);
    //board.PIECES_PER_PLAYER = Integer.parseInt(gameData.get(0)[0]);
    gameData.remove(0);

    LocationInterface startLocation = stringToLocation(gameData.get(0));
    gameData.remove(0);
    LocationInterface knockedLocation = stringToLocation(gameData.get(0));
    gameData.remove(0);
    LocationInterface endLocation = stringToLocation(gameData.get(0));
    gameData.remove(0);

    ArrayList<LocationInterface> locationsArr = new ArrayList<LocationInterface>();

    int count = 0;
    for(String[] strungLocation : gameData)
    {
      LocationInterface location = stringToLocation(strungLocation);
      if(location != null)
      {
        locationsArr.add(stringToLocation(strungLocation));
        count++;
      }
      else
      {
        return null;
      }
    }

    LocationInterface[] locations = new Location[locationsArr.size()];
    for(int i=0; i<locationsArr.size(); i++)
    {
      locations[i]=locationsArr.get(i);
    }

    BoardInterface board = new Board(startLocation, endLocation, knockedLocation, locations);
    Pair<BoardInterface, String[]> pair = new Pair<BoardInterface, String[]>(board, boardData);
    return pair;
  }

  public LocationInterface stringToLocation(String[] strungLocation)
  {
    String locationName = strungLocation[0];
    LocationInterface location = new Location(locationName);

    String mixed = strungLocation[1];
    if(mixed.equals("true"))
    {
      location.setMixed(true);
    }

    String colourOneStr = strungLocation[2];
    int colourOneNo = Integer.parseInt(strungLocation[3]);
    String colourTwoStr = strungLocation[4];
    int colourTwoNo = Integer.parseInt(strungLocation[5]);
    Colour colourOne = Colour.valueOf(colourOneStr);
    Colour colourTwo = Colour.valueOf(colourTwoStr);
    try
    {
      for(int i=0; i<colourOneNo; i++)
      {
        location.addPieceGetKnocked(colourOne);
      }
      for(int i=0; i<colourTwoNo; i++)
      {
        location.addPieceGetKnocked(colourTwo);
      }
    }
    catch(IllegalMoveException exception)
    {
      System.out.println(exception);
      System.out.println("Board is invalid.");
      return null;
    }
    return location;
  }
}
