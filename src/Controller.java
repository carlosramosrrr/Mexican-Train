import javax.swing.plaf.synth.SynthMenuBarUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages the game and has a bunch of help functions that help
 * manage the game for each system.
 * @author Carlos Ramos Guereca
 */
public class Controller {
    private static List<Player> players = new ArrayList<>();
    private static List<Domino> mexicanTrain = new ArrayList<>();
    private static List<Integer> playersSkip = new ArrayList();
    private static Domino center;
    protected static int numberOfPlayersGlobal;
    protected static int numberOfComputersGlobal;
    protected static int playerClicked;
    private static int decrementCenterBy = 1;
    private static int decrementCenterBySecond = 0;
    private static int doubleDomino = 9;
    private static Set set = new Set();
    private static int DECKSIZE = 10;
    protected static int turn;
    /**
     * Creates the players with numberOfPlayers
     * Uses 2 empty list to create the list for the
     * Hand and the Train.
     * @param numberOfPlayers
     */
    protected static void createPlayers(int numberOfPlayers){
        numberOfPlayersGlobal = numberOfPlayers;
        for(int i = 0; i < numberOfPlayers;i++) {
            List<Domino> empty = new ArrayList<>();
            List<Domino> empty2 = new ArrayList<>();
            players.add(new Player(empty, empty2, i,false));
            createHand(i);
            playersSkip.add(0);
        }
    }
    protected static void createComputers(int numberOfComputers){
        numberOfComputersGlobal = numberOfComputers;
        for(int i = 0; i < numberOfComputers;i++) {
            List<Domino> empty = new ArrayList<>();
            List<Domino> empty2 = new ArrayList<>();
            List<Domino> empty3 = new ArrayList<>();
            players.add(new Computer(empty, empty2, empty3,i,false));
            createHand(i + numberOfPlayersGlobal );
            playersSkip.add(0);
        }
    }

    /**
     * Creates the hand for each player depending on the decksize
     * @param index
     */
    protected static void createHand(int index){
        for (int j = 0; j < DECKSIZE; j++) {
            players.get(index).getHand().add(set.getDomino(0));
        }
    }
    /**
     * Initialize the board.
     * sets the center of the board.
     * Shuffles
     * Create the players.
     * @param numberOfPlayers
     * @param numberOfComputers
     */
    protected static void initialize(int numberOfPlayers, int numberOfComputers) {
       // System.out.println(set.getDominos());
        setCenter();
        set.shuffle();
        createPlayers(numberOfPlayers);
        createComputers(numberOfComputers);
      //  System.out.println(set.getDominos());
    }
    /**
     * Return the list of current players
     * @return
     */
    protected static List players(){
        return players;
    }

