import java.util.ArrayList;

public class Player {
    private int xPos;
    private int yPos;
    private int numItems;
    private ArrayList<Item> playerItems;
    private char name;
    private String direction;

    /**
     * Constructor for objects of class Player
     */
    public Player(int xPos, int yPos, char name) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        numItems = 0;
        playerItems = new ArrayList<Item>();
    }

    public void setPlayerPos(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void addItem(Item item) {
        playerItems.add(item);
        numItems++;
    }

    public void removeItem(Item item) {
        playerItems.remove(item);
        numItems--;
    }

    public ArrayList<Item> getItemList() {
        return playerItems;
    }

    public int getNumItems() {
        return numItems;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public char getName() {
        return name;
    }
}
