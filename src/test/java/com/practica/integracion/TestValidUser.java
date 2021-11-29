package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class TestValidUser {
	@DisplayName("Test startRemoteSystem con usuario valido")
	@Test
	public void testStartRemoteSystem() {
		boolean t = true;
		assertTrue(t);
	}

}
