import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
public class Board implements BoardInterface, Cloneable, java.io.Serializable
{
    private LocationInterface[] locations;
    private LocationInterface endLocation;
    private LocationInterface startLocation;
    private LocationInterface knockedLocation;
    private String boardName;

    public Board()
    {
        String boardName = "";
        locations = new Location[NUMBER_OF_LOCATIONS];
        endLocation = new Location("" + (NUMBER_OF_LOCATIONS + 1));
        endLocation.setMixed(true);
        startLocation = new Location("0");
        startLocation.setMixed(true);
        for(int i=0; i<PIECES_PER_PLAYER; i++)
        {
            for(Colour colour : Colour.values())
            {
                try
                {
                    startLocation.addPieceGetKnocked(colour);
                }
                catch(IllegalMoveException exception)
                {
                    System.out.println(exception);
                }
            }
        }

        knockedLocation = new Location("knockedLocation");
        knockedLocation.setMixed(true);

        for(int i=0; i<NUMBER_OF_LOCATIONS; i++)
        {
            locations[i] = new Location("" + (i + 1));
        }
    }

    public Board(LocationInterface startLocation, LocationInterface endLocation, LocationInterface knockedLocation, LocationInterface[] locations)
    {
        String boardName = "";
        this.locations = locations;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.knockedLocation = knockedLocation;
    }

