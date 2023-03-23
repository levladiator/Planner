/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.shared.CredentialsStore;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var login = FXML.load(LogonCtrl.class, "client", "scenes", "Logon.fxml");
        var boards = FXML.load(BoardsCtrl.class, "client", "scenes", "Boards.fxml");
        var boardOverview = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var cardList = FXML.load(CardListCtrl.class, "client", "scenes", "CardList.fxml");
        var addCardList = FXML.load(AddCardListCtrl.class, "client", "scenes", "AddCardList.fxml");
        var addCard = FXML.load(AddCardCtrl.class, "client", "scenes", "AddCard.fxml");
        var boardSettings = FXML.load(BoardSettingsCtrl.class, "client", "scenes", "BoardSettings.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        var cStore = new CredentialsStore();

        mainCtrl.initialize(primaryStage,
                login,
                boards,
                boardOverview,
                cardList,
                addCardList,
                addCard,
                boardSettings,
                cStore);
    }
}