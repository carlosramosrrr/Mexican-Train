import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Console version that manages the mexican Train game
 * @author Carlos ramos Guereca
 */
public class Console {
    private static int numberOfPlayers;
    private static int numberOfComputers;
    protected static int turn =0;
    private static boolean mexicanTrainPicked = false;
    private static boolean running = true;

    /**
     * Prints the current State of the Game with the board and the
     */
    private static void gameState(){
        System.out.println("Humans");
        for(int i =0; i< numberOfPlayers+ numberOfComputers; i++) {
            if(Controller.players().get(i).toString().contains("Computer")){
                System.out.println("Computers");
            }
            System.out.println(Controller.players().get(i).toString() +": " + Controller.playersHand(i));
        }
        System.out.println("Center:"+Controller.getCenter());
        System.out.println("Board:");
        //Print Trains with 2 lines
        String tempString = "";
        for(int i =0; i< numberOfPlayers + numberOfComputers; i++) {
            System.out.print(Controller.players().get(i).toString() +"("+
                    Controller.playersOpen(i) +"): ");
            for(int j =0; j < Controller.playersTrain(i).size();j++) {
                if(j%2 ==0){
                    System.out.print(Controller.playersTrain(i).get(j));
                }
                else{
                    tempString += Controller.playersTrain(i).get(j);
                }
            }
            System.out.println("");
            System.out.println("                   "+ tempString);
            tempString = "";
        }
        System.out.println("MexicanTrain(open): "+Controller.mexicanTrain().toString());
        System.out.println("Boneyard:"+ Controller.getSet().getDominos());
    }

    /**
     * Round end actions + messages
     * @param
     * @return
     */
    private static void roundEnd(){
        System.out.println("No player made a move round has ended");
        System.out.println("The Winner is " + Controller.checkWinner().toString());
        running = false;
        System.out.println("Another Round? Yes or No?");
        Scanner innScanner = new Scanner(System.in);
        String contrinue = innScanner.next();
        if(flipCheck(contrinue)){
            Controller.clear();
           // Controller.initialize(numberOfPlayers,numberOfComputers);
            running = true;
        }
        else {
            System.out.println("Game has ended ");
            running = false;
        }
    }

    /**
     * Checks to seen if the player choose to flip the domino
     * @param given
     * @return
     */
    private static boolean flipCheck(String given){
        if(given.equals("Yes")){
            return true;
        }
        else if(given.equals("No")){
            return false;
        }
        else {
            System.out.println("Not a valid input");
            playersTurn();
        }
        return false;
    }