    public boolean canMakeMove(Colour colour, MoveInterface move)
    {
        int finalLoc = move.getSourceLocation() + move.getDiceValue();
        if(knockedLocation.numberOfPieces(colour)>0)
        {
            if(move.getSourceLocation() == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        try
        {
            LocationInterface sourceLocation = getBoardLocation(move.getSourceLocation());
            if(sourceLocation.numberOfPieces(colour) == 0)
            {
                return false;
            }
            else if(finalLoc>NUMBER_OF_LOCATIONS)
            {
                return true;
            }
            else
            {
                try
                {
                    LocationInterface location = getBoardLocation(finalLoc);
                    return location.canAddPiece(colour);
                }
                catch(NoSuchLocationException exceptionFinalLoc)
                {
                    System.out.println(exceptionFinalLoc);
                    System.out.println("Trying to check move with an invalid final location in board class.");
                    return false;
                }
            }
        }
        catch(NoSuchLocationException exception)
        {
            System.out.println(exception);
            System.out.println("Trying to check move with an invalid initial location in board class.");
            return false;
        }
    }

    @Override
    public Board clone()
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);

            objectOutputStream.flush();
            objectOutputStream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInStream = new ObjectInputStream(inputStream);
            return (Board)objectInStream.readObject();
        }
        catch(IOException exception)
        {
            System.out.println(exception);
            return null;
        }
        catch(ClassNotFoundException exception)
        {
            System.out.println(exception);
            return null;
        }
    }

    public LocationInterface getBoardLocation(int locationNumber) throws NoSuchLocationException
    {
        if(locationNumber < 0)
        {
            throw new NoSuchLocationException("Location number out of range: " + locationNumber);
        }
        else if(locationNumber > NUMBER_OF_LOCATIONS)
        {
            return endLocation;
        }
        else if(locationNumber == 0)
        {
            return startLocation;
        }
        else
        {
            for(LocationInterface location : locations)
            {
                if(location.getName().equals("" + locationNumber))
                {
                    return location;
                }
            }
        }
        throw new NoSuchLocationException("Location does not exist on board.");
    }

    public LocationInterface getEndLocation()
    {
        return endLocation;
    }

    public LocationInterface getKnockedLocation()
    {
        return knockedLocation;
    }

    public LocationInterface getStartLocation()
    {
        return startLocation;
    }

    public boolean isValid()
    {
        boolean isValid = true;
        int noPieces = 0;
        for(Colour colour : Colour.values())
        {
            noPieces += startLocation.numberOfPieces(colour);
            noPieces += knockedLocation.numberOfPieces(colour);
            noPieces += endLocation.numberOfPieces(colour);

            if(!startLocation.isValid())
            {
                isValid = false;
            }
            if(!knockedLocation.isValid())
            {
                isValid = false;
            }
            if(!endLocation.isValid())
            {
                isValid = false;
            }
            for(LocationInterface location : locations)
            {
                noPieces += location.numberOfPieces(colour);
                if(!location.isValid())
                {
                    isValid = false;
                }
            }
        }
        if(noPieces != PIECES_PER_PLAYER*2)
        {
            isValid = false;
        }
        return isValid;
    }

    public boolean isWinner(Colour colour)
    {
        if(endLocation.numberOfPieces(colour) == PIECES_PER_PLAYER)
        {
            return true;
        }
        else
        return false;
    }

    public void makeMove(Colour colour, MoveInterface move) throws IllegalMoveException
    {
        if(!canMakeMove(colour, move))
        {
            throw new IllegalMoveException("Move not allowed");
        }
        int diceValue = move.getDiceValue();
        int finalLocNo = move.getDiceValue() + move. getSourceLocation();

        try
        {
            LocationInterface initialLoc = null;
            if(move.getSourceLocation() == 0)
            {
                if(knockedLocation.numberOfPieces(colour)>0)
                    initialLoc = knockedLocation;
                else
                    initialLoc = startLocation;
            }
            else
            {
                initialLoc = getBoardLocation(move.getSourceLocation());
            }
            LocationInterface finalLoc = getBoardLocation(finalLocNo);

            if(initialLoc.canRemovePiece(colour))
            {
                try
                {
                    Colour knockedColour = finalLoc.addPieceGetKnocked(colour);
                    try
                    {
                        initialLoc.removePiece(colour);
                    }
                    catch (IllegalMoveException exception)
                    {
                        System.out.println("Failed at trying to remove piece.");
                        System.out.println(exception);
                    }

                    if(knockedColour != null)
                    {
                        //System.out.println(knockedColour.toString() + " piece knocked off.");
                        knockedLocation.addPieceGetKnocked(knockedColour);
                    }
                }
                catch(IllegalMoveException exception)
                {
                    System.out.println(exception);
                    System.out.println("Cannot add piece to required destination");
                }
            }
            else
            {
                throw new IllegalMoveException("No pieces to remove.");
            }
        }
        catch(NoSuchLocationException exception)
        {
            System.out.println(exception);
            System.out.println("Move has invalid remove or add location number.");
        }


    }

    public HashSet<MoveInterface> possibleMoves(Colour colour, List<Integer> diceValues)
    {
        HashSet<Integer> set = new HashSet<Integer>(diceValues);
        boolean doubleRolled = false;
        if(set.size()<diceValues.size())
        {
            doubleRolled=true;
        }

        if(!doubleRolled)
        {
            HashSet<MoveInterface> moves = new HashSet<MoveInterface>();
            if(knockedLocation.numberOfPieces(colour)>0)
            {
                for(Integer diceValue : diceValues)
                {
                    try
                    {
                        if(getBoardLocation(diceValue).canAddPiece(colour))
                        {
                            MoveInterface possibleMove = createPossibleMove(colour, 0, diceValue);
                            if(possibleMove != null)
                            {
                                moves.add(possibleMove);
                            }
                        }
                    }
                    catch(NoSuchLocationException exception)
                    {
                        System.out.println(exception);
                        System.out.println("Trying to add piece to " + diceValue);
                    }
                }
            }
            else if(startLocation.numberOfPieces(colour)>0)
            {
                for(Integer diceValue : diceValues)
                {
                    MoveInterface possibleMove = createPossibleMove(colour, 0, diceValue);
                    if(possibleMove != null)
                    {
                        moves.add(possibleMove);
                    }
                }
            }

            int i = 1;
            for(LocationInterface location : locations)
            {
                if(location.numberOfPieces(colour)>0)
                {
                    for(Integer diceValue : diceValues)
                    {
                        MoveInterface possibleMove = createPossibleMove(colour, i, diceValue);
                        if(possibleMove != null)
                        {
                            moves.add(possibleMove);
                        }
                    }
                }
                i++;
            }
            return moves;
        }
        else if(doubleRolled)
        {
            HashSet<MoveInterface> moves = new HashSet<MoveInterface>();
            if(knockedLocation.numberOfPieces(colour)>0)
            {
                try
                {
                    if(getBoardLocation(diceValues.get(0)).canAddPiece(colour))
                    {
                        MoveInterface possibleMove = createPossibleMove(colour, 0, diceValues.get(0));
                        if(possibleMove != null)
                        {
                            moves.add(possibleMove);
                        }
                    }
                }
                catch(NoSuchLocationException exception)
                {
                    System.out.println(exception);
                    System.out.println("Trying to add piece to " + diceValues.get(0));
                }
            }
            else if(startLocation.numberOfPieces(colour)>0)
            {
                MoveInterface possibleMove = createPossibleMove(colour, 0, diceValues.get(0));
                if(possibleMove != null)
                {
                    moves.add(possibleMove);
                }
            }

            int i = 1;
            for(LocationInterface location : locations)
            {
                if(location.numberOfPieces(colour)>0)
                {
                    MoveInterface possibleMove = createPossibleMove(colour, i, diceValues.get(0));
                    if(possibleMove != null)
                    {
                        moves.add(possibleMove);
                    }
                }
                i++;
            }
            return moves;
        }
        else
        {
            System.out.println("Weird dice things occuring.");
            return null;
        }
    }

    public MoveInterface createPossibleMove(Colour colour, int sourceLocation, int diceValue)
    {
        MoveInterface possibleMove = new Move();
        try
        {
            possibleMove.setSourceLocation(sourceLocation);
            possibleMove.setDiceValue(diceValue);
            if(canMakeMove(colour, possibleMove))
            {
                return possibleMove;
            }
            else
            {
                return null;
            }
        }
        catch(NoSuchLocationException exception)
        {
            System.out.println(exception);
            System.out.println("Trying to make move with invalid source location");
            return null;
        }
        catch(IllegalMoveException exception)
        {
            System.out.println(exception);
            System.out.println("Dice value is out of range in board class");
            return null;
        }
    }



    public void setName(String name)
    {
        boardName = name;
    }

    public void takeTurn(Colour colour, TurnInterface turn, List<Integer> diceValues) throws IllegalTurnException
    {
        if(turn.getMoves().size() > diceValues.size())
            throw new IllegalTurnException("Too many moves for the number of dice.");

        Board board = clone();
        if(turn.getMoves().size() < FindTurns.getPossibleTurns(diceValues, board, colour).get(0).getMoves().size())
        {
            System.out.println("Number of moves that could be made:" + FindTurns.getPossibleTurns(diceValues, board, colour).get(0).getMoves().size());
            for(MoveInterface move : FindTurns.getPossibleTurns(diceValues, board, colour).get(0).getMoves())
            {
                System.out.println("Source: " + move.getSourceLocation());
                System.out.println("Die: " + move.getDiceValue());
            }
            throw new IllegalTurnException("Could take more moves with given dice.");
        }

        if(turn == null)
            throw new IllegalTurnException("Turn is null.");

        ArrayList<MoveInterface> usedMoves = new ArrayList<MoveInterface>();
        while(usedMoves.size() < turn.getMoves().size())
        {
            for(MoveInterface move : turn.getMoves())
            {
                try
                {
                    if(canMakeMove(colour, move) && !usedMoves.contains(move))
                    {
                        makeMove(colour, move);
                        usedMoves.add(move);
                    }
                }
                catch(IllegalMoveException exception)
                {
                    System.out.println("Could not make a move in 'takeTurn' method in board class");
                    System.out.println(exception);
                }
            }
        }
    }

    public String toString()
    {
        String string = "";
        //Putting everything into string format so it can be printed.
        Colour colourOne = Colour.values()[0];
        Colour colourTwo = Colour.values()[1];
        String colourOneStr = colourOne.toString().toUpperCase().substring(0, 1);
        String colourTwoStr = colourTwo.toString().toUpperCase().substring(0, 1);
        //First line: start location & knocked location
        string = ("             S        K             \n");
        string = (string + "            " + colourOneStr + "  " + colourTwoStr + "     " + colourOneStr + "  " + colourTwoStr + "           \n");
        String colourOneStartNo = "" + startLocation.numberOfPieces(colourOne);
        if(colourOneStartNo.length()==1)
            colourOneStartNo += " ";
        String colourTwoStartNo = "" + startLocation.numberOfPieces(colourTwo);
        if(colourTwoStartNo.length()==1)
            colourTwoStartNo += " ";
        String colourOneKnockNo = "" + knockedLocation.numberOfPieces(colourOne);
        if(colourOneKnockNo.length()==1)
        colourOneKnockNo += " ";
            String colourTwoKnockNo = "" + knockedLocation.numberOfPieces(colourTwo);
        if(colourTwoKnockNo.length()==1)
            colourTwoKnockNo += " ";

        string = (string + "            " + colourOneStartNo + " " + colourTwoStartNo + "    " + colourOneKnockNo + " " + colourTwoKnockNo + "           \n");

        String[] arrayPiecesOne = new String[NUMBER_OF_LOCATIONS];
        String[] arrayPiecesTwo = new String[NUMBER_OF_LOCATIONS];
        String[] locationName = new String[NUMBER_OF_LOCATIONS];

        int count = 0;
        for(LocationInterface location : locations)
        {
            if(location.numberOfPieces(colourOne)<10)
                arrayPiecesOne[count] = ("" + location.numberOfPieces(colourOne) + " ");
            else if(location.numberOfPieces(colourOne)>9)
                arrayPiecesOne[count] = ("" + location.numberOfPieces(colourOne));
            if(location.numberOfPieces(colourTwo)<10)
                arrayPiecesTwo[count] = ("" + location.numberOfPieces(colourTwo) + " ");
            else if(location.numberOfPieces(colourTwo)>9)
                arrayPiecesOne[count] = ("" + location.numberOfPieces(colourTwo));
            locationName[count] = (location.getName());
            count ++;
        }

        //Finding the number of rows needed.
        int noRows = 0;
        int modulus = NUMBER_OF_LOCATIONS%5;
        if(modulus<5)
            noRows = NUMBER_OF_LOCATIONS/5 +1;
        else
            noRows = NUMBER_OF_LOCATIONS/5;

        //Printing out the rows and columns. Has to be individually done for each line.
        int locationNo1 = 0;
        int locationNo2 = 0;
        int locationNo3 = 0;
        for(int i = 0; i<noRows; i++)
        {
            for(int j=0; j<5; j++)
            {
                string += "    ";
                if(locationNo1<NUMBER_OF_LOCATIONS)
                {
                    if(locationName[locationNo1].length()>1)
                        string = (string + "" + locationName[locationNo1] + " ");
                    else
                        string = (string + "" + locationName[locationNo1] + "  ");
                    locationNo1++;
                }
            }
            string += "\n";
            for(int j=0; j<5; j++)
            {
                if(locationNo2<NUMBER_OF_LOCATIONS)
                {
                    string += "   " + colourOneStr + "  " + colourTwoStr;
                    locationNo2++;
                }
            }
            string += "\n";
            string += " ";
            for(int j=0; j<5; j++)
            {
                if(locationNo3<NUMBER_OF_LOCATIONS)
                {
                    string += "  " + arrayPiecesOne[locationNo3] + " " + arrayPiecesTwo[locationNo3];
                    locationNo3++;
                }
            }
            string += "\n";
        }

        String colourOneFinalNo = "" + endLocation.numberOfPieces(colourOne);
        if(colourOneFinalNo.length()==1)
        {
            colourOneFinalNo += " ";
        }
        String colourTwoFinalNo = "" + endLocation.numberOfPieces(colourTwo);
        if(colourTwoFinalNo.length()==1)
        {
            colourTwoFinalNo += " ";
        }

        string += "                 END               \n";
        string += "                 " + colourOneStr + "  " + colourTwoStr + "            \n";
        string += "                 " + colourOneFinalNo + " " + colourTwoFinalNo + "            \n";

        return string;
    }

    public Colour winner()
    {
        for(Colour colour : Colour.values())
        {
            if(isWinner(colour))
            {
                return colour;
            }
        }
        return null;
    }
}
