package jumba.auth.service.generic.entity;

import jumba.auth.service.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Data
public abstract class LifeCycleEntity<T>  implements ILifeCycleEntity<T>, Serializable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "UUID")
	private T id;

	@Column(name="created_by",nullable=false)
	private UUID createdBy;
	
	@Column(name="updated_by")
	private UUID updatedBy;
	
	@Column(name="activated_by",nullable=false)
	private UUID activatedBy;
	
	@Column(name="state",nullable=false)
	private int state;
	
	@Column(name="active",nullable=false)
	private boolean active;
	
	@Column(name="created_at",nullable=false)
	private Instant createdAt;
	
	@Column(name="updated_at")
	private Instant updatedAt;

	@Column(name="activated_at",nullable=false)
	private Instant activatedAt;
	
	@Column(name="market_id")
	private Long marketId;

	@Column(name="territory_id")
	private Long territoryId;

	@PrePersist
	void createdAt() {
		this.createdAt=Instant.now();
		this.activatedAt=Instant.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LifeCycleEntity)) {
			return false;
		}
		return id != null && id.equals(((LifeCycleEntity) o).id);
	}
}
