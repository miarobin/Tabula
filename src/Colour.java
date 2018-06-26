public enum Colour{
    GREEN, BLUE;

    /**
     *
     * @return the other colour from this colour
     *
     **/
    public Colour otherColour(){
	switch(this){
	case GREEN:
	    return BLUE;

	case BLUE:
	    return GREEN;

	default:
	    // will not happen
	    return null;
	}
    }
}
