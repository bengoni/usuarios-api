package com.bienesraices.api.repository;




import com.bienesraices.api.model.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CobroRepository extends JpaRepository<Cobro, Long> {

    // Cobros de un usuario por su id
    List<Cobro> findByUsuario_Id(Long usuarioId);
}
