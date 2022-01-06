import java.util.ArrayList;
import java.util.List;

/**
 * Computer player. Checks all possible moves and creates a list from them all.
 * After checking the list it will move depending on which move will
 * get rid of the most dots on a domino. Will then place that domino.
 * Extends Player class.
 * @author Carlos Ramos Guereca
 */
public class Computer extends Player{
    private List<Domino> possibleMoves;
    private List<Integer> possibleIndexes;
    private List<Integer> possiblePlayerIndex;
    private boolean firstTurn = true;
    private int winningIndex = 0;
    /**
     * Constructor for the Computer Player
     * @param train
     * @param hand
     * @param possibleMoves
     * @param id
     * @param open
     */
    public Computer(List<Domino> train, List<Domino> hand,List<Domino> possibleMoves, int id, boolean open) {
        super(train, hand, id, open);
        this.possibleMoves = possibleMoves;
        this.possibleIndexes = new ArrayList<>();
        this.possiblePlayerIndex = new ArrayList<>();
    }
    /**
     * When its the computers first turn it checks the center of the board first.
     * @param player
     * @param index
     */
    private void firstMove(Player player, int index){
        if(firstTurn == true){
            Domino temp = new Domino(-1,-1);
            int dominoIndex = -1;
            for(int i = 0; i < player.getHand().size(); i++){
                if(Controller.getCenter().getRightValue() == ((Domino)player.getHand().get(i)).getLeftValue()){
                    if(((Domino)player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                        temp = ((Domino) player.getHand().get(i));
                        dominoIndex = i;
                    }
                }
                else if( Controller.getCenter().getRightValue() == ((Domino)player.getHand().get(i)).getRightValue()){
                    Controller.flipDomino( player, i);
                    if(((Domino)player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                        temp = ((Domino) player.getHand().get(i));
                        dominoIndex = i;
                    }
                }
            }
            if(temp.getLeftValue() == -1 && temp.getRightValue() == -1 && Controller.getSet().getCurrentSize() > 0){
                drawComputer();
            }
            else{
                if(dominoIndex > -1) {
                    player.getTrain().add(temp);
                    player.getHand().remove(dominoIndex);
                    firstTurn = false;
                }
            }
        }
    }

    /**
     * Player is the player we are checking the combo for.
     * Index was the index of the location of the hand.
     * Domino is the domino we start checking first.
     * @param player
     * @param index
     * @param domino
     */
    private void checkCombo(Player player, int index, Domino domino){
        Domino temp = new Domino(-1,-1);
        int dominoIndex = -1;
        for(int i = 0; i < player.getHand().size(); i++){
            if(domino.getRightValue() ==  ((Domino)player.getHand().get(i)).getLeftValue()){
                if(((Domino)player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                    temp = ((Domino) player.getHand().get(i));
                    dominoIndex = i;
                }
            }
            else if(domino.getRightValue() == ((Domino)player.getHand().get(i)).getRightValue()){
                Controller.flipDomino( player, i);
                if(((Domino)player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                    temp = ((Domino) player.getHand().get(i));
                    dominoIndex = i;
                }
            }

        }
        if(temp.getLeftValue() == -1 || temp.getRightValue() == -1 || dominoIndex != -1){
           // System.out.println("No possible outcomes.");
        }
        else{
            possibleMoves.add(temp);
            possibleIndexes.add(dominoIndex);
            possiblePlayerIndex.add(Controller.players().indexOf(player));
        }
    }
    /**
     * Will Check the other players train for a potential Placement of a domino
     * Player is the Current computer player
     * @param player
     */
    private void checkOpponents(Player player){
        Domino temp = new Domino(-1,-1);
        Player confirmed = null;
        boolean found = false;
        int dominoIndex = 0;
        int playerIndex = 0;
        for(int i =0; i < Controller.players().size();i++){
            Player target = (Player)Controller.players().get(i);
            for(int j = 0; j < player.getHand().size();j ++){
              //  System.out.println("------------------------------------");
                  //  System.out.println(((Domino) target.getTrain().get(target.getTrain().size())).getRightValue());
               // System.out.println(((Domino) player.getHand().get(j)));
                if(target.getOpen() && target.getTrain().size() > 0) {
                    if (((Domino) target.getTrain().get(target.getTrain().size()-1)).getRightValue()
                            == ((Domino) player.getHand().get(j)).getLeftValue()) {
                     //   System.out.println("CHECK--------");
                        if (((Domino) player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                       //     System.out.println("CHECK2-------");
                            temp = ((Domino) player.getHand().get(j));

                            confirmed = target;
                            found = true;
                            dominoIndex = j;
                            playerIndex = i;
                        }
                    } else if (((Domino) target.getTrain().get(target.getTrain().size() - 1)).getRightValue()
                            == ((Domino) player.getHand().get(j)).getRightValue()) {
                        Controller.flipDomino(player, j);
                      //  System.out.println("CHECK3--------");
                        if (((Domino) player.getHand().get(i)).getLeftValue() > temp.getLeftValue()) {
                       //     System.out.println("CHECK4--------");
                            temp = ((Domino) player.getHand().get(j));

                            confirmed = target;
                            found = true;
                            dominoIndex = j;
                            playerIndex = i;
                        }
                    }
                }
                // Check when nonComputer players are empty.
                else{

                }
            }
        }
       /* if(temp.getLeftValue() == -1 || temp.getRightValue() == -1 || dominoIndex != -1){
            System.out.println("No possible outcomes.");
        }*/
        if(found){
          //  System.out.println("FINALLY IN---------------");
            System.out.println(temp);
            System.out.println(dominoIndex);
            possibleMoves.add(temp);
         //   confirmed.getTrain().add(temp);
            possibleIndexes.add(dominoIndex );
            possiblePlayerIndex.add(playerIndex);
         //   player.getHand().remove(dominoIndex);
        }
    }

    /**
     * Determines the largest domino from the list.
     * @return
     */
    private Domino largestDomino(){
        int tempSum = 0;
        int tempIndex = 0;
        for(int i = 0; i < possibleMoves.size(); i++){
            int sum = possibleMoves.get(i).getLeftValue() + possibleMoves.get(i).getRightValue();
            if(sum > tempSum){
                tempSum = sum;
                tempIndex = i;
            }
        }
        winningIndex = tempIndex;

        return possibleMoves.get(tempIndex);
    }

    /**
     * draws the the set if no possible moves
     */
    private void drawComputer() {
        if (Controller.getSet().getCurrentSize() != 0) {
            Controller.moveDraw((Player) Controller.players().get(Controller.turn));
            ((Player) Controller.players().get(Controller.turn)).setOpen(true);
        }
        // BoneYard is empty set skip to true for this Computer Player
        else{
                Controller.getPlayersSkip().set(Controller.turn, 1);
            }
    }
    /**
     * Runs the Computer Decisions
     * Computer is the current Computer we are playing with
     * Index is the index of the computer in the players() list.
     * @param player
     * @param index
     */
    protected void makeMove2( Computer player, int index){
        System.out.println(Controller.players().get(Controller.turn) + "Turn");
        if(player.getTrain().size() == 0){
            firstMove(player,index);
        }
        else{
            checkCombo(player,index,(Domino)player.getTrain().get(player.getTrain().size() - 1));
            checkOpponents(player);
            if(possibleMoves.size() == 0 ){

                drawComputer();
            }
            else{
                Domino winningDomino = largestDomino();
                ((Player)Controller.players().get(possiblePlayerIndex.get(winningIndex))).getTrain().add(winningDomino);
                player.getHand().remove(winningDomino);
                possibleMoves.clear();
                possibleIndexes.clear();
                possiblePlayerIndex.clear();
            }
        }
    }
    @Override
    public String toString() {
        return "Computer" + getId();
    }
}