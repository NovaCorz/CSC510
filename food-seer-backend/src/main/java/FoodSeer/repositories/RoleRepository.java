package FoodSeer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import FoodSeer.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName ( String name );
}
