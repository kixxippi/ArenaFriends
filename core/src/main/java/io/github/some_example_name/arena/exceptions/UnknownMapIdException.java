package io.github.some_example_name.arena.exceptions;

public class UnknownMapIdException extends RuntimeException {
    public UnknownMapIdException(int mapId) {
        super("Unknown mapId: " + mapId + " (expected 1=WallsOnly, 2=PuddlesOnly, 3=Mixed)");
    }
}
