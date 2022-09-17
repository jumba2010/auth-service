package jumba.auth.service.generic.service;

import jumba.auth.service.generic.entity.LifeCyCleState;
import jumba.auth.service.generic.entity.LifeCycleEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


/**
 * @author Judiao Mbaua
 *
 */
public interface AbstractQueryService<T extends LifeCycleEntity<T>, ID extends UUID> {

Mono<T> findById(ID id);

Flux<T> findAll();

Flux<T> findByActiveAndState(LifeCyCleState lifeCyCleState);


}
