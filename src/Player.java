import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Player object that will maintain a collection of list
 * in order for them to have sets of dominos to manipulate
 * during the game
 * @author Carlos Ramos Guereca
 */
public class Player {
    private int id;
    private boolean open;

    private List<Domino> train;
    private List<Domino> hand;
    private List<Button>  buttonHand;
    private List<Button> buttonTrain;

    /**
     * Constructor for Player
     * @param train
     * @param hand
     * @param id
     * @param open
     */
    public Player(List<Domino> train, List<Domino> hand, int id, boolean open) {
        this.train = train;
        this.hand = hand;
        this.id = id;
        this.open = open;
        this.buttonHand = new ArrayList<>();
        this.buttonTrain = new ArrayList<>();
    }
    /**
     * gets the list of the players Train
     * @return
     */
    protected List getTrain() {
        return train;
    }

    /**
     * gets the hand of said player
     * @return
     */
    protected List getHand(){
        return hand;
    }

    /**
     * gets the Button hand of said player
     * @return
     */
    protected List getButtonHand(){
        return buttonHand;
    }

    /**
     * gets the button train of said player
     * @return
     */
    protected List getButtonTrain(){
        return buttonTrain;
    }

    /**
     * gets the id of said player
     * @return
     */
    protected int getId(){
        return id;
    }

    /**
     * gets the open status of said train
     * @return
     */
    protected boolean getOpen(){
        return open;
    }

    /**
     * sets the open status of said train
     * @param value
     */
    protected void setOpen(boolean value){
        if(value == true) {
            open = true;
        }
        else {
            open = false;
        }
    }

    /**
     * Returns a string of ex. Player1
     * @return
     */
    @Override
    public String toString(){
        return "Player"+getId();
    }
}
