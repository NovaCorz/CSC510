package FoodSeer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import FoodSeer.dto.RegisterRequestDto;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long   id;

    @Column ( nullable = false, unique = true )
    private String username;

    @Column ( nullable = false, unique = true )
    private String email;

    @Column ( nullable = false )
    private String password;

    @Builder.Default
    @Column ( nullable = false, length = 20 )
    private String role = "USER";

    public User ( final User o, final String pass ) {
        this.id = o.getId();
        this.username = o.getUsername();
        this.email = o.getEmail();
        this.password = pass;
        this.role = o.getRole();
    }

    public User ( final RegisterRequestDto o, final String pass ) {
        this.username = o.username();
        this.email = o.email();
        this.password = pass;
    }

}
