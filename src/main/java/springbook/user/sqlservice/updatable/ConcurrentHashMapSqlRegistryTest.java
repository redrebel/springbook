package springbook.user.sqlservice.updatable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.user.sqlservice.SqlNotFoundException;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{
	protected UpdatableSqlRegistry createUpdatableSqlRegistry(){
		return new ConcurrentHashMapSqlRegistry();
	}
}
