/**
 * Creates an object of domnio
 * @author Carlos Ramos Guereca
 */
public class Domino {
    private final int leftValue;
    private  final int rightValue;

    /**
     * Constructor for Domino
     * @param leftValue
     * @param rightValue
     */
    public Domino( int leftValue, int rightValue){
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    /**
     * gets the left value of domino
     * @return
     */
    public int getLeftValue(){
        return leftValue;
    }

    /**
     * gets the right value of domino
     * @return
     */
    public int getRightValue(){
        return rightValue;
    }


    /**
     * returns a string of the domino in a format of
     * [ 1 | 1 ]
     * @return
     */
    @Override
    public String toString(){
        return "[ " + String.valueOf(leftValue) + "| " +
                String.valueOf(rightValue) + " ]";
    }
}
