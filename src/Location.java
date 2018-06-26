import java.util.HashMap;
public class Location implements LocationInterface, Cloneable, java.io.Serializable
{
	private String locationName;
	private HashMap<Colour, Integer> noOfPieces;
	private boolean isMixed;

	public Location(String nameOfLocation)
	{
		locationName = nameOfLocation;
		noOfPieces = new HashMap<Colour, Integer>();
		isMixed = false;
		for(Colour colour:Colour.values())
		{
			noOfPieces.put(colour, 0);
		}
	}

	public String getName()
	{
		return locationName;
	}

	public void setName(String nameOfLocation)
	{
		locationName = nameOfLocation;
	}

	public boolean isMixed()
	{
		return isMixed;
	}

	public void setMixed(boolean isMixed)
	{
		this.isMixed = isMixed;
	}

	public boolean isEmpty()
	{
		for(Integer number : noOfPieces.values())
		{
			if(number>0)
			{
				return false;
			}
		}
		return true;
	}

	public int numberOfPieces(Colour colour)
	{
		return noOfPieces.get(colour);
	}

	public boolean canAddPiece(Colour colour)
	{
		if(isMixed)
			return true;
		else if(noOfPieces.get(colour.otherColour())>1)
		{
			return false;
		}
		return true;
	}

	public Colour addPieceGetKnocked(Colour colour) throws IllegalMoveException
	{
		if(canAddPiece(colour))
		{
			noOfPieces.put(colour, (noOfPieces.get(colour) + 1));

			if(isMixed())
			{
				return null;
			}
			else if(canRemovePiece(colour.otherColour()) && noOfPieces.get(colour.otherColour()) == 1)
			{
				removePiece(colour.otherColour());
				return colour.otherColour();
			}
			else if(noOfPieces.get(colour.otherColour()) == 0)
			{
				return null;
			}
			else
			{
				throw new IllegalMoveException("Trying to knock of a piece with more than one colour present.");
			}
		}
		else
		{
			throw new IllegalMoveException("Cannot add a piece of colour" + colour + "to location");
		}
	}

	public boolean canRemovePiece(Colour colour)
	{
		if(noOfPieces.get(colour) == 0)
			return false;
		else if(noOfPieces.get(colour)>0)
			return true;
		else
			return false;
	}


	public void removePiece(Colour colour) throws IllegalMoveException
	{
		if(canRemovePiece(colour))
		{
			noOfPieces.put(colour, noOfPieces.get(colour) - 1);
		}
		else
			throw new IllegalMoveException("Cannot remove piece from location.");
	}

	public boolean isValid()
	{
		if(isMixed)
		{
			return true;
		}
		else
		{
			int moreThanOnePiece = 0;
			for(Colour colour:noOfPieces.keySet())
			{
				if(noOfPieces.get(colour)>1)
					moreThanOnePiece ++;
			}
			if(moreThanOnePiece>1)
				return false;
			else
				return true;
		}
	}

	public HashMap getNoOfPieces()
	{
		return noOfPieces;
	}

	@Override
	public Location clone()
	{
		try
		{
			Location locationCopy = (Location)super.clone();
			return locationCopy;
		}
		catch (CloneNotSupportedException exception)
		{
			System.out.println(exception);
		}
		return null;
	}
}
