package com.tools;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * A extension of DefaultTreeModel
 * 
 * This model is made for file system models
 */
public class RelativeFileSystemModel extends DefaultTreeModel {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
	 * Should we hide files and show directories only ?
	 */
	boolean showDirectoryOnly;

	/**
	 * Create the tree, starting at the given path
	 * 
	 * @param rootFilePath
	 *            The path to the element which is the tree root
	 */
	public RelativeFileSystemModel(String rootFilePath,
			boolean showDirectoryOnly) {
		super(new DefaultMutableTreeNode(convertToNodeName(rootFilePath)));
		this.showDirectoryOnly = showDirectoryOnly;
		listFile(new File(rootFilePath), (DefaultMutableTreeNode) getRoot());
	}

	/**
	 * List files in given directory, and add them in tree
	 */
	private DefaultMutableTreeNode listFile(File file,
			DefaultMutableTreeNode node) {

		if (file.isFile()) {
			return new DefaultMutableTreeNode(convertToNodeName(file.getName()));
		}

		for (File name : file.listFiles()) {

			DefaultMutableTreeNode subNode;
			if (name.isDirectory()) {
				subNode = new DefaultMutableTreeNode(
						convertToNodeName(name.getName()));
				node.add(listFile(name, subNode));
			} else {
				
				if (showDirectoryOnly) {
					continue;
				}
				
				subNode = new DefaultMutableTreeNode(
						convertToNodeName(name.getName()));
			}
			
			node.add(subNode);
		}
		return node;
	}

	/**
	 * Get the path to display in a tree node
	 */
	private static String convertToNodeName(String path) {
		String[] files = path.split(File.separator.equals("\\") ? "\\\\" : File.separator);	// stupid windows fix...
		return files[files.length - 1];
	}

	/**
	 * Get file path with the node TreePath
	 * 
	 * @warning The root node is ignored
	 */
	public String getFilePath(TreePath tp) {
		String path = "";
		if (tp != null && tp.getPathCount() != 0) {
			for (int i = 1; i < tp.getPathCount(); ++i) {
				path += tp.getPathComponent(i);
				if (i != tp.getPathCount() - 1) {
					path += "/";
				}
			}
		}
		return path;
	}
}
