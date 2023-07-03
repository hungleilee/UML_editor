package UMLeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

class UseCaseObject extends BasicObject {
    public UseCaseObject(int x, int y) {
        super(x, y);
        this.name = "Use Case"; 
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
        g.drawString(name, x + 25, y + 25); 
        drawConnectionPorts(g);
    }
    public boolean contains(Point point) {
    	// 檢查是否在物件範圍內
        return (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height);
    }
}
