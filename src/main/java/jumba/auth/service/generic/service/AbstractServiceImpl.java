package jumba.auth.service.generic.service;

import jumba.auth.service.generic.dao.AbstractBaseRepository;
import jumba.auth.service.generic.entity.LifeCyCleState;
import jumba.auth.service.generic.entity.LifeCycleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Judiao Mbaua
 *
 *         <p>
 *         Implementation of the generic repository
 *         </p>
 */
@Service
@Transactional
public class AbstractServiceImpl<T extends LifeCycleEntity<T>, ID extends Serializable>
		implements AbstractService<T, ID> {

	@Autowired
	private AbstractBaseRepository<T, ID> repository;

	@Override
	public Mono<T> create(T entity) {
		entity.setCreatedBy(null);
		entity.setActivatedBy(null);
		entity.setActive(LifeCyCleState.ACTIVE.isActive());
		entity.setState(LifeCyCleState.ACTIVE.getState());
		return repository.save(entity);
	}

	@Override
	public  Mono<T>  update( T entity) {
		entity.setUpdatedBy(null);
		entity.setUpdatedAt(LocalDateTime.now());
		return repository.save(entity);
	}

	@Override
	public void inactivate(T entity) {
		entity.setActivatedBy(null);
		entity.setActive(LifeCyCleState.INACTIVE.isActive());
		entity.setState(LifeCyCleState.INACTIVE.getState());
		repository.save(entity);

	}

	@Override
	public void activate( T entity) {
		entity.setActive(LifeCyCleState.ACTIVE.isActive());
		entity.setState(LifeCyCleState.ACTIVE.getState());
		repository.save(entity);

	}

	@Override
	public void delete( T entity) {
		entity.setActive(LifeCyCleState.DELETED.isActive());
		entity.setState(LifeCyCleState.DELETED.getState());
		repository.save(entity);

	}

	@Override
	public void banish( T entity) {
		entity.setActive(LifeCyCleState.BANNED.isActive());
		entity.setState(LifeCyCleState.BANNED.getState());
		repository.save(entity);

	}

}
