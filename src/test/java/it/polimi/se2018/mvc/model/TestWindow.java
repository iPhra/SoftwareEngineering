package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TestWindow {
    private String title;
    private int level;
    private final Square[][] matrix = new Square[4][5];
    private Window window;

    @Before
    public void init() {
        title = "Title";
        Random random = new Random();
        level = random.nextInt(7);
        for (int i = 0; i<4; i++) {
            for (int j = 0; j<5; j++) {
                Color color = Color.values()[random.nextInt(6)];
                int value = random.nextInt(6)+1;
                Coordinate coordinate = new Coordinate(i, j);
                Square square = new Square(color, value, coordinate, WindowBuilder.getValuePaths().get(value));
                matrix[i][j] = square;
            }
        }
        window = new Window(title, level, matrix,WindowBuilder.getLevelPaths().get(level));
    }

    @Test
    public void testGetTitle() {
        Assert.assertEquals(title, window.getTitle());
        Assert.assertNotEquals("Not title", window.getTitle());
    }

    @Test
    public void testGetLevel() {
        Assert.assertEquals(level, window.getLevel());
        Assert.assertNotEquals(level + 1, window.getLevel());
    }

    @Test
    public void testGetSquare() {
        Random random = new Random();
        Coordinate coordinate = new Coordinate(random.nextInt(4), random.nextInt(5));
        Assert.assertEquals(matrix[coordinate.getRow()][coordinate.getCol()], window.getSquare(coordinate));
        if (coordinate.getCol() < 3){
            Assert.assertNotEquals(matrix[coordinate.getRow()][coordinate.getCol() + 1], window.getSquare(coordinate));
        }
        else {
            Assert.assertNotEquals(matrix[coordinate.getRow()][coordinate.getCol() - 1], window.getSquare(coordinate));
        }
    }

    @Test
    public void testGetRows() {
        Assert.assertEquals(matrix.length, window.getRows());
    }

    @Test
    public void testGetCols() {
        Assert.assertEquals(matrix[0].length, window.getCols());
    }

    @Test
    public void testGetDie() {
        Random random = new Random();
        Color colorOne = Color.values()[random.nextInt(6)];
        int valueOne = random.nextInt(6)+1;
        Die dieOne = new Die(valueOne,colorOne);
        Color colorTwo = Color.values()[random.nextInt(6)];
        int valueTwo = random.nextInt(6)+1;
        Die dieTwo = new Die(valueTwo,colorTwo);
        Coordinate coordinate = new Coordinate(random.nextInt(4), random.nextInt(5));
        Assert.assertNotEquals(dieOne, window.getDie(coordinate));
        matrix[coordinate.getRow()][coordinate.getCol()].setDie(dieOne);
        Assert.assertEquals(dieOne, window.getDie(coordinate));
        Assert.assertNotEquals(dieTwo, window.getDie(coordinate));
    }

    @Test
    public void testModelViewCopy() {
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getCols(); j++) {
                Assert.assertEquals(window.modelViewCopy()[i][j], window.getSquare(new Coordinate(i, j)));
            }
        }
        Assert.assertNotEquals(window.modelViewCopy()[1][2], window.getSquare(new Coordinate(2, 3)));
    }

    @Test
    public void testCountEmptySlots() {
        int count = 0;
        Random random = new Random();
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getCols(); j++) {
                if (random.nextInt(4) < 3) {
                    Die die = new Die(random.nextInt(6) + 1, Color.values()[random.nextInt(6)]);
                    window.getSquare(new Coordinate(i, j)).setDie(die);
                }
            }
        }
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getCols(); j++) {
                if (window.getSquare(new Coordinate(i, j)).isEmpty()) {
                    count++;
                }
            }
        }
        Assert.assertEquals(count, window.countEmptySlots());
        Assert.assertNotEquals(count + 1, window.countEmptySlots());
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void testAdjacentDie() {
        List<Die> result;
        Random random = new Random();
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getCols(); j++) {
                if (random.nextInt(4) < 3) {
                    Die die = new Die(random.nextInt(6) + 1, Color.values()[random.nextInt(6)]);
                    window.getSquare(new Coordinate(i, j)).setDie(die);
                }
            }
        }
        result = window.adjacentDice(new Coordinate(2, 3));
        result = window.adjacentDice(new Coordinate(0, 0));
        result = window.adjacentDice(new Coordinate(window.getRows() - 1, window.getCols() - 1));
    }
}
