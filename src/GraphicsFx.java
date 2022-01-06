import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * JavaFx for mexican Train
 * @author Carlos Ramos Guereca
 */
public class GraphicsFx extends Application {

    private GridPane gridTop = new GridPane();
    private GridPane gridMiddle = new GridPane();
    private GridPane gridBottom = new GridPane();
    private BorderPane gameWindow = new BorderPane();
    private Scene gameWindowScene = new Scene(gameWindow, 800,600);
    private VBox playingPlayersVBox = new VBox();

    private String dominoPlayed = "";

    private Button drawButton = new Button();
    private Button skipButton = new Button();
    private Button endGameButton = new Button();
    private Button flipButton = new Button();
    private Button computerTurn = new Button();

    private Label playerLabel = new Label();
    private Label playerHandLabel = new Label();
    private Label playerTrainLabel = new Label();

    private VBox playerInfo = new VBox();

    private int leftValue = 0;
    private int rightValue = 0;
    private int indexRemove = 0;
    private int indexPlayerPicked;
    private boolean running = true;
    private boolean flipCheck = false;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox introductionVBox = new VBox();
        Button startGameButton = new Button("Start");
        Label introLabel = new Label("Welcome to Mexican Train");
        introLabel.textProperty();
        introductionVBox.getChildren().addAll(introLabel,startGameButton);

        Label titlePlayersLabel = new Label("Insert Number of Players. Total Number of Players is 4");
        Label playersLabel = new Label("Number of Human Players?");
        Label computersLabel = new Label("Number of Computer Players?");
        playersLabel.textProperty();
        computersLabel.textProperty();


        // Create VBox for number of Players
        HBox submitPlayers = new HBox();
        HBox submitComputers = new HBox();
        VBox declarePlayersVBox = new VBox();

        // Create the Radio buttons for Players and Computers
        ToggleGroup playersInput = new ToggleGroup();
        RadioButton one = new RadioButton("1");
        RadioButton two = new RadioButton("2");
        RadioButton three = new RadioButton("3");
        RadioButton four = new RadioButton("4");
        one.setToggleGroup(playersInput);
        two.setToggleGroup(playersInput);
        three.setToggleGroup(playersInput);
        four.setToggleGroup(playersInput);

        ToggleGroup computersInput = new ToggleGroup();
        RadioButton oneC = new RadioButton("1");
        RadioButton twoC = new RadioButton("2");
        RadioButton threeC = new RadioButton("3");
        RadioButton fourC = new RadioButton("4");
        oneC.setToggleGroup(computersInput);
        twoC.setToggleGroup(computersInput);
        threeC.setToggleGroup(computersInput);
        fourC.setToggleGroup(computersInput);

        //Create submit Button Players button
        Button submitPlayersButton = new Button("Submit");

        // Add radio Buttons to VBox to get to GUI
        submitPlayers.getChildren().addAll(one,two,three,four);
        submitComputers.getChildren().addAll(oneC,twoC,threeC,fourC);
        declarePlayersVBox.getChildren().addAll(titlePlayersLabel,playersLabel,submitPlayers,
                computersLabel,submitComputers,submitPlayersButton);

        // Sets the Panes and the scenes
        BorderPane root = new BorderPane();
        BorderPane playersPane = new BorderPane();

        root.setCenter(introductionVBox);
        playersPane.setCenter(declarePlayersVBox);
        gameWindow.setLeft(playingPlayersVBox);
        Scene introScene = new Scene(root, 400, 400);
        Scene playersScene = new Scene(playersPane,400,400);
        primaryStage.setScene(introScene);
        primaryStage.show();
        // Intro to numberofplayers scene
        startGameButton.setOnAction(event -> {
        primaryStage.setScene(playersScene);

        });


