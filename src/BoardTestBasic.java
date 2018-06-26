import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.*;

public class BoardTestBasic {

    private BoardInterface b;
    private LocationInterface s, e, k, l1, l2;
    private MoveInterface m, n;
    private TurnInterface t;
    private Colour G,B;
    
    public BoardTestBasic(){

    }

    @Before
    public void setUp()
    {
	b = new Board();
	s = b.getStartLocation();
	e = b.getEndLocation();
	k = b.getKnockedLocation();
	G = Colour.GREEN;
	B = Colour.BLUE;
	m = new Move();
	n = new Move();
	t = new Turn();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void constructor_initialises_start(){
	assertEquals(15, s.numberOfPieces(B));
	assertEquals(15, s.numberOfPieces(G));
    }

    @Test
    public void constructor_initialises_empty_elsewhere() throws Exception{
	assertEquals(0, e.numberOfPieces(B));
	assertEquals(0, e.numberOfPieces(G));
	assertEquals(0, k.numberOfPieces(B));
	assertEquals(0, k.numberOfPieces(G));
	for(int i=1; i<=24; i++){
	    assertEquals(0, b.getBoardLocation(i).numberOfPieces(G));
	    assertEquals(0, b.getBoardLocation(i).numberOfPieces(B));
	}
    }

    @Test
    public void cloned_board_is_different() throws Exception{
	BoardInterface clone = b.clone();
	assertEquals(clone.getBoardLocation(1).numberOfPieces(G), 0);
	assertEquals(b.getBoardLocation(1).numberOfPieces(G), 0);
	clone.getBoardLocation(1).addPieceGetKnocked(G);
	assertEquals(clone.getBoardLocation(1).numberOfPieces(G), 1);
	assertEquals(b.getBoardLocation(1).numberOfPieces(G), 0);
    }
    
    @Test
    public void initial_move_from_start() throws Exception {
	m.setSourceLocation(0);
	m.setDiceValue(5);
	assert(b.canMakeMove(G, m));
    }
    
    @Test(expected=IllegalMoveException.class)
    public void no_initial_move_elsewhere() throws Exception {
	m.setSourceLocation(1);
	m.setDiceValue(5);
	b.makeMove(B, m);
    }
    
    @Test
    public void turn_initial() throws Exception {
	m.setSourceLocation(0);
	m.setDiceValue(5);
	n.setSourceLocation(5);
	n.setDiceValue(4);
	t.addMove(m);
	t.addMove(n);
	List<Integer> dice = new ArrayList<>();
	dice.add(4);
	dice.add(5);
	b.takeTurn(G, t, dice);
	assertEquals(b.getBoardLocation(9).numberOfPieces(G), 1);
    }
    
    @Test
    public void knock_after_initial() throws Exception {
	m.setSourceLocation(0);
	m.setDiceValue(5);
	b.makeMove(G,m);
	b.makeMove(B,m);
	assertEquals(k.numberOfPieces(G), 1);
	assertEquals(b.getBoardLocation(5).numberOfPieces(G), 0);
	assertEquals(b.getBoardLocation(5).numberOfPieces(B), 1);
    }
    
    @Test
    public void piece_can_finish() throws Exception {
	l1 = b.getBoardLocation(23);
	l1.addPieceGetKnocked(G);
	m.setSourceLocation(23);
	m.setDiceValue(4);
	b.makeMove(G,m);
	assertEquals(e.numberOfPieces(G), 1);
    }
    
    @Test
    public void can_win() throws Exception {
	for(int i = 0; i<15; i++){
	    e.addPieceGetKnocked(G);
	}
	assert(b.isWinner(G));
	assertEquals(b.winner(), G);
    }
    
}
