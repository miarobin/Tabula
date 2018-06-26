import java.util.ArrayList;
import java.util.List;
public final class FindTurns
{
    public static ArrayList<Turn> getPossibleTurns(List<Integer> dice, BoardInterface currentBoard, Colour colour)
    {
        ArrayList<Integer> diceValues = new ArrayList<Integer>();
        for(Integer die : dice)
        {
            diceValues.add(die);
        }
        ArrayList<Move> previousMoves = new ArrayList<Move>();
        BoardInterface testBoard = currentBoard.clone();
        ArrayList<Turn> possibleTurns = possibleTurns(diceValues, previousMoves, testBoard, colour);

        int numberNeededKnocked = currentBoard.getKnockedLocation().numberOfPieces(colour);
        int maxKnockedRemoved = 0;
        for(Turn turn : possibleTurns)
        {
            int knockedRemoved = 0;
            for(MoveInterface move : turn.getMoves())
            {
                if(move.getSourceLocation() == 0)
                knockedRemoved ++;
            }
            if(knockedRemoved > maxKnockedRemoved && knockedRemoved <= numberNeededKnocked)
            {
                maxKnockedRemoved = knockedRemoved;
            }
        }

        int maxMovesUsed = 0;
        ArrayList<Turn> turnsCorrectKnocked = new ArrayList<Turn>();
        for(Turn turn : possibleTurns)
        {
            int knockedRemoved = 0;
            for(MoveInterface move : turn.getMoves())
            {
                if(move.getSourceLocation() == 0)
                knockedRemoved ++;
            }
            if(knockedRemoved >= maxKnockedRemoved)
            {
                turnsCorrectKnocked.add(turn);
                if(turn.getMoves().size() > maxMovesUsed)
                maxMovesUsed = turn.getMoves().size();
            }
        }

        ArrayList<Turn> legalTurns = new ArrayList<Turn>();
        for(Turn turn : turnsCorrectKnocked)
        {
            if(turn.getMoves().size() == maxMovesUsed)
            legalTurns.add(turn);
        }

        for(Turn turn : legalTurns)
        {
            int numberKnocked = 0;
            for(MoveInterface move : turn.getMoves())
            {
                if(move.getSourceLocation() ==0)
                {
                    numberKnocked ++;
                }
            }
            if(numberKnocked < currentBoard.getKnockedLocation().numberOfPieces(colour))
            {
                legalTurns.remove(turn);
                Turn newTurn = new Turn();
                for(MoveInterface move : turn.getMoves())
                {
                    if(move.getSourceLocation() == 0)
                    {
                        try
                        {
                            newTurn.addMove(move);
                        }
                        catch(IllegalTurnException exception)
                        {
                            System.out.println("Adding too many moves to a turn in FindTurns class.");
                            System.out.println(exception);
                        }
                    }
                }
                legalTurns.add(newTurn);
            }
        }
        return legalTurns;
    }

    private static ArrayList<Turn> possibleTurns(ArrayList<Integer> dice, ArrayList<Move> previousMoves, BoardInterface currentBoard, Colour colour)
    {
        if(dice.isEmpty())
        {
            ArrayList<Turn> turns = new ArrayList<Turn>();
            Turn turn = new Turn();
            for(Move move : previousMoves)
            {
                try
                {
                    //System.out.println("Adding move. Die: " + move.getDiceValue() + " Source: " + move.getSourceLocation());
                    turn.addMove(move);
                }
                catch(IllegalTurnException exception)
                {
                    System.out.println("Too many moves added when finding turns.");
                    System.out.println(exception);
                }
            }
            turns.add(turn);
            return turns;
        }

        ArrayList<LocationInterface> allLocations = new ArrayList<LocationInterface>();
        for(int i=1; i<currentBoard.NUMBER_OF_LOCATIONS + 1; i++)
        {
            try
            {
                allLocations.add(currentBoard.getBoardLocation(i));
            }
            catch(NoSuchLocationException exception)
            {
                System.out.println(exception);
            }
        }
        allLocations.add(currentBoard.getStartLocation());
        allLocations.add(currentBoard.getEndLocation());

        ArrayList<Turn> runningTurns = new ArrayList<Turn>();

        int die = dice.get(0);
        ArrayList<Integer> remainingDice = new ArrayList<Integer>();
        for(int i = 1; i<dice.size(); i++)
        {
            remainingDice.add(dice.get(i));
        }

        for(LocationInterface location : allLocations)
        {
            if(!location.equals(currentBoard.getEndLocation()))
            {
                int newLocNo = Integer.parseInt(location.getName()) + die;
                try
                {
                    if(currentBoard.getBoardLocation(newLocNo).canAddPiece(colour) && location.canRemovePiece(colour))
                    {
                        Move move = new Move();
                        move.setSourceLocation(Integer.parseInt(location.getName()));
                        move.setDiceValue(die);
                        BoardInterface updatedBoard = currentBoard.clone();
                        if(updatedBoard.canMakeMove(colour, move))
                        {
                            updatedBoard.makeMove(colour, move);
                            ArrayList<Move> newPreviousMoves = new ArrayList<Move>();
                            for(Move prevMove : previousMoves)
                            {
                                newPreviousMoves.add(prevMove);
                            }
                            newPreviousMoves.add(move);
                            ArrayList<Turn> turns = possibleTurns(remainingDice, newPreviousMoves, updatedBoard, colour);

                            if(!turns.isEmpty())
                            {
                                runningTurns.addAll(turns);
                            }
                        }
                    }
                }
                catch(NoSuchLocationException exception)
                {
                    System.out.println(exception);
                    System.out.println("Location: " + location.getName());
                    System.out.println("New location number: " + newLocNo);
                }
                catch(IllegalMoveException exception)
                {
                    System.out.println(exception);
                }
            }
        }

        if(runningTurns.isEmpty())
        {
            ArrayList<Turn> turns = new ArrayList<Turn>();
            if(remainingDice.size()>1)
            {
                ArrayList<Turn> turnsNextDie = possibleTurns(remainingDice, previousMoves, currentBoard, colour);
                int count = 0;
                for(MoveInterface turnNextMove : turnsNextDie.get(0).getMoves())
                {
                    if(previousMoves.contains(turnNextMove));
                        count ++;
                }
                if(count > previousMoves.size() || turnsNextDie.size()>1)
                {
                    remainingDice.add(die);
                    turns = possibleTurns(remainingDice, previousMoves, currentBoard, colour);
                }
            }
            if(turns.isEmpty())
            {
                Turn turn = new Turn();
                for(Move move : previousMoves)
                {
                    try
                    {
                        turn.addMove(move);
                    }
                    catch(IllegalTurnException exception)
                    {
                        System.out.println(exception);
                    }
                }
                runningTurns.add(turn);
                return runningTurns;
            }
            else
            {
                return turns;
            }
        }
        else
        {
            return runningTurns;
        }
    }
}
