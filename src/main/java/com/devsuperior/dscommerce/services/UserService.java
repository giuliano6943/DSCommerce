package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService { //Implementa a interface obrigatoria do SpringSecurity

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username); // Atribuindo a uma lista da projection o resultado do metodo searchUserAndRolesByEmail
        if(result.size() == 0){ //Se o resultado for vazio, rodar uma exception dizendo q o usuário nao foi encontrado
            throw new UsernameNotFoundException("User not found");
        }
        //Se existir resultado, instancia um novo usuário e atribuir a ele as variáveis coletadas no result
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for(UserDetailsProjection projection : result){ //Atribuindo as roles ao usuário passado como parametro chamando o addRole
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }
}
