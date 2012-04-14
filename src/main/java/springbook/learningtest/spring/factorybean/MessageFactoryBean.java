package springbook.learningtest.spring.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
	
	String text;
	
	public void setText(String text){
		this.text = text;
	}

	public Message getObject() throws Exception {
		// TODO Auto-generated method stub
		return Message.newMessage(this.text);
	}

	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
