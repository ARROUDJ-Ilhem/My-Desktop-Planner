package tp.javafx.models;

import java.time.Duration;

public interface Decomposable {
    boolean estDecomposable(Duration dureeCreneau, Duration dureeTache);
}