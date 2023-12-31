package client.scenes;

import client.utils.ServerUtils;
import client.utils.UIUtils;
import com.google.inject.Inject;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the AddCardList scene,
 * where the user can create a new CardList for the current board.
 */

public class AddCardListCtrl implements Initializable{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    @Inject
    public AddCardListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.title.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
    }

    /**
     * Method to check whether a new cardList has a valid new name
     */
    public void ok() {

        if (this.title.getText().trim().equals("")) {
            UIUtils.showError("Card List name can't be empty!");
        }
        else {
            try {
                server.addCardList(getCardList());
            } catch (WebApplicationException e) {
                UIUtils.showError(e.getMessage());
            }

            clearFields();
            mainCtrl.showBoardOverview();
        }
    }

    /**
     * getter for cardList
     * @return the cardList
     */
    private CardList getCardList() {
        return new CardList(title.getText(), mainCtrl.getCurrentBoard());
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoardOverview();
    }

    /**
     * clears fields
     */
    private void clearFields() {
        title.clear();
    }

    /**
     * method for shortcuts "enter" and "escape" when used on a cardlist.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

}