        // Calculates the number of players and starts the game.
        submitPlayersButton.setOnAction(event ->{
            int sum;
            RadioButton selectedRadioButton = (RadioButton) playersInput.getSelectedToggle();
            String playersValue = selectedRadioButton.getText();
            int numberOfPlayers = Integer.parseInt(playersValue);

            RadioButton selectedRadioButton2 = (RadioButton) computersInput.getSelectedToggle();
            String computersValue = selectedRadioButton2.getText();
            int numberOfComputers = Integer.parseInt(computersValue);
            sum = numberOfComputers+ numberOfPlayers;

            if(sum <= 4){
                Controller.initialize(numberOfPlayers,numberOfComputers);
                buildBoard(playingPlayersVBox);
                primaryStage.setScene(gameWindowScene);
            }
        });

        drawButton.setOnAction(event->{
            if(Controller.getSet().getCurrentSize()!= 0){
                Controller.moveDraw(((Player)Controller.players().get(Controller.turn)));
            }
            else {
                Controller.getPlayersSkip().set(Controller.turn,1);
            }
            ((Player)Controller.players().get(Controller.turn)).setOpen(true);
            if(Controller.checkEndGame()){
                roundEnded(primaryStage, playingPlayersVBox);
            }
            Controller.turn =  Controller.turn(Controller.turn);
            update(playingPlayersVBox);
        });
        endGameButton.setOnAction(event -> {
            Pane endPane = new Pane();
            Label endGameLabel = new Label("Thank you for Playing");
            endPane.getChildren().add(endGameLabel);
            Scene endGame = new Scene(endPane, 500,500);
            primaryStage.setScene(endGame);
        });
        flipButton.setOnAction(event -> {
            flipCheck = true;
        });
        skipButton.setOnAction(event -> {
            Controller.getPlayersSkip().set(Controller.turn,1);
            if(Controller.checkEndGame()){
                roundEnded(primaryStage,playingPlayersVBox);
            }
            Controller.turn =  Controller.turn(Controller.turn);
            update(playingPlayersVBox);
        });
        computerTurn.setOnAction(event -> {
            Controller.turn =  Controller.turn(Controller.turn);
            update(playingPlayersVBox);
        });
    }
    /**
     * Displays the round ending page.
     * @param primaryStage
     * @param playingPlayersVBox
     */
    private void roundEnded(Stage primaryStage, VBox playingPlayersVBox){
        Label endGame = new Label("Round has ended. The winner is " + Controller.checkWinner());
        Pane endPane = new Pane();
        Button newRound  = new Button();
        newRound.setText("AnotherRound?");
        endPane.getChildren().addAll(endGame,newRound);
        Scene endScene = new Scene(endPane,400,400);
        primaryStage.setScene(endScene);
        primaryStage.show();
        newRound.setOnAction(event -> {
            Controller.clear();
            running = false;
            buildBoard(playingPlayersVBox);
            primaryStage.setScene(gameWindowScene);
            running = true;
        });

    }
    /**
    Updates the Box each turn because its creating the board depending on the
     list of the Dominos from Controller. Each new turn is going to be different so it
     Builds from the current stage
     */
    private void update(VBox playingPlayersVBox) {
        if(running) {
            playerInfo.getChildren().clear();
            Label infoTurn = new Label(Controller.players().get(Controller.turn).toString() + " turn");
            Label centerDomino = new Label("Center Domino: " + Controller.getCenter().toString());
            playerInfo.getChildren().add(centerDomino);
            playerInfo.getChildren().add(infoTurn);

            if (Controller.players().get(Controller.turn).toString().contains("Computer")) {
                ((Computer) Controller.players().get(Controller.turn)).makeMove2((Computer) Controller.players()
                        .get(Controller.turn), Controller.turn);
                for (int i = 0; i < Controller.players().size(); i++) {
                    playerLabel = new Label(Controller.players().get(i).toString());
                    playerHandLabel = new Label(((Player) Controller.players().get(i)).getHand().toString());
                    playerTrainLabel = new Label(((Player) Controller.players().get(i)).getTrain().toString());

                    playerInfo.getChildren().addAll(playerLabel, playerHandLabel, playerTrainLabel);
                }
                Controller.turn = Controller.turn(Controller.turn);
                update(playingPlayersVBox);

            } else {
                gridMiddle.getChildren().clear();
                for (int i = 0; i < Controller.players().size(); i++) {
                    playerLabel = new Label(Controller.players().get(i).toString());
                    playerHandLabel = new Label(((Player) Controller.players().get(i)).getHand().toString());
                    playerTrainLabel = new Label(((Player) Controller.players().get(i)).getTrain().toString());
                    Label playerOpen = new Label(String.valueOf(((Player) Controller.players().get(i)).getOpen()));

                    playerInfo.getChildren().addAll(playerLabel, playerHandLabel, playerTrainLabel, playerOpen);

                    Button playerNameButton = new Button(Controller.players().get(i).toString());
                    playerNameButton.setUserData(i);
                    gridMiddle.add(playerNameButton, 0, i);

                    playerNameButton.setOnAction(event -> {
                        indexPlayerPicked = (int) playerNameButton.getUserData();
                        if (flipCheck) {
                            Controller.flipDomino((Player) Controller.players().get(Controller.turn), indexRemove);
                        }
                        if (Controller.checkValidMove((Player) Controller.players().get(Controller.turn),
                                Controller.turn, indexRemove, ((Player) Controller.players().get(indexPlayerPicked)).getTrain())) {
                        }
                        Controller.getPlayersSkip().set(Controller.turn, 0);

                        ((Player) Controller.players().get(Controller.turn)).setOpen(false);
                        update(playingPlayersVBox);
                        Controller.turn = Controller.turn(Controller.turn);

                    });

                    for (int j = 0; j < ((Player) Controller.players().get(i)).getHand().size(); j++) {
                        Button playerButton = new Button();

                        playerButton.setText(String.valueOf(((Domino) ((Player) Controller.players().get(i)).getHand().get(j)).getLeftValue())
                                + "| " + String.valueOf(((Domino) ((Player) Controller.players().get(i)).getHand().get(j)).getRightValue()));

                        ((Player) Controller.players().get(i)).getButtonHand().add(playerButton);
                        gridMiddle.add(((Button) ((Player) Controller.players().get(i)).getButtonHand().get(j)), j + 1, i);

                        playerButton.setOnAction(event -> {
                            //Where the player places a domino on the other players train and checks if its a valid move
                            dominoPlayed = playerButton.getText();
                            dominoPlayed = dominoPlayed.replaceAll("[^0-9]", "");
                            // minus 48 because thats the anci of 0.
                            leftValue = Integer.valueOf(dominoPlayed.charAt(0) - 48);
                            rightValue = Integer.valueOf(dominoPlayed.charAt(1) - 48);
                            indexRemove = Controller.checkDominoIndexNumber(leftValue, rightValue);

                        });
                    }
                }
            }
            flipCheck = false;
        }
    }

    /**
     * Creates the Board depending on the amount of variables.
     * @param playingPlayersVBox
     */
    private void buildBoard(VBox playingPlayersVBox) {
        gridTop.getChildren().clear();
        gridMiddle.getChildren().clear();
        gridBottom.getChildren().clear();
        playingPlayersVBox.getChildren().clear();
        gridTop.setAlignment(Pos.TOP_CENTER);
        gridMiddle.setAlignment(Pos.TOP_CENTER);
        gridBottom.setAlignment(Pos.TOP_CENTER);

        int spacing = 10;
        gridBottom.setVgap(spacing);
        gameWindow.setTop(gridTop);
        gameWindow.setCenter(gridMiddle);
        gameWindow.setBottom(gridBottom);
        running = true;

        drawButton.setText("Draw");
        skipButton.setText("Skip");
        endGameButton.setText("End Game");
        flipButton.setText("Flip");
        computerTurn.setText("Computer's Turn");
        gridBottom.add(drawButton,0,0);
        gridBottom.add(flipButton,1,0);
        gridBottom.add(skipButton,2,0);
        gridBottom.add(endGameButton,3,0);
        gridBottom.add(computerTurn,4,0);

        update(playingPlayersVBox);
        playingPlayersVBox.getChildren().add(playerInfo);
    }
}
