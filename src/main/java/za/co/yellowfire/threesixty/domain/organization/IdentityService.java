package za.co.yellowfire.threesixty.domain.organization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {

	private IdentityRepository repository;
	private IdentityMetadata metadata = new IdentityMetadata();
	
	@Autowired
	public IdentityService(final IdentityRepository repository) {
		this.repository = repository;
	}
	
	public void persist(Identity node) {
		persist(node, null);
	}
	
	private void persist(Identity node, Identity parent) {
		
		if (parent != null) {
			node.setParent(parent);
		}
		node = save(node);
		
		if (node.hasChildren()) {
			for(Identity child : node.getChildren()) {
				persist(child, node);
			}
		}		
	}
	
	public Identity createChildFor(
			final Identity node) {
		
		Identity child = new Identity("", node);
		if (node.getMetadata().isPresent()) {
			child.setMetadata(node.getMetadata().get().getChild().orElse(null));
		}
		persist(child, node);
		return child;
	}
	
	public Identity save(final Identity node) {
		return repository.save(node);
	}

	public List<Identity> retrieve(
			final boolean onlyActive) {

		List<Identity> roots = onlyActive ? repository.findRoot(onlyActive) : repository.findRoot();
		for (Identity root : roots) {
			root.setMetadata(metadata.getRootMetadata());
			retrieveChildren(root);
		}
		return roots;
	}

	public List<Identity> retrieve(
			final Identity identity,
			final boolean onlyActive) {

		List<Identity> roots = onlyActive ? repository.findChildren(identity.getId(), onlyActive) : repository.findChildren(identity.getId());
		for (Identity root : roots) {
			root.setMetadata(metadata.getMetadata(root.getType()).orElse(metadata.getRootMetadata()));
			retrieveChildren(root);
		}
		return roots;
	}

	public List<Identity> retrieve(
			final IdentityType type,
			final boolean onlyActive) {

		List<Identity> roots = repository.findByType(type, onlyActive);
		for (Identity root : roots) {
			root.setMetadata(metadata.getRootMetadata());
			retrieveChildren(root);
		}
		return roots;
	}

	public int retrieveChildrenCount(
			final Identity identity,
			final boolean onlyActive) {

		return repository.findChildren(identity != null ? identity.getId() : null, onlyActive).size();
	}

	public void delete(final Identity node) {
		node.setActive(false);
		repository.save(node);
	}
	
	private void retrieveChildren(
			final Identity root) {

		root.setChildren(repository.findChildren(root.getId(), true));
		for (Identity child : root.getChildren()) {
			child.setMetadata(metadata.getMetadata(child.getType()).orElse(null));
			retrieveChildren(child);
		}
	}
	
	public long count() {
		return this.repository.count();
	}
}
