package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.awt.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AddCardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board parentBoard;
    private CardList parentCardList;

    @FXML
    private AnchorPane root;
    @FXML
    private TextField title;
    @FXML
    private ButtonBar tagsBar;
    @FXML
    private MenuButton addTag;
    @FXML
    private TextArea description;
    @FXML
    private Button submit;
    @FXML
    private ImageView addSubtask;
    @FXML
    private Label addSubtaskLabel;
    @FXML
    private GridPane subtaskPane;
    @FXML
    private TextField subtaskTitle;
    @FXML
    private TextArea subtaskDescription;
    private List<Task> subtasks;

    // temporary variable, because the Board class wasn't yet sufficiently implemented at the time of this commit.
    // replace with parentboard.tags, and set the value in MainCtrl when the Board is sufficiently implemented.
    private final Set<Tag> parentboardDotTags = new HashSet<Tag>(List.of(
            new Tag("Tag 1", Color.BLUE),
            new Tag("Tag 2", Color.RED)));

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        EventHandler<ActionEvent> removeTagEvent = e -> {
            this.tagsBar.getButtons().remove((ToggleButton) e.getSource());
        };
        EventHandler<ActionEvent> addTagEvent = e -> {
            String tagName = ((MenuItem) e.getSource()).getText();
            System.out.println(this.tagsBar.getButtons().size() - 1);
            if (this.tagsBar.getButtons().stream().anyMatch(
                    b -> (b instanceof ToggleButton) && ((ToggleButton) b).getText().equals(tagName)))
                return;

            Tag tag = parentboardDotTags.stream().filter(t -> t.name.equals(tagName)).findFirst().orElse(null);
            ToggleButton tagButton = new ToggleButton(tagName);
            tagButton.setStyle(String.format("-fx-background-color: rgb(%d, %d, %d);",
                    tag.color.getRed(), tag.color.getGreen(), tag.color.getBlue()));
            tagButton.getStyleClass().add("tag");

            tagButton.setOnAction(removeTagEvent);

            tagsBar.getButtons().add(tagButton);
        };
        for(Tag tag : parentboardDotTags){
            MenuItem mi = new MenuItem(tag.name);
            mi.setOnAction(addTagEvent);
            this.addTag.getItems().add(mi);
        }
        this.subtaskPane.setVisible(false);
    }

    public void setParentBoard(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    public void setParentCardList(CardList parentCardList) {
        this.parentCardList = parentCardList;
    }

    public void submitCard(){

        // communicate it to the parent Card
        if (this.title.getText().equals("")){
            this.submit.setText("Please provide a title!");
            this.submit.setStyle("-fx-text-fill: red;");
            return;
        }
        this.parentCardList.addCard(new Card(this.title.getText(), description.getText(), this.parentCardList));
        this.subtaskPane.setVisible(true);
        System.out.println("Grid: " + this.subtaskPane.toString());

        // communicate it to the server
        try {
            server.send("/app/cards", new Card(title.getText(), description.getText(), parentCardList));
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void addSubtask(){

        System.out.println("Grid: " + this.subtaskPane.toString());
        ImageView iv = new ImageView("file:/client/assets/trashcan_icon.png");
        iv.setFitHeight(50);
        iv.setFitWidth(50);
        TextField subtaskTitle = new TextField();
        subtaskTitle.setPrefWidth(300);
        subtaskTitle.setPrefHeight(50);
        subtaskTitle.setStyle("-fx-background-color: yellow;");
        subtaskTitle.setPromptText("Add subtask title...");
        TextArea subtaskDescription = new TextArea();
        subtaskDescription.setPrefWidth(300);
        subtaskDescription.setPrefHeight(100);
        subtaskDescription.setStyle("-fx-background-color: red;");
        subtaskDescription.setPromptText("Add description...");

        this.subtaskPane.add(new Text("SHOW URSELF"), 0, 0);
        this.subtaskPane.add(subtaskTitle, 1, 0);
        this.subtaskPane.add(new Text("PLEASE"), 0, 1);
        this.subtaskPane.add(subtaskDescription, 1, 1);

        System.out.println("Grid is now: "
                + this.subtaskPane.getRowCount()
                + " by "
                + this.subtaskPane.getColumnCount());
        for (Node child : this.subtaskPane.getChildren()) {
            System.out.println(child.toString());
        }
    }
}

