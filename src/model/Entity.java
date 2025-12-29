package model;

import java.util.Objects;

public abstract class Entity {
    protected Point pos;

    public Entity(Point pos) {
        this.pos = pos;
    }

    public Point getPos() { return pos; }
    public void setPos(Point pos) { this.pos = pos; }

    public abstract char getSymbol();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return Objects.equals(pos, entity.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }

    @Override
    public String toString() {
        return "Object " + getSymbol() + " at " + pos;
    }
}