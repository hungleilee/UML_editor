package UMLeditor;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import UMLeditor.ConnectionLine.ArrowType;

abstract class ConnectionLine extends UmlElement{
    protected Point startPoint;
    protected Point endPoint;
    protected BasicObject startObject;
    protected BasicObject endObject;
    public BasicObject getStartObject() {
        return startObject;
    }

    public BasicObject getEndObject() {
        return endObject;
    }
    
    public Point getStart() {
        return startPoint;
    }

    public Point getEnd() {
        return endPoint;
    }

    public void setStart(Point start) {
        this.startPoint = start;
    }

    public void setEnd(Point end) {
        this.endPoint = end;
    }
    public enum ArrowType {
        LINE, TRIANGLE, DIAMOND
    }
    private boolean selected;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public ConnectionLine(Point startPoint, Point endPoint, BasicObject startObject, BasicObject endObject) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startObject = startObject;
        this.endObject = endObject;
    }

    //skip
    public void draw(Graphics g) {
        if (startObject instanceof CompositeObject) {
            startPoint = ((CompositeObject) startObject).getNearestConnectionPoint(startPoint);
        }
        if (endObject instanceof CompositeObject) {
            endPoint = ((CompositeObject) endObject).getNearestConnectionPoint(endPoint);
        }

        drawLine(g, startPoint, endPoint);
    }

    protected abstract void drawLine(Graphics g, Point startPoint, Point endPoint);


    protected static void drawArrow(Graphics g, Point start, Point end, ArrowType arrowType) {
        int arrowSize = 10; // 箭頭大小

        //Calculate the angle of the line
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        //Calculate the positions of the arrow endpoint
        int x1 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        
        g.drawLine(start.x, start.y, end.x, end.y);
        //Draw the arrow
        switch (arrowType) {
            case LINE:
                g.drawLine(x1, y1, end.x, end.y);
                g.drawLine(x2, y2, end.x, end.y);
                break;
            case TRIANGLE:
                Polygon triangle = new Polygon();
                triangle.addPoint(end.x, end.y);
                triangle.addPoint(x1, y1);
                triangle.addPoint(x2, y2);
                g.drawPolygon(triangle);
                break;
            case DIAMOND:
                int x3 = (int) (end.x - 2 * arrowSize * Math.cos(angle));
                int y3 = (int) (end.y - 2 * arrowSize * Math.sin(angle));
                int[] xPoints = {end.x, x1, x3, x2};
                int[] yPoints = {end.y, y1, y3, y2};
                g.drawPolygon(xPoints, yPoints, 4);
                break;
        }
    }
    
    public void updateConnectionPoints() {
        if (startObject != null && endObject != null) {
            startPoint = startObject.getNearestConnectionPoint(startPoint);
            endPoint = endObject.getNearestConnectionPoint(endPoint);
        }
    }
    
    public void setConnectionPortsVisible(boolean visible) {
    
    }
    
    @Override
	protected boolean contains(Point point) {
		
		return false;
	}

}