package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	@Mock
	private static AuthDAO mockAuthDAO;
	@Mock
	private static GenericDAO mockGenericDAO;
	
	private User validUser;
	
	private InOrder ordered;
	
	private SystemManager manager;
	
	@BeforeEach
	public void setUp() {
		validUser = new User("1", "Helmuth", "Hernández", "Madrid", new ArrayList<Object>(Arrays.asList(1,2)));
		when(mockAuthDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		ordered = inOrder(mockAuthDAO, mockGenericDAO);
		manager = new SystemManager(mockAuthDAO, mockGenericDAO);
	}
	
	@Test
	@DisplayName("Test Start Remote System")
	public void testStartRemoteSystemWithValidUserAndSystem() throws Exception {

		String validId = "1234";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDAO.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);

		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);
		assertEquals(retorno.toString(), "[uno, dos]");
		
		ordered.verify(mockAuthDAO).getAuthData(validUser.getId());
		ordered.verify(mockGenericDAO).getSomeData(validUser,  "where id=" + validId);
			
	}
	
	@Test
	@DisplayName("Test Stop Remote System")
	public void testStopRemoteSystemWithValidUserAndSystem() throws Exception {

		String validId = "1234";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDAO.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);
			
		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);
		assertEquals(retorno.toString(), "[uno, dos]");
		
		ordered.verify(mockAuthDAO).getAuthData(validUser.getId());
		ordered.verify(mockGenericDAO).getSomeData(validUser,  "where id=" + validId);
			
	}
	
	@Test
	@DisplayName("Test Add Remote System")
	public void testAddRemoteSystemWithValidUserAndSystem() throws Exception {
		
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDAO.updateSomeData(validUser, lista)).thenReturn(true);
		
		manager.addRemoteSystem(validUser.getId(), lista);
		
		ordered.verify(mockAuthDAO).getAuthData(validUser.getId());
		ordered.verify(mockGenericDAO).updateSomeData(validUser, lista);
		
	}
	
	@Test
	@DisplayName("Test Delete Remote System")
	public void testDeleteRemoteSystemWithValidUserAndSystem() throws Exception {
		String validId = "1234";
		when(mockGenericDAO.deleteSomeData(validUser, validId)).thenReturn(true);
		
		manager.deleteRemoteSystem(validUser.getId(), validId);
		
		ordered.verify(mockAuthDAO).getAuthData(validUser.getId());
		ordered.verify(mockGenericDAO).deleteSomeData(validUser, validId);
		
	}
}
