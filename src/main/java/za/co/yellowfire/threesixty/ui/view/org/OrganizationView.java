package za.co.yellowfire.threesixty.ui.view.org;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;

import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.AbstractDashboardPanel;


@SpringView(name = OrganizationView.VIEW_NAME)
public class OrganizationView extends AbstractDashboardPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Organization.SINGULAR;
	public static final String VIEW_NAME = "organization";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); } 
    public static final String VIEW_NEW() { return VIEW("new_" + VIEW_NAME); }
    
    private Tree tree;
    
    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
    
	public OrganizationView() {
		super();
	}
	

	protected Component buildContent() {
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		
		Tree tree = buildTree();
		layout.addComponent(tree);
		
		Panel content = new Panel();
		layout.addComponent(content);
		
		layout.setExpandRatio(tree, 1);
		layout.setExpandRatio(content, 2);
		return layout;
	}
	
	protected Tree buildTree() {
		tree = new Tree();

		Node node = 
				new Node("Organization")
					.child(new Node("Division")
							.child(new Node("Department 1")
									.child(new Node("Team 1"), new Node("Team 2"), new Node("Team 3")),
								   new Node("Department 2")
									.child(new Node("Team 1"), new Node("Team 2"), new Node("Team 3"))));
		
		buildNode(node, null);
		return tree;
	}
	
	private void buildNode(Node node, Node parent) {
		tree.addItem(node);
		
		if (parent != null) {
			tree.setParent(node, parent);
		}
		if (node.hasChildren()) {
			for(Node child : node.children) {
				buildNode(child, node);
			}
		} else {
			tree.setChildrenAllowed(node, false);
		}
	}
	
	@Override
    public void enter(final ViewChangeEvent event) {
		build();
    }
	
	private static class Node {
		private Node parent;
		private Set<Node> children = new HashSet<>();
		private String caption = "";
		
		public Node(final String caption) {
			this.caption = caption;
		}
		
		public Node parent(final Node parent) {
			this.parent = parent;
			parent.child(this);
			return this;
		}
		
		public Node child(final Node child) {
			child.parent = this;
			this.children.add(child);
			return this;
		}
		
		public Node child(final Node...children) {
			for(Node child : children) {
				child.parent = this;
				this.children.add(child);
			}
			return this;
		}
		
		public boolean hasChildren() { return !this.children.isEmpty(); }
		
		public String toString() { return this.caption; }
	}
}
