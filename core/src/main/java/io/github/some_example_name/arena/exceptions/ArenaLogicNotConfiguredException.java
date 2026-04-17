package io.github.some_example_name.arena.exceptions;

public class ArenaLogicNotConfiguredException extends RuntimeException {
    public ArenaLogicNotConfiguredException() {
        super("Arena logic strategy is not configured. Call arena.setLogicStrategy(...) in ArenaFactory.");
    }
}
