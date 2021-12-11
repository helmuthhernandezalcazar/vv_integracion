package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
public class TestValidUser {
	@Mock
	private static AuthDAO mockAuthDao;
	@Mock
	private static GenericDAO mockGenericDao;
	
	private User validUser;
	private InOrder ordered;
	private SystemManager manager;
	private String filter = "12345";
	private String data = "12345";
	private String id = "12345";
	
	@BeforeEach
	public void setUp() {
		validUser = new User("1", "Valido", "Valido", "Calle valida", 
				new ArrayList<Object>(Arrays.asList(1, 2)));
		
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		
		ordered = inOrder(mockAuthDao, mockGenericDao);
		
		manager = new SystemManager(mockAuthDao, mockGenericDao);
	}
	
	@Test
	public void startRemoteSystemValidUser() throws Exception {
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + filter)).thenReturn(lista);
		
		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), filter);
		assertEquals("[uno, dos]", retorno.toString());
		
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + filter);
	}

	@Test
	public void stopRemoteSystemValidUser() throws Exception {
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + filter)).thenReturn(lista);
		
		Collection<Object> retorno = manager.stopRemoteSystem(validUser.getId(), filter);
		assertEquals("[uno, dos]", retorno.toString());
		
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + filter);
	}
	
	@Test
	public void addRemoteSystemValidUserIsAddedTrue() throws Exception {
		when(mockGenericDao.updateSomeData(validUser, data)).thenReturn(true);
		
		manager.addRemoteSystem(validUser.getId(), data);
		
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, data);
	}
	
	@Test
	public void addRemoteSystemValidUserIsAddedFalse() throws Exception {
		when(mockGenericDao.updateSomeData(validUser, data)).thenReturn(false);
		
		assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(validUser.getId(), data);
		});
		
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, data);
	}
	
	@Test
	public void deleteRemoteSystemValidUserIsDeletedTrue() throws Exception {
		when(mockGenericDao.deleteSomeData(validUser, id)).thenReturn(true);
		
		manager.deleteRemoteSystem(validUser.getId(), id);
		
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).deleteSomeData(validUser, id);
	}
	
	@Test
	public void deleteRemoteSystemValidUserIsDeletedFalse() throws Exception {
		when(mockGenericDao.deleteSomeData(validUser, id)).thenReturn(false);
		
		assertThrows(SystemManagerException.class, () -> {
			manager.deleteRemoteSystem(validUser.getId(), id);
		});

		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).deleteSomeData(validUser, id);
	}
}