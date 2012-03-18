package Interfaces;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import Model.GenericModel;

public interface GameClickListenerI extends MouseListener, MouseMotionListener {

	void mouseEntered(MouseEvent e);

	void mouseExited(MouseEvent e);

	/**
	 * To be called to clear the list of selected objects.
	 */
	public void clearSelected();

	/**
	 * 
	 * @return the list of selected objects.
	 */
	public ArrayList<GenericModel> getSelected();
	
}