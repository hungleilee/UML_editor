package UMLeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

class ClassObject extends BasicObject {
    public ClassObject(int x, int y) {
        super(x, y);
        this.name = "Class"; // 預設
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(name, x + 40, y + 25);
        drawConnectionPorts(g);
    }
    public boolean contains(Point point) {
        
        // 檢查是否在物件範圍內
        return (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height);
    }
}