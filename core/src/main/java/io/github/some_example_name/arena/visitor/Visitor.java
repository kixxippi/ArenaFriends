package io.github.some_example_name.arena.visitor;

import io.github.some_example_name.arena.WallVisual;
import io.github.some_example_name.arena.PuddleVisual;

/**
 * Visitor pattern — интерфейс посетителя.
 *
 * Объявляет по одному методу на каждый тип элемента арены.
 * Имена методов явно показывают с каким типом работает посетитель
 * (visitWall, visitPuddle) — точно как в канонической реализации паттерна.
 *
 * Конкретные посетители:
 *   StatsCollectorVisitor — собирает статистику арены (логика)
 *
 * Элементы арены (VisitableVisual):
 *   WallVisual   → вызывает v.visitWall(this)
 *   PuddleVisual → вызывает v.visitPuddle(this)
 */
public interface Visitor {
    void visitWall(WallVisual wall);
    void visitPuddle(PuddleVisual puddle);
}
