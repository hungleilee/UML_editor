package UMLeditor;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class CompositeObject extends BasicObject {
    private List<BasicObject> groupedObjects;
    private List<ConnectionLine> connectionLines; // 連接線清單

    public CompositeObject() {
        this(0, 0);
    }

    public CompositeObject(int x, int y) {
        super(x, y);
        groupedObjects = new ArrayList<>();
        connectionLines = new ArrayList<>(); 
    }
    
    public void addGroupedObject(BasicObject obj) {
        groupedObjects.add(obj);
        updateBounds();
    }

    public List<BasicObject> getGroupedObjects() {
        return groupedObjects;
    }

    @Override
    public void draw(Graphics g) {
        for (BasicObject obj : groupedObjects) {
            obj.draw(g);
        }
        // 畫出所有連接線
        for (ConnectionLine line : connectionLines) {
            line.draw(g);
        }
    }

    @Override
    public boolean contains(Point point) {
        for (BasicObject obj : groupedObjects) {
            if (obj.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setSelected(boolean isSelected) {
        super.setSelected(isSelected);
        for (BasicObject obj : groupedObjects) {
            obj.setSelected(isSelected);
        }
    }

    @Override
    public void setConnectionPortsVisible(boolean isVisible) {
        for (BasicObject obj : groupedObjects) {
            obj.setConnectionPortsVisible(isVisible);
        }
    }

    public BasicObject getNearestObjectContainingPoint(Point point) {
        BasicObject nearestObject = null;
        double minDistance = Double.MAX_VALUE;

        for (BasicObject obj : groupedObjects) {
            if (obj.contains(point)) {
                double distance = point.distance(obj.getCenter());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestObject = obj;
                }
            }
        }
        return nearestObject;
    }

    @Override
    public void moveBy(int dx, int dy) {
        super.moveBy(dx, dy);
        for (BasicObject obj : groupedObjects) {
            obj.moveBy(dx, dy);
        }
        for (ConnectionLine line : connectionLines) {
            line.updateConnectionPoints(); 
        }
        updateBounds();
    }

 
    public void updateBounds() {
        if (groupedObjects.isEmpty()) {
            return;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (BasicObject obj : groupedObjects) {
            minX = Math.min(minX, obj.x);
            minY = Math.min(minY, obj.y);
            maxX = Math.max(maxX, obj.x + obj.width);
            maxY = Math.max(maxY, obj.y + obj.height);
        }

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }
}