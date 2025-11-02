package FoodSeer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User role.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table ( name = "roles" )
public class Role {
    /**
     * The unique identifier for the role.
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long   id;
    /**
     * The name of the role (e.g., "ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_STAFF").
     */
    private String name;

}
