package it.ice.nttz.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class MysqlAdapter {
	private String database;
	private String username;
	private String password;
	private Connection connection;

	public MysqlAdapter(String database, String username, String password) {
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ database + "?user=" + username + "&password=" + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeSelectQuery(String query) {
		List results = null;
		String[] record;
		int colonne = 0;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			results = new Vector();
			ResultSetMetaData rsmd = rs.getMetaData();
			colonne = rsmd.getColumnCount();

			while (rs.next()) {
				record = new String[colonne];
				for (int i = 0; i < colonne; i++)
					record[i] = rs.getString(i + 1);
				results.add((String[]) record.clone());
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public void executeUpdateQuery(String query) {
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
