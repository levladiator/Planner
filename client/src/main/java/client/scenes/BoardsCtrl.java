package client.scenes;

import java.io.IOException;
import java.util.Random;

import client.utils.ServerUtils;
import client.utils.UIUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.NotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class is the controller of the Boards scene,
 * which is basically a menu where the user can either:
 * 1. join a new board
 * 2. create a new board
 * 3. choose a board from the list of previously joined boards
 */

public class BoardsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField key;

    @FXML
    private ListView<Parent> previous;

    @FXML
    private Label listLabel;

    @FXML
    private Button shutdown;

    private final Random rand;

    @Inject
    public BoardsCtrl(ServerUtils server, MainCtrl mainCtrl, Random rand) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.rand = rand;
    }

    /**
     * shuts down the server
     */
    @FXML
    public void shutdown() {
        mainCtrl.showLogon();
        try {
            server.shutdown();
        } catch (Exception ignored) {
        }
    }

    public void prepare() {
        server.connect();
        server.registerForMessages("/topic/relist/", Board.class, q -> {
            Platform.runLater(() -> {
                relist();
            });
        });

        shutdown.setVisible(mainCtrl.accessStore().isAdmin());

        relist();
    }

    public void relist() {
        previous.getItems().clear();

        if(mainCtrl.accessStore().isAdmin()) {
            listLabel.setText("All boards:");
            for(var board : server.getBoards()) {
                addToPreviousList(board, true);
            }
        } else {
            listLabel.setText("Previously joined boards:");
            for(var board : server.getPrevious()) {
                addToPreviousList(board, false);
            }
        }
    }

    /**
     * adds a board to a list of previously joined boards
     * @param board the board to add
     * @param isAdmin boolean whether user is admin
     */
    private void addToPreviousList(Board board, boolean isAdmin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/" +
                    (isAdmin ? "AdminBoard.fxml" :  "NonAdminBoard.fxml")));
            loader.setControllerFactory(c -> new AdminBoardCtrl(server, mainCtrl, board));
            Parent root = loader.load();
            root.setUserData(board);
            previous.getItems().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * allows a user to join a board
     */
    public void join() {
        if(UIUtils.isNullOrEmpty(key.getText())) {
            UIUtils.showError("The board key must not be empty");
            return;
        }

        Board receivedBoard = null;
        try {
            receivedBoard = server.joinBoard(key.getText());
        } catch (NotFoundException e) {
            UIUtils.showError("This board has not been found");
        } catch (Exception e) {
            UIUtils.showError("An unexpected error occurred");
        }

        clearFields();

        mainCtrl.setupBoardOverview(receivedBoard);
        mainCtrl.showBoardOverview();
    }

    /**
     * creates a new board for the user
     */
    public void create() {
        StringBuilder sb = new StringBuilder();

        for(int j = 0; j < 3; j++) {
            for (int i = 0; i < 6; i++) {
                sb.append((char)('a' + rand.nextInt(26)));
            }
            if(j != 2){sb.append('-');}
        }

        Board created = new Board(sb.toString(), "New board");

        mainCtrl.setupBoardOverview(server.addBoard(created));
        mainCtrl.showBoardOverview();
    }

    public void clearFields() {
        this.key.clear();
    }

    public void fillIn() {
        key.setText(((Board)previous.getSelectionModel().getSelectedItem().getUserData()).key);
    }

    public void back() {
        mainCtrl.showLogon();
    }

    @FXML
    public void showHelp() {
        mainCtrl.showHelpScreen("logon");
    }
}
