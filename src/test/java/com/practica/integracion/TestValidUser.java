package com.practica.integracion;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.practica.integracion.DAO.*;
import com.practica.integracion.manager.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.mockito.InOrder;
import org.mockito.InjectMocks;


@ExtendWith(MockitoExtension.class)

public class TestValidUser {
	
	@Mock
	private AuthDAO mockAuthDAO;
	
	@Mock
	private GenericDAO mockGenericDAO;
	
	private SystemManager systemManager;
	List<Object> roles = new ArrayList<Object>();
	private User userValido = new User ("1234", "Juan", "Garcia", "direccion", roles);
	
	
	@BeforeEach
	public void setUp() {
		roles.add("Jefe");
		userValido.setRoles(roles);
		systemManager = new SystemManager (mockAuthDAO, mockGenericDAO);
	}
	


	
	@DisplayName("Test startRemoteSystem")
	@Test
	public void testStartRemoteSystem() throws SystemManagerException, OperationNotSupportedException {
		

		//Simulación del comportamiento de authDAO
		when(mockAuthDAO.getAuthData(userValido.getId())).thenReturn(userValido);
		
		//Simulacion de comportamiento para GenericDAO
		List data = new ArrayList<>();
		data.add("dato");
		when(mockGenericDAO.getSomeData(userValido, "where id=0000")).thenReturn(data);
	
		//Probamos el metodo de SystemManager simulanco que el usuario es valido
		Collection<Object> res = systemManager.startRemoteSystem(userValido.getId(), "0000");
		
		//Se prueba que se ha producido 1 llamada a AuthDAO
		verify(mockAuthDAO, times(1)).getAuthData(userValido.getId());
		
		// Se prueba que se ha producido 1 llamada a GenericDAO
		verify(mockGenericDAO, times(1)).getSomeData(userValido, "where id=0000");
		
		//Se prueba que la llamada a Generic DAO es posterior a AuthDAO
		//despues el de  acceso a datos del sistema (la validaciones del orden en cada prueba)
		InOrder ordered = inOrder(mockAuthDAO, mockGenericDAO);
		// vemos si se ejecutan las llamadas a los dao, y en el orden correcto
		ordered.verify(mockAuthDAO).getAuthData(userValido.getId());
		ordered.verify(mockGenericDAO).getSomeData(userValido, "where id=0000");
		
		//Probar que el método de SystemManager ha devuelto información correctamente
		//System.out.println("Roles: " + userValido.getRoles());
		//System.out.println(res.toString());
		assertEquals(res.toString(), "[dato]");
	}

}
