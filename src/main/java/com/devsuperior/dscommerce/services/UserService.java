package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    //Metodo que retorna o usuário autenticado
    //SecurityContextHolder guarda informações da segurança da sessão atual
    //O principal é um token JWT que carrega as informações do usuário, como claims(Atributos)
    //Por fim, é atribuido a variavel username o valor coletado do JWT Claim chamado username
    protected User authenticated(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");

            return userRepository.findByEmail(username).get();
        }
        catch(Exception e){
            throw new UsernameNotFoundException("Email not found");
        }
    }
    @Transactional(readOnly = true)
    public UserDTO getMe(){
        User user = authenticated();
        return new UserDTO(user);
    }
}
