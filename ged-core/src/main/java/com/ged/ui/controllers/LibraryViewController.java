package com.ged.ui.controllers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.services.GedDocumentService;
import com.ged.tools.FileHelper;
import com.ged.ui.listeners.LibraryListener;
import com.ged.ui.screens.SoftwareScreen.Screen;
import com.ged.ui.widgets.LibraryView;
import com.tools.PropertiesHelper;

/**
 * The library view Controller
 * 
 * @author xavier
 * 
 *         Also include an adapted tree transfer handler
 * @see http://www.coderanch.com/t/346509/GUI/java/JTree-drag-drop-inside-one
 */
public class LibraryViewController extends TransferHandler implements
		MouseListener, TreeSelectionListener, TreeModelListener {

	private static final Logger logger = Logger.getLogger(LibraryViewController.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The controlled object
	 */
	private LibraryView libraryView;

	/**
	 * Drag & drop action is running
	 */
	private boolean dragAndDropIsRunning;

	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Temporally variable, for renaming...
	 */
	private String nodePath;
	
	
	protected Properties properties;
	
	
	public LibraryViewController(LibraryView libraryView) {
		this.libraryView = libraryView;

		// drag & drop
		dragAndDropIsRunning = false;
		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\""
					+ javax.swing.tree.DefaultMutableTreeNode[].class.getName()
					+ "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFound: " + e.getMessage());
		}
		this.properties = PropertiesHelper.getInstance().getProperties();
	}

	/**
	 * Return true if one element at least is selected, false otherwise
	 */
	public boolean isElementSelected() {
		return (libraryView.getTree().getSelectionCount() > 0);
	}
	
	/**
	 * Build a file path with the tree node path
	 * 
	 * @warning The root is ignored
	 * 
	 * @param path
	 *            The Node path
	 * @return The file (or directory) path
	 */
	private String getFilePath(TreeNode[] path) {
		String strPath = "";
		for (int i = 1; i < path.length; ++i) {
			strPath += path[i];
			if (i != path.length - 1) {
				strPath += "/";
			}
		}
		return strPath;
	}

	/**
	 * Show popup menu
	 */
	@Override
	public void mousePressed(MouseEvent event) {

		if (event.getButton() == MouseEvent.BUTTON3) {

			JTree tree = libraryView.getTree();

			if (tree.getRowForLocation(event.getX(), event.getY()) != -1) {

				tree.setSelectionPath(tree.getPathForLocation(event.getX(),
						event.getY()));

				// look for parent node
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) libraryView
						.getTree().getLastSelectedPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
						.getParent();

				List<JMenuItem> menus = new ArrayList<JMenuItem>();
				File currentNode = new File(Profile.getInstance()
						.getLibraryRoot() + getFilePath(node.getPath()));

				if (currentNode.isFile()) {
					JMenuItem editMenu = new JMenuItem(properties.getProperty("modify"));
					editMenu.addActionListener(new EditMenuListener(node, parentNode));

					menus.add(editMenu);
				}
				if (node.isRoot() || currentNode.isDirectory()) {
					JMenuItem addDirectoryMenu = new JMenuItem(
							properties.getProperty("add_directory"));
					addDirectoryMenu.addActionListener(new AddNodeMenuListener(
							node));

					menus.add(addDirectoryMenu);
				}
				if (!node.isRoot()) {
					JMenuItem renameMenu = new JMenuItem(properties.getProperty("rename"));
					renameMenu.addActionListener(new RenameMenuListener(node));

					JMenuItem eraseMenu = new JMenuItem(properties.getProperty("delete"));
					eraseMenu.addActionListener(new EraseMenuListener(
							parentNode, node));

					menus.add(renameMenu);
					menus.add(eraseMenu);
				}

				JPopupMenu jpm = new JPopupMenu();
				for (JMenuItem item : menus) {
					jpm.add(item);
				}
				jpm.show(libraryView.getTree(), event.getX(), event.getY());
				jpm.show(tree, event.getX(), event.getY());
			}
		}
	}

	/**
	 * Listener for deleting nodes
	 * 
	 * @author CHerby
	 * @see http://www.siteduzero.com/tutoriel-3-65570-les-arbres.html#ss_part_4
	 */
	class EraseMenuListener implements ActionListener {

		private DefaultMutableTreeNode parentNode;
		private DefaultMutableTreeNode node;

		public EraseMenuListener(DefaultMutableTreeNode parent,
				DefaultMutableTreeNode node) {
			this.parentNode = parent;
			this.node = node;
		}

		public void actionPerformed(ActionEvent e) {
			int option = JOptionPane.showConfirmDialog(null,
					properties.getProperty("wanna_delete_item"),
					properties.getProperty("delete"), JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				libraryView.getModel().removeNodeFromParent(this.node);
				libraryView.getModel().nodeChanged(this.parentNode);
			}
		}
	}

	/**
	 * Listener for node renaming
	 */
	class RenameMenuListener implements ActionListener {

		private DefaultMutableTreeNode node;

		public RenameMenuListener(DefaultMutableTreeNode node) {
			this.node = node;
			nodePath = getFilePath(node.getPath());
		}

		public void actionPerformed(ActionEvent e) {
			libraryView.getTree().setEditable(true);
			TreeNode[] nodes = libraryView.getModel().getPathToRoot(node);
			TreePath path = new TreePath(nodes);
			libraryView.getTree().startEditingAtPath(path);
		}
	}
	
	
	/**
	 * Listener for document edition
	 */
	class EditMenuListener implements ActionListener {

		private DefaultMutableTreeNode parentNode;
		
		public EditMenuListener(DefaultMutableTreeNode node, DefaultMutableTreeNode parentNode) {
			nodePath = getFilePath(node.getPath());
			this.parentNode = parentNode;
		}

		public void actionPerformed(ActionEvent e) {
			libraryView.getParentScreen().pushScreen(Screen.EDITION_SCREEN);
			
			Map<String, Object> extra = new HashMap<String, Object>();
			extra.put("document_file_path", nodePath);
			extra.put("parent_node", parentNode);
			
			libraryView.getParentScreen().putExtra(extra);
		}
	}
	

	/**
	 * Listener for adding a directory
	 */
	class AddNodeMenuListener implements ActionListener {

		private DefaultMutableTreeNode parentNode;

		public AddNodeMenuListener(DefaultMutableTreeNode parentNode) {
			this.parentNode = parentNode;
		}

		public void actionPerformed(ActionEvent e) {
			libraryView.getTree().setEditable(true);
			
			if ( ! new File(Profile.getInstance().getLibraryRoot() + libraryView.getSelectedPath() + "/" + properties.getProperty("new_dir")).mkdir() ) {
				return;
			}
			
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
					properties.getProperty("new_dir"));
			libraryView.getModel().insertNodeInto(newNode, parentNode,
					parentNode.getChildCount());
			TreeNode[] nodes = libraryView.getModel().getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			libraryView.getTree().scrollPathToVisible(path);
			libraryView.getTree().setSelectionPath(path);
			libraryView.getTree().startEditingAtPath(path);
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		if (libraryView.getTree().getLastSelectedPathComponent() != null) {
			String newSelectionPath = libraryView.getModel().getFilePath(
					arg0.getPath());
            for(LibraryListener listener : getLibraryListeners()) {
                listener.selectionChanged(newSelectionPath);
            }
		}
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		libraryView.getTree().setEditable(false); // edition is over
		GedDocumentService.renameDocumentFile(nodePath, libraryView.getSelectedPath());
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		libraryView.getTree().setEditable(false); // edition is over
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		libraryView.getTree().setEditable(false); // edition is over
		if (!dragAndDropIsRunning) {
			GedDocumentService.deleteDocumentFile(libraryView.getSelectedPath());
		}
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	// for externals listeners

	public void addLibraryListener(LibraryListener listener) {
		listeners.add(LibraryListener.class, listener);
	}

	public void removeLibraryListener(LibraryListener listener) {
		listeners.remove(LibraryListener.class, listener);
	}

	public LibraryListener[] getLibraryListeners() {
		return listeners.getListeners(LibraryListener.class);
	}

	
	// lucky getter (the value is not really sure)
	
	public String getLastKnownNodePath() {
		return nodePath;
	}
	
	
	// drag & drop

	private DataFlavor nodesFlavor;

	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;

	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}
		support.setShowDropLocation(true);
		if (!support.isDataFlavorSupported(nodesFlavor)) {
			return false;
		}
		// Do not allow a drop on the drag source selections.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		JTree tree = (JTree) support.getComponent();
		int dropRow = tree.getRowForPath(dl.getPath());
		int[] selRows = tree.getSelectionRows();
		for (int i = 0; i < selRows.length; i++) {
			if (selRows[i] == dropRow) {
				return false;
			}
		}

		// Do not allow a non-leaf node to be copied to a level
		// which is less than its source level.
		TreePath dest = dl.getPath();

		if (dest == null) {
			return false;
		}

		DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		TreePath path = tree.getPathForRow(selRows[0]);
		DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		// moving a node on a file is forbidden
		logger.debug(getNodePath(target));
		if (new File(getNodePath(target)).isFile()) {
			return false;
		}

		// cannot move in my parent, I'm in !
		if (firstNode.getParent() == target) {
			return false;
		}

		if (firstNode.getChildCount() > 0
				&& target.getLevel() < firstNode.getLevel()) {
			return false;
		}
		return true;
	}

	private String getNodePath(DefaultMutableTreeNode node) {
		return Profile.getInstance().getLibraryRoot()
				+ getFilePath(node.getPath());
	}

	protected Transferable createTransferable(JComponent c) {
		dragAndDropIsRunning = true;
		nodePath = libraryView.getSelectedPath();
		
		// cannot move root
		if (libraryView.getTree().getSelectionRows()[0] == 0) { 
			dragAndDropIsRunning = false;
			return null;
		}
		
		JTree tree = (JTree) c;
		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			// Make up a node array of copies for transfer and
			// another for/of the nodes that will be removed in
			// exportDone after a successful drop.
			List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
			List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0]
					.getLastPathComponent();
			DefaultMutableTreeNode copy = (DefaultMutableTreeNode) node.clone();
			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i]
						.getLastPathComponent();
				// Do not allow higher level nodes to be added to list.
				if (next.getLevel() < node.getLevel()) {
					break;
				} else if (next.getLevel() > node.getLevel()) { // child node
					copy.add((MutableTreeNode) node.clone());
					// node already contains child
				} else { // sibling
					copies.add((DefaultMutableTreeNode) node.clone());
					toRemove.add(next);
				}
			}
			DefaultMutableTreeNode[] nodes = copies
					.toArray(new DefaultMutableTreeNode[copies.size()]);
			nodesToRemove = toRemove
					.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransferable(nodes);
		}
		return null;
	}

	protected void exportDone(JComponent source, Transferable data, int action) {

		if ((action & MOVE) == MOVE) {
			JTree tree = (JTree) source;
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Remove nodes saved in nodesToRemove in createTransferable.
			for (int i = 0; i < nodesToRemove.length; i++) {
				model.removeNodeFromParent(nodesToRemove[i]);
			}
		}
		dragAndDropIsRunning = false;
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
		} catch (UnsupportedFlavorException ufe) {
			logger.error("UnsupportedFlavor: " + ufe.getMessage());
		} catch (java.io.IOException ioe) {
			logger.error("I/O error: " + ioe.getMessage());
		}
		// Get drop location info.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		int childIndex = dl.getChildIndex();
		TreePath dest = dl.getPath();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		JTree tree = (JTree) support.getComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model.
		for (int i = 0; i < nodes.length; i++) {
			model.insertNodeInto(nodes[i], parent, index++);
	
			FileHelper.move(Profile.getInstance().getLibraryRoot() + nodePath, Profile.getInstance().getLibraryRoot() + getFilePath(nodes[i].getPath()));
		}
		
		libraryView.getTree().setEditable(false);
		
		String[] splittedPath = nodePath.split("/");
		String target = getFilePath(parent.getPath()) + "/" + splittedPath[splittedPath.length - 1];
		if (target.startsWith("/")) {
			target = splittedPath[splittedPath.length - 1];
		}
		GedDocumentService.renameDocumentFile(nodePath, target);
		
		return true;
	}

	public String toString() {
		return getClass().getName();
	}

	public class NodesTransferable implements Transferable {
		DefaultMutableTreeNode[] nodes;

		public NodesTransferable(DefaultMutableTreeNode[] nodes) {
			this.nodes = nodes;
		}

		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return nodes;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}

	}

}
