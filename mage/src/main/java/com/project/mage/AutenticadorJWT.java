package com.project.mage;

import javax.servlet.http.HttpServletRequest;

import java.security.Key;

import com.project.mage.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AutenticadorJWT {
	
	private static Key key = null; //Va a ser una clave que vamos a usar en cada logeo del usuario, diferente en cada ejecución
	
	//A partir de un usuario generamos un JWT
	public static String codificaJWT (Usuario u) {
		String jws = Jwts.builder().setSubject("" + u.getId()).
				signWith(SignatureAlgorithm.HS512, getGeneratedKey()).compact();
		return jws;
	}
	
	//Obtenemos el id del usuario a partir de un jwt
	public static int getIdUsuarioDesdeJWT (String jwt) {
		try {
			String stringIdUsuario = Jwts.parser().setSigningKey(getGeneratedKey()).parseClaimsJws(jwt).getBody().getSubject();
			int idUsuario = Integer.parseInt(stringIdUsuario);
			return idUsuario;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	
	//Obtenemos un id de un jwt recibido en un request
	public static int getIdUsuarioDesdeJwtIncrustadoEnRequest (HttpServletRequest request) {
		String autHeader = request.getHeader("Authorization");
		if (autHeader != null && autHeader.length() > 8) {
			String jwt = autHeader.substring(7);
			return getIdUsuarioDesdeJWT(jwt);
		}
		else {
			return -1; 
		}
	}
	
	//Generamos una clave nueva cada vez que iniciamos el servidor
	private static Key getGeneratedKey () {
		if (key == null) {
			key = MacProvider.generateKey();
		}
		return key;
	}

}