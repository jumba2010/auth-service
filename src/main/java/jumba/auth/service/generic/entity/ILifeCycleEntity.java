package jumba.auth.service.generic.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ILifeCycleEntity<T> {
	
	public T getId() ;

	public void setId(T id) ;

	public UUID getCreatedBy();

	public void setCreatedBy(UUID createdBy) ;

	public UUID getUpdatedBy() ;

	public void setUpdatedBy(UUID updatedBy);

	public UUID getActivatedBy() ;
	
	public void setActivatedBy(UUID activatedBy) ;
	
	public int getState() ;

	public void setState(int state);

	public boolean isActive() ;

	public void setActive(boolean active) ;

	public Instant getCreatedAt() ;

	public void setCreatedAt(Instant createdAt) ;

	public Instant getUpdatedAt();

	public void setUpdatedAt(Instant updatedAt) ;

	public Instant getActivatedAt() ;

	public void setActivatedAt(Instant activatedAt) ;

}
