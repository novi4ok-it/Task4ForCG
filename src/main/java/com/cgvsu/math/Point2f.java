package com.cgvsu.math;

public class Point2f {

    private float x;
    private float y;

    // Конструктор по умолчанию
    public Point2f() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    // Конструктор с параметрами
    public Point2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Геттеры и сеттеры
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Вычисление расстояния до другой точки
    public float distance(Point2f other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // Операция сложения двух точек
    public Point2f add(Point2f other) {
        return new Point2f(this.x + other.x, this.y + other.y);
    }

    // Операция вычитания двух точек
    public Point2f subtract(Point2f other) {
        return new Point2f(this.x - other.x, this.y - other.y);
    }

    // Перевод в строку для отладки
    @Override
    public String toString() {
        return "Point2f{" + "x=" + x + ", y=" + y + '}';
    }

    // Сравнение двух точек
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point2f point2f = (Point2f) obj;
        return Float.compare(point2f.x, x) == 0 && Float.compare(point2f.y, y) == 0;
    }

    // Хэш-код для использования в коллекциях
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
