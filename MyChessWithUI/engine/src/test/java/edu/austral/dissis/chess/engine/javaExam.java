package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.test.game.GameTester;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class javaExam {

  @TestFactory
  Stream<DynamicTest> requiredExamTests() {
    return new GameTester(new JavaTestGameRunner()).test();
  }
}


