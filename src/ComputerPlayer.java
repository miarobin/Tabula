import java.util.ArrayList;
import java.util.Set;
import java.util.List;
public class ComputerPlayer implements PlayerInterface
{

    public ComputerPlayer()
    {
    }

    public TurnInterface getTurn(Colour colour, BoardInterface board, java.util.List<java.lang.Integer> diceValuesList) throws PauseException
    {
        ArrayList<Move> prevMoves = new ArrayList<Move>();
        ArrayList<Turn> possibleTurnsComp = FindTurns.getPossibleTurns(diceValuesList, board, colour);

        System.out.println("The computer has dice:");
        for(Integer diceValue : diceValuesList)
        {
            System.out.print(diceValue + " ");
        }

        if(possibleTurnsComp.size() > 0)
        {
            Turn turnTaken = getOptimalTurn(possibleTurnsComp, colour, board);
            System.out.println("And is taking moves...");
            for(MoveInterface move : turnTaken.getMoves())
            {
                if(move.getDiceValue() + move.getSourceLocation() > board.NUMBER_OF_LOCATIONS)
                {
                    System.out.println("Moving from " + move.getSourceLocation() + " to the end using dice (" + move.getDiceValue() + ")");
                }
                else
                {
                    System.out.println("Moving from " + move.getSourceLocation() + " to " + (move.getDiceValue() + move.getSourceLocation()) + " using dice (" + move.getDiceValue() + ")");
                }
            }
            System.out.println("");
            return turnTaken;
        }
        else
        {
            System.out.println("Computer not taking a turn.");
            return null;
        }
    }

    public Turn getOptimalTurn(ArrayList<Turn> possibleTurns, Colour colour, BoardInterface currentBoard)
    {
        double maxWeight = 0;
        Turn maxWeightTurn = new Turn();
        for(Turn turn : possibleTurns)
        {
            BoardInterface boardTest = currentBoard.clone();
            for(MoveInterface move : turn.getMoves())
            {
                try
                {
                    boardTest.makeMove(colour, move);
                }
                catch(IllegalMoveException exception)
                {
                    System.out.println("Illegal move attempted at Computer.");
                    System.out.println(exception);
                }
            }

            double averagePiecePosition = averagePiecePosition(colour, boardTest);
            if(averagePiecePosition>maxWeight)
            {
                maxWeight = averagePiecePosition;
                maxWeightTurn = turn;
            }
        }

        return maxWeightTurn;
    }

    public double averagePiecePosition(Colour colour, BoardInterface board)
    {
        double weightedTotal = 0;
        for(int i=1; i<board.NUMBER_OF_LOCATIONS + 1; i++)
        {
            LocationInterface location = new Location("i");
            try
            {
                location = board.getBoardLocation(i);
            }
            catch(NoSuchLocationException exception)
            {
                System.out.println("No such location at computer");
                System.out.println(exception);
            }
            weightedTotal += Integer.parseInt(location.getName())*location.numberOfPieces(colour);
        }

        weightedTotal += board.getEndLocation().numberOfPieces(colour)*(board.NUMBER_OF_LOCATIONS+1);

        return weightedTotal/(double)(board.NUMBER_OF_LOCATIONS + 2);
    }
}
