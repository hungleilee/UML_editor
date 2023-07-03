package UMLeditor;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public abstract class BasicObject extends UmlElement{
    protected int x, y, width, height;
    //protected boolean selected;
    protected String name;
    private boolean selected = false;
    private boolean connectionPortsVisible = false;

    public BasicObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 50;
        this.selected = false;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);
    public abstract boolean contains(Point point);
    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setConnectionPortsVisible(boolean visible) {
        this.connectionPortsVisible = visible;
    }
    
    public void drawConnectionPorts(Graphics g) {
        if (connectionPortsVisible) {
            for (Point p : getConnectionPoints()) {
                g.setColor(Color.RED);
                g.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
        }
    }
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public List<Point> getConnectionPoints() {
        List<Point> connectionPoints = new ArrayList<>();
        connectionPoints.add(new Point(x, y + height / 2)); // Left
        connectionPoints.add(new Point(x + width, y + height / 2)); // Right
        connectionPoints.add(new Point(x + width / 2, y)); // Top
        connectionPoints.add(new Point(x + width / 2, y + height)); // Bottom
        return connectionPoints;
    }
    
    public Point getNearestConnectionPoint(Point point) {
        double minDistance = Double.MAX_VALUE;
        Point nearestPoint = null;

        for (Point connectionPoint : getConnectionPoints()) {
            double distance = point.distance(connectionPoint);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = connectionPoint;
            }
        }

        return nearestPoint;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    

}
    

//public Point getClosestConnectionPoint(Point p) {
//List<Point> connectionPoints = new ArrayList<>();
//connectionPoints.add(new Point(x + width / 2, y)); // Top
//connectionPoints.add(new Point(x + width / 2, y + height)); // Bottom
//connectionPoints.add(new Point(x, y + height / 2)); // Left
//connectionPoints.add(new Point(x + width, y + height / 2)); // Right
//
//Point closest = null;
//double minDistance = Double.MAX_VALUE;
//
//for (Point point : connectionPoints) {
//  double distance = Math.sqrt(Math.pow(point.x - p.x, 2) + Math.pow(point.y - p.y, 2));
//  if (distance < minDistance) {
//      minDistance = distance;
//      closest = point;
//  }
//}
//
//return closest;
//}