    /**
     * Returns the Mexican Train Open
     */
    protected static List mexicanTrain(){
        return mexicanTrain;
    }
    /**
     *
     * return the players hand at an index
     * @param index
     * @return
     */
    protected static List playersHand(int index){
        return players.get(index).getHand();
    }
    /**
     * return the players train at an index
     * @param index
     * @return
     */
    protected static List playersTrain(int index){
        return players.get(index).getTrain();
    }
    /**
     * return the state of the train whether its open or closed.
     * @param index
     * @return
     */
    protected static boolean playersOpen(int index){
        return players.get(index).getOpen();
    }
    /**
     * Checks the string value of a player to see if that
     * player has an open train.Calls playersOpen to confirm with
     * an index
     * @param value
     * @return
     */
    protected static boolean playersTrainOpen(String value){
        for(int i = 0; i < players().size(); i++){
            if ( players().get(i).toString().equals(value)){
                return playersOpen(i);
            }
        }
        return false;
    }
    /**
     * Mexican train check String for Console to see if its open
     */
    protected static boolean mexicanTrainOpen(String given){
        if(given.equals("MexicanTrain")){
            return  true;
        }
        else {
            return false;
        }
    }
    /**
     *
     * @param domino The Domino that is going to be placed in the board
     * @param givenList  The List we want to place that said domino into
     */
    protected static boolean checkValidMove(Domino domino, List givenList) {

        if(givenList.size()== 0){
            if(center.getRightValue() == domino.getLeftValue()){
                givenList.add(domino);

                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(((Domino)givenList.get(givenList.size() -1)).getRightValue() == domino.getLeftValue()){
                return true;
            }
            else return false;
        }
    }
    /**
     *Checks the valid move of the current Player targeting a list from another player.
     * @param
     * @param playerIndex
     * @param dominoIndex
     * @param targetList
     * @return
     */
    protected static boolean checkValidMove(Player currentPlayer,int playerIndex, int dominoIndex, List targetList) {

        Domino domino = (Domino)((Player) Controller.players().get(playerIndex)).getHand().get(dominoIndex);
        System.out.println(domino.toString());
        if(targetList.size()== 0){
            if(center.getRightValue() == domino.getLeftValue()){
                targetList.add(domino);
                currentPlayer.getHand().remove(dominoIndex);
                return true;
            }
            else{
                return false;
            }
        }
        else{
          //  System.out.println("Almost There");
            if(((Domino)targetList.get(targetList.size() -1)).getRightValue() == domino.getLeftValue()){
              //  System.out.println("In the check");
                targetList.add(currentPlayer.getHand().get(dominoIndex));
                currentPlayer.getHand().remove(dominoIndex);
                return true;
            }
            else return false;
        }
    }
    /**
     * Flips a domino returns it to that said player
     */
    protected static void flipDomino(Player player, int index) {
        Domino temp = new Domino(((Domino) player.getHand().get(index)).getRightValue(),
                ((Domino) player.getHand().get(index)).getLeftValue());
        player.getHand().remove(index);
        player.getHand().add(index, temp);
    }
    /**
     * Checks to see if the given index is going to be valid for said player
     */
    protected static boolean checkDominoIndex (int index, Player player){
        if(index < 0 ){
            return false;
        }
        else if(index > player.getHand().size()-1){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Checks the given index given two values of dominos. Will also change the global variable
     * that corresponds to from which players train was clicked.
     * @param leftValue
     * @param rightValue
     * @return
     */
    protected static int checkDominoIndexNumber(int leftValue, int rightValue){
        System.out.println("Left:" + leftValue + "right: "+rightValue);
        for(int i = 0; i < players().size(); i++){
            if(((Player)Controller.players().get(i)).getHand().size() > 0){
                for (int j = 0; j< ((Player)Controller.players().get(i)).getHand().size(); j++){
                    if(leftValue == ((Domino)((Player) Controller.players().get(i)).getHand().get(j)).getLeftValue() &&
                            rightValue == ((Domino)((Player) Controller.players().get(i)).getHand().get(j)).getRightValue()){
                        playerClicked = i;
                        return j;
                    }
                }
            }

        }
        return -1;
    }
    /**
     * Manages the turn of the players given a current player
     * @param turn
     * @return
     */
    protected static int turn(int turn){

        // Moves between turns
        if(turn == Controller.players().size()-1){
            turn = 0;
        }
        else{
            turn++;
        }
        return turn;
    }
    /**
     * Draw a random domino from the Boneyard for a given player.
     */
    protected static void moveDraw( Player player) {
        player.getHand().add(set.getDomino(0));
    }
    /**
     * Clears the board
     * Each player the train gets cleared as well as the hand
     * setOpen is set to false for all players
     * Sets up the board for next round as well.
     */
    protected static void clear() {
        mexicanTrain().clear();
        set.getDominos().clear();
        set = new Set();
        setCenter();
        for(int i=0; i < players().size();i++){
            playersSkip.set(i,0);
            players.get(i).getHand().clear();
            players.get(i).getTrain().clear();
            players.get(i).getButtonHand().clear();
            players.get(i).getButtonTrain().clear();
            players.get(i).setOpen(false);
            createHand(i);
        }

    }
    /**
     * sets the center domino in this case [9,9]
     */
    protected static void setCenter() {

        center = set.getDomino(set.getSetSize()-decrementCenterBy - decrementCenterBySecond);
        decrementCenterBySecond += decrementCenterBy;
        decrementCenterBy ++;
      //  System.out.println(decrementCenterBySecond + "Decrement Second "+ decrementCenterBy + " decrementCenterBt");
    }
    /**
     * gets the center domino
     * @return
     */
    protected static Domino getCenter(){
        return center;
    }
    protected static List getPlayersSkip(){
        return playersSkip;
    }
    /**
     * gets the set we are using
     * @return
     */
    protected static Set getSet(){
        return set;
    }
    /**
     * Given a string will return a player to which that players.toString() equals to.
     * @param given
     * @return
     */
    protected static Player getStringToPlayer(String given) {
        for(int i = 0; i < players().size(); i++){
            if(players().get(i).toString().equals(given)){
                return (Player)players().get(i);
            }
        }
        return null;
    }

    /**
     * Gives the winning player
     * Goes throught each player in the list.
     * Gets the hand and goes through each domino adding the result a list.
     * @return
     */
    protected static Player checkWinner(){
        Player tempPlayer = null;
        int compareSum = 10000000;
        int tempSum =0;
        for(int i=0; i < numberOfPlayersGlobal + numberOfComputersGlobal; i++){
            ((Player)players().get(i)).getHand().size();
            for(int j= 0; j< ((Player)players().get(i)).getHand().size();j++){
                tempSum = ((Domino)((Player)players().get(i)).getHand().get(j)).getLeftValue() +
                        ((Domino) ((Player)players().get(i)).getHand().get(j)).getRightValue();
            }
            if(tempSum < compareSum){
                compareSum = tempSum;
                tempPlayer = ((Player)players().get(i));
            }
        }
        return tempPlayer;
    }
    /**
     * Checks to see if the List of playersSkip is all set to 1
     * If this is the case then the round has ended.
     * @return
     */
    protected static boolean checkEndGame() {
        boolean temp = false;
        System.out.println(playersSkip);
        for(int i = 0; i < playersSkip.size(); i++){
            if(playersSkip.get(i).equals(1)){
                temp = false;
            }
            else {
                return false;
            }
        }
       return true;
    }
}
