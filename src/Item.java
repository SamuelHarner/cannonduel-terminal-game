public class Item {
    private int xPos = 0;
    private int yPos = 0;
    int fieldRowLimit;
    int fieldColLimit;
    private String direction;

    /**
     * Constructor for objects of class Item
     */
    public Item(int fieldRowLimit, int fieldColLimit) {
        this.fieldRowLimit = fieldRowLimit;
        this.fieldColLimit = fieldColLimit;
    }

    /**
     * Sets an item into action
     */
    public void useItem(int xPos, int yPos, String direction) {
        this.direction = direction;
        switch (direction) {
            case "up":
                this.yPos = yPos - 1;
                this.xPos = xPos;
                break;
            case "down":
                this.yPos = yPos + 1;
                this.xPos = xPos;
                break;
            case "left":
                this.xPos = xPos - 1;
                this.yPos = yPos;
                break;
            case "right":
                this.xPos = xPos + 1;
                this.yPos = yPos;
                break;
            default:
                break;
        }
    }

    public void drawItem(String[][] gameField) {
        gameField[yPos][xPos] = "*";
    }

    public void moveItem(char[][] gameField) {
        gameField[yPos][xPos] = ' '; // remove old position

        // Set new position
        setXPos(getNextXPos());
        setYPos(getNextYPos());

        gameField[yPos][xPos] = '*'; // add new position

    }

    public void setDirection(String dir) {
        direction = dir;
    }

    public String getDirection() {
        return direction;
    }

    public void setXPos(int x) {
        xPos = x;
    }

    public void setYPos(int y) {
        yPos = y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getNextXPos() {
        switch (direction) {
            case "up":
                return xPos;
            case "down":
                return xPos;
            case "left":
                return xPos - 1;
            case "right":
                return xPos + 1;
            default:
                return xPos;
        }
    }

    public int getNextYPos() {
        switch (direction) {
            case "up":
                return yPos - 1;
            case "down":
                return yPos + 1;
            case "left":
                return yPos;
            case "right":
                return yPos;
            default:
                return yPos;
        }
    }
}
