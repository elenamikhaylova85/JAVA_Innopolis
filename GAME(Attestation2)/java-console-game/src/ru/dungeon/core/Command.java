package ru.dungeon.core;

import ru.dungeon.model.GameState;
import java.util.List;

@FunctionalInterface
public interface Command { void execute(GameState ctx, List<String> args); }
