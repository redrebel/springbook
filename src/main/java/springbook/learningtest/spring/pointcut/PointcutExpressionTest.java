package springbook.learningtest.spring.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class PointcutExpressionTest {
	@Test
	public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public int " +
				"springbook.learningtest.spring.pointcut.Target.minus(int,int) " +
				"throws java.lang.RuntimeException)");
		
		// Target.minus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
				pointcut.getMethodMatcher().matches(
						Target.class.getMethod("minus", int.class, int.class), null), is(true));
		
		// Target.plus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
				pointcut.getMethodMatcher().matches(
						Target.class.getMethod("plus", int.class, int.class), null), is(false));
		
		// Bean.method()
		assertThat(pointcut.getClassFilter().matches(Bean.class) &&
				pointcut.getMethodMatcher().matches(
						Target.class.getMethod("method", int.class, int.class), null), is(false));
	}
}