    /**
     * Determines the players move depending on the input of the player
     * Determines to see if its trying to play its own train or other players
     * trains or the mexicanTrain
     */
    private static void placeAction(){
        boolean valid = false;
        while(!valid){
            System.out.println(Controller.players().get(turn)+ "'s Turn");
            System.out.println("[p] play domino");
            System.out.println("[d] draw from boneyard");
            System.out.println("[q] quit");
            Scanner inScanner = new Scanner (System.in);
            char pickedMove = inScanner.next().charAt(0);
            // Place domino on a Train
            if(pickedMove == 'p'){
                // Check domino index given by player
                System.out.println("Which domino? (Please enter index of your own hand)");
                int pickedDomino = inScanner.nextInt();
                if(!Controller.checkDominoIndex(pickedDomino, (Player) Controller.players().get(turn))) {
                    System.out.println("Too large try from [0, #Dominos - 1]");
                }
                // Check a train given by current player
                System.out.println("Which Train? (Format: Player1, Computer3,MexicanTrain)");
                String pickedTrain = inScanner.next();
                Player playerPicked = Controller.getStringToPlayer(pickedTrain);
                boolean validTrain = false;
                int pickedTrainIndex = 999;
                // Check to see if pickedTrain is a valid one.(start) -----------
                for(int i = 0; i < Controller.players().size(); i ++){
                    if(Controller.players().get(i).toString().equals(pickedTrain)){

                        pickedTrainIndex = i;
                    }
                }
                if(Controller.mexicanTrainOpen(pickedTrain)){
                      mexicanTrainPicked = true;

                }
                if(validTrain == false){
                    //System.out.println("Not a valid train");
                }
                // End check for valid train(end) -----------
                // Flip Domino at that given index.
                System.out.println("Flip?");
                String pickedFlip = inScanner.next();
                if(flipCheck(pickedFlip)){
                    Controller.flipDomino((Player) Controller.players().get(turn),pickedDomino );
                }
                // Picked the mexican Train
                if(Controller.mexicanTrainOpen(pickedTrain)){
                    System.out.println("Im able to play on the Mexican Train");
                    // If its valid it will add the Domino to the Mexican Train
                    if(Controller.checkValidMove((Domino)((Player) Controller.players().get(turn)).getHand().get(pickedDomino),
                            Controller.mexicanTrain())){
                        System.out.println("Added to said Train");
                        valid = true;
                    }
                    else {
                        System.out.println("Not a valid move Please try again-");
                    }
                }
                // Picked own players train during turn
                else if(Controller.players().get(turn).equals(playerPicked)){
                    if(Controller.checkValidMove(playerPicked,turn,pickedDomino,playerPicked.getTrain())){
                        System.out.println("Added to own Train");
                        ((Player) Controller.players().get(turn)).setOpen(false);
                        valid = true;
                    }
                }
                // Picked another player
                else if(Controller.playersTrainOpen(pickedTrain) == true){
                    System.out.println("Im able to place a domino in that players"+ pickedTrainIndex +" train");
                    if(Controller.checkValidMove((Player)Controller.players().get(turn),turn,pickedDomino, playerPicked.getTrain())){
                        System.out.println("Added to player's Train");
                    }
                    else {
                        System.out.println("Not a valid move try again--");
                    }
                }
                else{
                    System.out.println("Train is not open");

                }
            }
            // Draw Domino from boneyard
            else if (pickedMove == 'd'){
                if(Controller.getSet().getCurrentSize()!= 0) {
                    Controller.moveDraw((Player) Controller.players().get(turn));
                    ((Player) Controller.players().get(turn)).setOpen(true);
                    gameState();
                    valid =true;
                }
                else{
                    System.out.println("BoneYard  Is Empty end game approaching try another move");
                    System.out.println("[t] Try again");
                    System.out.println("[s] Skip turn");
                    pickedMove = inScanner.next().charAt(0);
                    if(pickedMove == 't'){
                        playersTurn();
                    }
                    else if(pickedMove == 's'){

                        Controller.getPlayersSkip().set(turn, 1);
                        if(Controller.checkEndGame()){
                            roundEnd();
                        }
                        valid = true;
                    }
                    else{
                        System.out.println("enter a valid value");
                    }
                    gameState();
                }
            }
            // Quit the game
            else if (pickedMove == 'q'){
                System.out.println("Game has ended ");
                running = false;
            }
            else {
                System.out.println("Please enter a valid value");
            }

        }
        }


    /**
     * Manages the moves of the current Player
     */
    private static void playersTurn(){

        if(Controller.players().get(turn).toString().contains("Computer")) {
            System.out.println(Controller.players().get(turn)+ "'s Turn");
            ((Computer)Controller.players().get(turn)).makeMove2((Computer)Controller.players().get(turn),turn);
        }
        else{
            placeAction();
        }
        if(Controller.checkEndGame()){
            roundEnd();
        }
         turn = Controller.turn(turn);
    }

    private static void setNumberOfPlayers(){
        System.out.println("Please enter the number of human players:");
        Scanner inScanner = new Scanner(System.in);
        numberOfPlayers = inScanner.nextInt();
        System.out.println("Please enter the number of computer players");
        numberOfComputers = inScanner.nextInt();
        if((numberOfComputers + numberOfPlayers) > 4){
            System.out.println("Sum of players cannnot be greater than 4. Try again");
            setNumberOfPlayers();
        }
    }
    /**
     * Runs the Console version of Mexican Train
     * @param args
     */
    public static void main(String[] args){
        System.out.println("Welcome to Mexican Train!");
        System.out.println("If you are not familiar with the rules please "+""+
                "browse to the following site: ");
        System.out.println(": https://www.mexicantrainrulesandstrategies.\n" +
                "com/.");
        System.out.println("Up to 4 players can play with any mix of "+"" +
                "human and computer players.");
        setNumberOfPlayers();
        System.out.println("GameState:");
        Controller.initialize(numberOfPlayers,numberOfComputers);
        while(running == true){
        gameState();
        playersTurn();
        }
    }

}
