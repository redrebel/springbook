package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {
	private Map<String , String > sqlMap = new HashMap<String, String>();
	
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlNotFoundException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		else
			return sql;
	}

}
