package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
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
	private static AuthDAO mockAuthDao;
	@Mock
	private static GenericDAO mockGenericDao;
	
	private User invalidUser;
	private InOrder ordered;
	private SystemManager manager;
	private String filter = "12345";
	private String data = "12345";
	private String id = "12345";
	
	@BeforeEach
	public void setUp() {
		invalidUser = new User("10", "Invalido", "Invalido", "Calle invalida", 
				new ArrayList<Object>(Arrays.asList(10, 11)));

		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(invalidUser);
		
		ordered = inOrder(mockAuthDao, mockGenericDao);
		
		manager = new SystemManager(mockAuthDao, mockGenericDao);
	}
	
	@Test
	public void startRemoteSystemInvalidUser() throws Exception {
		when(mockGenericDao.getSomeData(invalidUser, "where id=" + filter)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, () -> {
			Collection<Object> retorno = manager.startRemoteSystem(invalidUser.getId(), filter);
		});
		
		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).getSomeData(invalidUser, "where id=" + filter);
	}

	@Test
	public void stopRemoteSystemInvalidUser() throws Exception {
		when(mockGenericDao.getSomeData(invalidUser, "where id=" + filter)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, () -> {
			Collection<Object> retorno = manager.stopRemoteSystem(invalidUser.getId(), filter);
		});
		
		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).getSomeData(invalidUser, "where id=" + filter);
	}
	
	@Test
	public void addRemoteSystemInvalidUser() throws Exception {
		when(mockGenericDao.updateSomeData(invalidUser, data)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(invalidUser.getId(), data);
		});
		
		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(invalidUser, data);
	}
	
	@Test
	public void deleteRemoteSystemInvalidUser() throws Exception {
		when(mockGenericDao.deleteSomeData(invalidUser, id)).thenThrow(OperationNotSupportedException.class);
		
		assertThrows(SystemManagerException.class, () -> {
			manager.deleteRemoteSystem(invalidUser.getId(), id);
		});

		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).deleteSomeData(invalidUser, id);
	}
}