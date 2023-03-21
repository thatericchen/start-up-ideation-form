import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.CornerRadii;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GUI that takes in start-up problems and organizes them into a list.
 * @author Eric Chen
 * @version 1.0
 */
public class StarterUpper extends Application {

    private StackPane root = new StackPane();
    private BorderPane main = new BorderPane();
    private VBox questions = new VBox();
    private TextField problem = new TextField();
    private TextField targetCustomer = new TextField();
    private TextField customerNeed = new TextField();
    private TextField knownPeopleWithProblem = new TextField();
    private TextField targetMarketSize = new TextField();
    private TextField competitors = new TextField();
    private Text problemText = new Text("What is the problem?");
    private Text targetCustomerText = new Text("Who is the target customer?");
    private Text customerNeedText = new Text("How badly does the customer NEED this problem fixed (1-10)?");
    private Text knownPeopleWithProblemText =
            new Text("How many people do you know who might experience this problem?");
    private Text targetMarketSizeText = new Text("How big is the target market?");
    private Text competitorsText = new Text("Who are the competitors/existing solutions?");
    private VBox popUp = new VBox();
    private Label popUpText = new Label("Input fields are either incorrect or not complete.");
    private VBox popUp2 = new VBox();
    private Label popUp2Text = new Label("Reset the form?");
    private HBox popUp2Options = new HBox();
    private List<StartUpIdea> ideas = new ArrayList<>();
    private File file = new File("ideas.txt");

    /**
     * Main method that launches the GUI.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Problem Ideation Form.");
        Label label = new Label("Eric Chen");
        Button startBtn = new Button("Add Start-Up Idea");
        startBtn.setMaxHeight(100);
        startBtn.setMaxWidth(200);
        ImageView imageView = new ImageView("https://media.istockphoto.com/vectors/light-bulb-idea-lamp-outline-"
                + "icon-vector-vector-id960813498?k=20&m=960813498&s=612x612&w=0&h="
                + "QqqU63p6dPQrF6jAGhLxTWltcNEp1CijGqX2whgj3vo=");
        imageView.setFitHeight(800);
        imageView.setFitWidth(800);
        root.getChildren().add(imageView);
        root.getChildren().add(main);
        main.setCenter(startBtn);
        main.setBottom(new StackPane(label));
        startBtn.setOnAction(new MainHandler());
        stage.setScene(new Scene(root, 1500, 900));
        stage.show();
    }

    /**
     * Main handler class that has buttons to store start-up ideas into a list then a file.
     * @author Eric Chen
     * @version 1.0
     */
    private class MainHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            questions.setMaxHeight(300);
            questions.setMaxWidth(800);
            questions.setSpacing(5);

            Button storeBtn = new Button("Store Idea");
            storeBtn.setMaxWidth(180);
            Button sortBtn = new Button("Sort Ideas");
            sortBtn.setMaxWidth(180);
            Button resetBtn = new Button("Reset");
            resetBtn.setMaxWidth(180);
            Button saveBtn = new Button("Save to File");
            saveBtn.setMaxWidth(180);

            questions.getChildren().addAll(problemText, problem, targetCustomerText, targetCustomer, customerNeedText,
                    customerNeed, knownPeopleWithProblemText, knownPeopleWithProblem, targetMarketSizeText,
                    targetMarketSize, competitorsText, competitors, new StackPane(storeBtn), new StackPane(sortBtn),
                    new StackPane(resetBtn), new StackPane(saveBtn));
            main.setCenter(questions);

            popUpText.setMinHeight(50);
            popUp.setAlignment(Pos.CENTER);
            Button close = new Button("Close");
            close.setOnAction(this::close);
            popUp.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            popUp.getChildren().addAll(popUpText, close);

            storeBtn.setOnAction(e -> {
                if (problem.getText().isBlank() || targetCustomer.getText().isBlank()
                        || customerNeed.getText().isBlank() || knownPeopleWithProblem.getText().isBlank()
                        || targetMarketSize.getText().isBlank() || competitors.getText().isBlank()) {
                    root.getChildren().add(popUp);
                } else if (isNumeric(problem.getText()) || isNumeric(targetCustomer.getText())
                        || !isNumeric(customerNeed.getText()) || !isNumeric(knownPeopleWithProblem.getText())
                        || !isNumeric(targetMarketSize.getText()) || isNumeric(competitors.getText())) {
                    root.getChildren().add(popUp);
                } else if (Integer.parseInt(customerNeed.getText()) < 1 || Integer.parseInt(customerNeed.getText()) > 10
                        || Integer.parseInt(knownPeopleWithProblem.getText()) < 0
                        || Integer.parseInt(targetMarketSize.getText()) < 0) {
                    root.getChildren().add(popUp);
                } else {
                    StartUpIdea idea = new StartUpIdea(problem.getText(), targetCustomer.getText(),
                            Integer.parseInt(customerNeed.getText()),
                            Integer.parseInt(knownPeopleWithProblem.getText()),
                            Integer.parseInt(targetMarketSize.getText()), competitors.getText());
                    ideas.add(idea);
                    clearFields();
                }
            });

            sortBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Collections.sort(ideas);
                }
            });

            Button yes = new Button("Yes");
            Button no = new Button("No");
            popUp2.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
            popUp2.setAlignment(Pos.CENTER);
            popUp2.setSpacing(10);
            popUp2Options.setSpacing(7);
            popUp2Options.getChildren().addAll(yes, no);
            popUp2Options.setAlignment(Pos.CENTER);
            popUp2.getChildren().addAll(popUp2Text, popUp2Options);

            resetBtn.setOnAction(e -> {
                root.getChildren().add(popUp2);
                no.setOnAction(this::close);
                yes.setOnAction(e1 -> {
                    if (file.exists()) {
                        file.delete();
                    }
                    ideas.clear();
                    clearFields();
                    root.getChildren().remove(popUp2);
                });
            });

            saveBtn.setOnAction(e -> {
                if (!file.exists()) {
                    file = new File("ideas.txt");
                }
                FileUtil.saveIdeasToFile(ideas, file);
            });
        }

        /**
         * Closes the pop-up.
         * @param event clicking the button
         */
        public void close(ActionEvent event) {
            root.getChildren().remove(popUp);
            root.getChildren().remove(popUp2);
        }

        /**
         * Checks if the string is only containing integers.
         * @param str string
         * @return boolean
         */
        public boolean isNumeric(String str) {
            return str != null && str.matches("[-+]?\\d*\\.?\\d+");
        }

        /**
         * Clears all input fields.
         */
        public void clearFields() {
            problem.setText("");
            targetCustomer.setText("");
            customerNeed.setText("");
            knownPeopleWithProblem.setText("");
            targetMarketSize.setText("");
            competitors.setText("");
        }
    }
}
