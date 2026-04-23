package io.github.some_example_name.arena.visitor;

import io.github.some_example_name.arena.PuddleVisual;
import io.github.some_example_name.arena.WallVisual;

/**
 * Visitor pattern — конкретный посетитель.
 *
 * Обходит все элементы арены и собирает игровую статистику:
 * количество стен/луж и их суммарную площадь.
 *
 * Не содержит GUI-логики — работает только с геометрией Rectangle.
 *
 * Клиентский код (по шаблону из лекции):
 *
 *   StatsCollectorVisitor stats = new StatsCollectorVisitor();
 *   for (VisitableVisual v : arena.getVisuals()) {
 *       v.accept(stats);          // каждый элемент сам вызывает нужный visitXxx
 *   }
 *   log.info(stats.toString());
 */
public class StatsCollectorVisitor implements Visitor {

    private int wallCount      = 0;
    private int puddleCount    = 0;
    private float totalWallArea   = 0f;
    private float totalPuddleArea = 0f;

    // WallVisual.accept(v) вызывает v.visitWall(this)
    @Override
    public void visitWall(WallVisual wall) {
        wallCount++;
        totalWallArea += wall.getRect().width * wall.getRect().height;
    }

    // PuddleVisual.accept(v) вызывает v.visitPuddle(this)
    @Override
    public void visitPuddle(PuddleVisual puddle) {
        puddleCount++;
        totalPuddleArea += puddle.getRect().width * puddle.getRect().height;
    }

    public int   getWallCount()        { return wallCount; }
    public int   getPuddleCount()      { return puddleCount; }
    public float getTotalWallArea()    { return totalWallArea; }
    public float getTotalPuddleArea()  { return totalPuddleArea; }

    @Override
    public String toString() {
        return String.format(
            "ArenaStats{walls=%d, puddles=%d, wallArea=%.1f, puddleArea=%.1f",
            wallCount, puddleCount, totalWallArea, totalPuddleArea
        );
    }
}
