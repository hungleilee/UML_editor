package UMLeditor;

import java.awt.Graphics;
import java.awt.Point;

public abstract class UmlElement {
    protected boolean selected = false;

    public boolean isSelected() {
        return selected;
    } 

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
 
    public abstract void draw(Graphics g);
    public abstract void setConnectionPortsVisible(boolean visible);

	protected abstract boolean contains(Point point);
}
