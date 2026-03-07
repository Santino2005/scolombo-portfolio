package edu.austral.dissis.chess.engine.Commons.GameAndAdapters;

import static edu.austral.dissis.chess.gui.AdapterKt.createGameViewFrom;

import edu.austral.dissis.chess.gui.CachedImageResolver;
import edu.austral.dissis.chess.gui.DefaultImageResolver;
import edu.austral.dissis.chess.gui.GameEngine;
import edu.austral.dissis.chess.gui.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChessGameApplication extends Application {

  @Override
  public void start(Stage primaryStage) {
    GameEngine gameEngine = new JavaAdapter(new GameFactory().createCheckersGame());
    CachedImageResolver imageResolver = new CachedImageResolver(new DefaultImageResolver());

    GameView root = createGameViewFrom(gameEngine, imageResolver);

    primaryStage.setTitle("Chess");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

