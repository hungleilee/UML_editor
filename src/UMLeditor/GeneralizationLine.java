package UMLeditor;

import java.awt.Graphics;
import java.awt.Point;

import UMLeditor.ConnectionLine.ArrowType;

class GeneralizationLine extends ConnectionLine {

    public GeneralizationLine(Point startPoint, Point endPoint, BasicObject startObject, BasicObject endObject) {
        super(startPoint, endPoint, startObject, endObject);
    }

    @Override
    protected void drawLine(Graphics g, Point startPoint, Point endPoint) {
        // Draw the line
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw the arrow for generalization
        drawArrow(g, startPoint, endPoint, ArrowType.TRIANGLE);
    }

    @Override
    public void draw(Graphics g) {
        // Draw the line
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw the arrow for generalization
        drawArrow(g, startPoint, endPoint, ArrowType.TRIANGLE);
    }
}