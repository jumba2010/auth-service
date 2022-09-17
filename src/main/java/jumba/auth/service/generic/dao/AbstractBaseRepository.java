package jumba.auth.service.generic.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.io.Serializable;

/**
 * @author Judiao Mbaua
 * 
 *        <p> This a generic repository for all crud operations</p>
 *        @NoRepositoryBean annotation avoids JPA for finding an implementation for the repository
 *
 */
@NoRepositoryBean
public interface AbstractBaseRepository<T,  ID extends Serializable>
		extends ReactiveCrudRepository<T, ID> {

	Flux<T> findByActiveAndState(boolean active, int state);

}
