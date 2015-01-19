package it.ice.nttz.test;

import it.ice.nttz.persistence.MysqlAdapter;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MysqlAdapterTest {
	private MysqlAdapter mysql;

	@Before
	public void setUp() {
		mysql = new MysqlAdapter("tabularasa", "root", "burp");
		mysql.connect();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testRetrieveAll() {
		List<String[]> relations = mysql
				.executeSelectQuery("select * from relations");
		for (String[] relation : relations) {
			System.out.println(relation[0] + " " + relation[1]);
		}
	}

	@After
	public void tearDown() {
		mysql.disconnect();
	}
}
