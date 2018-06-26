import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.*;

public class DiceTestBasic {

    private Die d;
    private Dice ds;
    
    public DiceTestBasic(){

    }
    
    @Before
    public void setUp()
    {
	d = new Die();
	ds = new Dice();
    }

    @After
    public void tearDown()
    {
    }

    @Test(expected=NotRolledYetException.class)
    public void initially_die_not_rolled() throws Exception {
	d.getValue();
    }

    @Test(expected=NotRolledYetException.class)
    public void initially_dice_not_rolled() throws Exception {
	ds.getValues();
    }

    @Test
    public void set_seed_works_die() throws Exception{
	d.setSeed(1);
	d.roll();
	assertEquals(d.getValue(), 4);
	d.roll();
	assertEquals(d.getValue(), 5);
	d.roll();
	assertEquals(d.getValue(), 2);
    }

    @Test
    public void set_seed_works_dice() throws Exception{
	d.setSeed(1);
	ds.roll();
	assertEquals(ds.getValues().size(), 2);
	ds.roll();
	ds.roll();
	ds.roll();
	ds.roll();
	assertEquals(ds.getValues().size(), 4);
    }
}
