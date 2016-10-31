package za.co.yellowfire.threesixty.domain.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

	private OrganizationRepository repository;
	private OrganizationMetadata metadata = new OrganizationMetadata();
	
	@Autowired
	public OrganizationService(final OrganizationRepository repository) {
		this.repository = repository;
	}
	
	public void persist(Organization node) {
		persist(node, null);
	}
	
	private void persist(Organization node, Organization parent) {
		
		if (parent != null) {
			node.setParent(parent);
		}
		node = repository.save(node);
		
		if (node.hasChildren()) {
			for(Organization child : node.getChildren()) {
				persist(child, node);
			}
		}		
	}
	
	public List<Organization> retrieveRoots() {
		List<Organization> roots = repository.findRoot(true);
		for (Organization root : roots) {
			root.setMetadata(Optional.of(metadata.getRootMetadata()));
			retrieveChildren(root);
		}
		return roots;
	}
	
	private void retrieveChildren(Organization root) {
		root.setChildren(repository.findChildren(root.getId(), true));
		for (Organization child : root.getChildren()) {
			child.setMetadata(metadata.getMetadata(child.getType()));
			retrieveChildren(child);
		}
	}
	
	public long count() {
		return this.repository.count();
	}
}
