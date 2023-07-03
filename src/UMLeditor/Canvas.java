package UMLeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel {
	// private List<BasicObject> umlObjects;
	private List<UmlElement> umlObjects;

	private BasicObject pressedObject;
	private BasicObject releasedObject;
	private Point pressedPoint;
	private Point releasedPoint;

	private Point dragStartPoint;
	private Rectangle selectionRectangle;
	private Menu menu;
	
	public Canvas(Menu menu) {
		this.menu = menu;
		setBackground(Color.WHITE);
		umlObjects = new ArrayList<>();

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
			    if (menu.getMode().equals("Select")) {
			        boolean objectSelected = false;
			        for (UmlElement element : umlObjects) {
			            if (element.contains(e.getPoint())) {
			                element.setSelected(true);
			                element.setConnectionPortsVisible(true);
			                objectSelected = true;
			            } else {
			                element.setSelected(false);
			                element.setConnectionPortsVisible(false);
			            }
			        }

			        if (!objectSelected) {
			            for (UmlElement element : umlObjects) {
			                element.setSelected(false);
			            }
			        }

			        repaint();
			    } else if (menu.getMode() != null
			            && (menu.getMode().equals("Class") || menu.getMode().equals("Use Case"))) {
			        BasicObject newObject;
			        if (menu.getMode().equals("Class")) {
			            newObject = new ClassObject(e.getX(), e.getY());
			        } else {
			            newObject = new UseCaseObject(e.getX(), e.getY());
			        }
			        umlObjects.add(newObject);
			        repaint();
			    }
			}


			@Override
			public void mousePressed(MouseEvent e) {
		        if (menu.getMode().equals("Select")) {
		            pressedObject = getObjectAt(e.getPoint());
		            if (pressedObject != null) {
		                pressedObject.setSelected(true);
		            } else {
		                unselectAllObjects();
		            }
		            dragStartPoint = e.getPoint();
		            selectionRectangle = null;
				} else if (isConnectionLineMode(menu.getMode())) {
					pressedObject = getObjectAt(e.getPoint());
					if (pressedObject != null) {
						if (pressedObject instanceof CompositeObject) {
							BasicObject innerPressedObject = ((CompositeObject) pressedObject)
									.getNearestObjectContainingPoint(e.getPoint());
							if (innerPressedObject != null) {
								pressedPoint = innerPressedObject.getNearestConnectionPoint(e.getPoint());
							}
						} else {
							pressedPoint = pressedObject.getNearestConnectionPoint(e.getPoint());
						}
					}
				}
			}
 
			@Override
			public void mouseReleased(MouseEvent e) {
			    if (menu.getMode().equals("Select") && dragStartPoint != null) {
			        if (selectionRectangle != null) {
			            unselectAllObjects();
			            selectObjectsInRectangle(selectionRectangle);
			        }
			        for (Object obj : umlObjects) {
			            if (obj instanceof ConnectionLine) {
			                ((ConnectionLine) obj).updateConnectionPoints();
			            }
			        }
			        dragStartPoint = null;
			        selectionRectangle = null;
			        repaint();
				} else if (isConnectionLineMode(menu.getMode())) {
					releasedObject = getObjectAt(e.getPoint());
					if (releasedObject != null) {
						if (releasedObject instanceof CompositeObject) {
							BasicObject innerReleasedObject = ((CompositeObject) releasedObject)
									.getNearestObjectContainingPoint(e.getPoint());
							if (innerReleasedObject != null) {
								releasedPoint = innerReleasedObject.getNearestConnectionPoint(e.getPoint());
							}
						} else {
							releasedPoint = releasedObject.getNearestConnectionPoint(e.getPoint());
						}
					}
					if (pressedObject != null && releasedObject != null && pressedPoint != null
							&& releasedPoint != null) {
						BasicObject startObject = pressedObject;
						BasicObject endObject = releasedObject;

						if (pressedObject instanceof CompositeObject) {
							startObject = ((CompositeObject) pressedObject)
									.getNearestObjectContainingPoint(e.getPoint());
							if (startObject != null) {
								pressedPoint = startObject.getNearestConnectionPoint(e.getPoint());
							}
						}

						if (releasedObject instanceof CompositeObject) {
							endObject = ((CompositeObject) releasedObject)
									.getNearestObjectContainingPoint(e.getPoint());
							if (endObject != null) {
								releasedPoint = endObject.getNearestConnectionPoint(e.getPoint());
							}
						}

						ConnectionLine newLine = createConnectionLine(menu.getMode(), startObject, endObject,
								pressedPoint, releasedPoint);
						repaint(); // Repaint the panel to show the new connection line
					}

				}
			}

		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
			    if (menu.getMode().equals("Select") && dragStartPoint != null) {
			        Point currentPoint = e.getPoint();
			        int dx = currentPoint.x - dragStartPoint.x;
			        int dy = currentPoint.y - dragStartPoint.y;

			        if (pressedObject != null && pressedObject.isSelected()) {
			            pressedObject.moveBy(dx, dy);

			            // Update connected lines when moving any object
			            for (Object obj : umlObjects) {
			                if (obj instanceof ConnectionLine) {
			                    ConnectionLine connectionLine = (ConnectionLine) obj;
			                    if (connectionLine.getStartObject() == pressedObject || connectionLine.getEndObject() == pressedObject) {
			                        connectionLine.updateConnectionPoints();
			                    } else if (pressedObject instanceof CompositeObject) {
			                        CompositeObject compositeObject = (CompositeObject) pressedObject;
			                        for (BasicObject innerObject : compositeObject.getGroupedObjects()) {
			                            if (connectionLine.getStartObject() == innerObject || connectionLine.getEndObject() == innerObject) {
			                                connectionLine.updateConnectionPoints();
			                            }
			                        }
			                    }
			                }
			            }

			            dragStartPoint = currentPoint;
			            repaint();
			        } else {
			            int x = Math.min(dragStartPoint.x, currentPoint.x);
			            int y = Math.min(dragStartPoint.y, currentPoint.y);
			            int width = Math.abs(currentPoint.x - dragStartPoint.x);
			            int height = Math.abs(currentPoint.y - dragStartPoint.y);

			            selectionRectangle = new Rectangle(x, y, width, height);
			            repaint();
			        }
			    }
			}

		});
	}

	public BasicObject getSelectedObject() {
	    for (Object obj : umlObjects) {
	        if (obj instanceof BasicObject && ((BasicObject) obj).isSelected()) {
	            if (obj instanceof CompositeObject) {
	                CompositeObject compositeObject = (CompositeObject) obj;
	                for (BasicObject innerObject : compositeObject.getGroupedObjects()) {
	                    if (innerObject.isSelected()) {
	                        return innerObject;
	                    } 
	                }
	            } else {
	                return (BasicObject) obj;
	            }
	        }
	    }
	    return null;
	}



	private boolean isConnectionLineMode(String mode) {
		return mode.equals("Association Line") || mode.equals("Generalization Line")
				|| mode.equals("Composition Line");
	}

	private BasicObject getObjectAt(Point point) {
		// 反轉順序先檢查後加入的
		List<Object> reversedUmlObjects = new ArrayList<>(umlObjects);
		Collections.reverse(reversedUmlObjects);

		for (Object obj : reversedUmlObjects) {
			if (obj instanceof BasicObject && ((BasicObject) obj).contains(point)) {
				return (BasicObject) obj;
			}
		}
		return null;
	}

	// BasicObject先判斷
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    for (UmlElement element : umlObjects) {
	        element.draw(g);
	    }

	    if (menu.getMode().equals("Select") && selectionRectangle != null) {
	        g.setColor(Color.BLACK);
	        g.drawRect(selectionRectangle.x, selectionRectangle.y, selectionRectangle.width,
	                selectionRectangle.height);
	    }
	}


	private ConnectionLine createConnectionLine(String mode, BasicObject from, BasicObject to, Point pressedPoint,
			Point releasedPoint) {
		ConnectionLine line = null;

		switch (mode) {
		case "Association Line":
			line = new AssociationLine(pressedPoint, releasedPoint, from, to);
			break;
		case "Generalization Line":
			line = new GeneralizationLine(pressedPoint, releasedPoint, from, to);
			break;
		case "Composition Line":
			line = new CompositionLine(pressedPoint, releasedPoint, from, to);
			break;
		default:
			break;
		}
		if (line != null) {
			umlObjects.add(line); // Add the ConnectionLine object to the list
			repaint();
		}
		return line;
	}
	

	
	private void unselectAllObjects() {
	    for (UmlElement element : umlObjects) {
	        element.setSelected(false);
	        element.setConnectionPortsVisible(false);
	    }
	}


	private void selectObjectsInRectangle(Rectangle rectangle) {
	    for (Object obj : umlObjects) {
	        if (obj instanceof BasicObject) {
	            BasicObject basicObject = (BasicObject) obj;
	            if (rectangle.contains(basicObject.x, basicObject.y) && rectangle.contains(basicObject.x + basicObject.width, basicObject.y + basicObject.height)) {
	                basicObject.setSelected(true);
	                basicObject.setConnectionPortsVisible(true);
	            }
	        }

	        if (obj instanceof CompositeObject) {
	            CompositeObject compositeObject = (CompositeObject) obj;
	            if (rectangle.contains(compositeObject.x, compositeObject.y) && rectangle.contains(compositeObject.x + compositeObject.width, compositeObject.y + compositeObject.height)) {
	                compositeObject.setSelected(true);
	                compositeObject.setConnectionPortsVisible(true);
	            }
	        }
	    }
	}



	public void groupSelectedObjects() {
		CompositeObject compositeObject = new CompositeObject();
		List<BasicObject> selectedObjects = new ArrayList<>();

		for (Object obj : umlObjects) {
			if (obj instanceof BasicObject) {
				BasicObject basicObject = (BasicObject) obj;
				if (basicObject.isSelected()) {
					selectedObjects.add(basicObject);
					compositeObject.addGroupedObject(basicObject);
				}
			}
		}

		umlObjects.removeAll(selectedObjects);
		umlObjects.add(compositeObject);
		repaint();
	}

	public void ungroupSelectedObjects() {
		CompositeObject selectedCompositeObject = null;

		for (Object obj : umlObjects) {
			if (obj instanceof CompositeObject) {
				CompositeObject compositeObject = (CompositeObject) obj;
				if (compositeObject.isSelected()) {
					selectedCompositeObject = compositeObject;
					break;
				}
			}
		}

		if (selectedCompositeObject != null) {
			for (BasicObject basicObject : selectedCompositeObject.getGroupedObjects()) {
				basicObject.setSelected(false);
				basicObject.setConnectionPortsVisible(false);
			}
			umlObjects.addAll(selectedCompositeObject.getGroupedObjects());
			umlObjects.remove(selectedCompositeObject);
			repaint();
		}

	}

}
//@Override
//public void mouseClicked(MouseEvent e) {
//	if (menu.getMode().equals("Select")) {
//		boolean objectSelected = false;
//		for (Object obj : umlObjects) {
//
//			if (obj instanceof BasicObject) {
//				BasicObject basicObject = (BasicObject) obj;
//				if (basicObject.contains(e.getPoint())) {
//					basicObject.setSelected(true);
//					basicObject.setConnectionPortsVisible(true);
//					objectSelected = true;
//				} else {
//					basicObject.setSelected(false);
//					basicObject.setConnectionPortsVisible(false);
//				}
//			}
//		}
//
//		if (!objectSelected) {
//			for (Object obj : umlObjects) {
//				if (obj instanceof ConnectionLine) {
//					((ConnectionLine) obj).setSelected(false);
//				}
//			}
//		}
//
//		repaint();
//	} else if (menu.getMode() != null
//			&& (menu.getMode().equals("Class") || menu.getMode().equals("Use Case"))) {
//		BasicObject newObject;
//		if (menu.getMode().equals("Class")) {
//			newObject = new ClassObject(e.getX(), e.getY());
//		} else {
//			newObject = new UseCaseObject(e.getX(), e.getY());
//		}
//		umlObjects.add(newObject);
//		repaint();
//	}
//}
//@Override
//protected void paintComponent(Graphics g) {
//	super.paintComponent(g);
//
//	// Draw all BasicObject objects on top
//	for (Object obj : umlObjects) {
//		if (obj instanceof BasicObject) {
//			((BasicObject) obj).draw(g);
//		}
//	}
//	// Draw all ConnectionLine objects first
//	for (Object obj : umlObjects) {
//		if (obj instanceof ConnectionLine) {
//			((ConnectionLine) obj).draw(g);
//		}
//	}
//	if (menu.getMode().equals("Select") && selectionRectangle != null) {
//		g.setColor(Color.BLACK);
//		g.drawRect(selectionRectangle.x, selectionRectangle.y, selectionRectangle.width,
//				selectionRectangle.height);
//	}
//}
//private void unselectAllObjects() {
//for (Object obj : umlObjects) {
//	if (obj instanceof BasicObject) {
//		((BasicObject) obj).setSelected(false);
//		((BasicObject) obj).setConnectionPortsVisible(false);
//	}
//}
//}