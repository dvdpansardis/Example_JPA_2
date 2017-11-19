package br.com.caelum;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestaPool {

	public static void main(String[] args) throws PropertyVetoException, SQLException {
		
		ComboPooledDataSource dataSource = (ComboPooledDataSource)  new JpaConfigurator().getDataSource();
		
		Connection connection = dataSource.getConnection();
		
		System.out.println(dataSource.getNumBusyConnections());
		System.out.println(dataSource.getNumIdleConnections());
		
	}
	
}
