package org.antares.model;

public record Images(
        String background,
        String fighterLeft,
        String fighterRight,
        Boolean leftWins,
        Boolean rightWins
) {
}
