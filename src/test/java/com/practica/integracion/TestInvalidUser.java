package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.OperationNotSupportedException;

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
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {

	@Mock
	private static AuthDAO mockAuthDAO;
	@Mock
	private static GenericDAO mockGenericDAO;
	
	private User invalidUser;
	
	private InOrder ordered;
	
	private SystemManager manager;
	@BeforeEach
	public void setUp() {
		invalidUser = new User("1", "Helmuth", "Hernández", "Madrid", new ArrayList<Object>(Arrays.asList(3)));
		when(mockAuthDAO.getAuthData(invalidUser.getId())).thenReturn(invalidUser);
		ordered = inOrder(mockAuthDAO, mockGenericDAO);
		manager = new SystemManager(mockAuthDAO, mockGenericDAO);
	}
	
	@Test
	@DisplayName("Test Start Remote System")
	public void testStartRemoteSystemWithInvalidUserAndSystem() throws Exception {
	
		String validId = "1234";
		when(mockGenericDAO.getSomeData(invalidUser, "where id=" + validId)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, ()-> {
			manager.startRemoteSystem(invalidUser.getId(), validId);
		});
		
		ordered.verify(mockAuthDAO).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDAO).getSomeData(invalidUser,  "where id=" + validId);
		
	}
	
	@Test
	@DisplayName("Test Stop Remote System")
	public void testStopRemoteSystemWithInvalidUserAndSystem() throws Exception {

		String validId = "1234";
		when(mockGenericDAO.getSomeData(invalidUser, "where id=" + validId)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, ()-> {
			manager.stopRemoteSystem(invalidUser.getId(), validId);
		});
		
		ordered.verify(mockAuthDAO).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDAO).getSomeData(invalidUser,  "where id=" + validId);
		
	}
	
	@Test
	@DisplayName("Test Add Remote System")
	public void testAddRemoteSystemWithInvalidUserAndSystem() throws Exception {
		String data = "data";
		when(mockGenericDAO.updateSomeData(invalidUser, data)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, ()-> {
			manager.addRemoteSystem(invalidUser.getId(), data);
		});
		
		ordered.verify(mockAuthDAO).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDAO).updateSomeData(invalidUser, data);
	}
	
	@Test
	@DisplayName("Test Delete Remote System")
	public void testDeleteRemoteSystemWithInvalidUserAndSystem() throws Exception {
		String validId = "1234";
		when(mockGenericDAO.deleteSomeData(invalidUser, validId)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, ()-> {
			manager.deleteRemoteSystem(invalidUser.getId(), validId);
		});
		
		ordered.verify(mockAuthDAO).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDAO).deleteSomeData(invalidUser, validId);
	}
}
