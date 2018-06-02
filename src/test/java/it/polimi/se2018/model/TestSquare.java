package it.polimi.se2018.model;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TestSquare {
    private Square square;
    private Color color;
    private int value;
    private Coordinate coordinate;
    private Die die;

    @Before
    public void init() {
        Random random = new Random();
        color = Color.values()[random.nextInt(6)];
        value = random.nextInt(6)+1;
        coordinate = new Coordinate(random.nextInt((5)), random.nextInt(4));
        square = new Square(color, value, coordinate,WindowBuilder.getColorPaths().get(color.getAbbreviation()));
    }

    @Test
    public void testGetRow() {
        Assert.assertEquals(coordinate.getRow(), square.getRow());
        Assert.assertNotEquals(coordinate.getRow() - 1, square.getRow());
    }

    @Test
    public void testGetCol() {
        Assert.assertEquals(coordinate.getCol(), square.getCol());
        Assert.assertNotEquals(coordinate.getCol() - 1, square.getCol());
    }

    @Test
    public void testGetDie() {
        square.setDie(null);
        Assert.assertTrue(square.isEmpty());
        //Assert.assertEquals(null, square.getDie());
        Random random = new Random();
        Color colorDie = Color.values()[random.nextInt(6)];
        int valueDie = random.nextInt(6)+1;
        Die die = new Die(valueDie, colorDie);
        Assert.assertNotEquals(die, square.getDie());
        square.setDie(die);
        Assert.assertEquals(die, square.getDie());
        Assert.assertNotEquals(null, square.getDie());
    }

    @Test
    public void testSameColor() {
        Random random = new Random();
        int valueDie = random.nextInt(6)+1;
        color = Color.values()[random.nextInt(5)];
        square = new Square(color, value, coordinate,WindowBuilder.getColorPaths().get(color.getAbbreviation()));
        Die dieOne = new Die(valueDie, color);
        Die dieTwo;
        if (Color.fromColor(color) < 3) {
            Color colorDieTwo = Color.values()[Color.fromColor(color) + 1];
            dieTwo = new Die(valueDie, colorDieTwo);
        }
        else {
            Color colorDieTwo = Color.values()[Color.fromColor(color) - 1];
            dieTwo = new Die(valueDie, colorDieTwo);
        }
        Assert.assertTrue(square.sameColor(dieOne));
        Assert.assertFalse(square.sameColor(dieTwo));
        Square squareWhite = new Square(Color.WHITE, value, coordinate,WindowBuilder.getValuePaths().get(value));
        Assert.assertTrue(squareWhite.sameColor(dieOne));
    }

    @Test
    public void testSameValue() {
        Random random = new Random();
        Color colorDie = Color.values()[random.nextInt(6)];
        Die dieOne = new Die(value, colorDie);
        Die dieTwo;
        if (value < 3) {
            dieTwo = new Die(value + 1, colorDie);
        }
        else {
            dieTwo = new Die(value - 1, colorDie);
        }
        Assert.assertTrue(square.sameValue(dieOne));
        Assert.assertFalse(square.sameValue(dieTwo));
        Square squareNoValue = new Square(color, 0, coordinate,WindowBuilder.getColorPaths().get(color.getAbbreviation()));
        Assert.assertFalse(square.sameValue(dieTwo));
        Square squareZero = new Square(color, 0, coordinate, WindowBuilder.getColorPaths().get(color.getAbbreviation()));
        Assert.assertTrue(squareZero.sameValue(dieOne));
    }

    @Test
    public void testIsEmpty() {
        square.setDie(null);
        Assert.assertTrue(square.isEmpty());
        Random random = new Random();
        Color colorDie = Color.values()[random.nextInt(6)];
        int valueDie = random.nextInt(6)+1;
        Die die = new Die(valueDie, colorDie);
        square.setDie(die);
        Assert.assertFalse(square.isEmpty());
    }
}
