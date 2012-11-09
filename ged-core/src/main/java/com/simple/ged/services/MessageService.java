package com.simple.ged.services;

import java.util.List;

import com.simple.ged.dao.MessageDAO;
import com.simple.ged.models.GedMessage;

public class MessageService {

	/**
	 * Get messages, sorted by date desc
	 */
	public static synchronized List<GedMessage> getMessages() {
		return MessageDAO.getMessages();
	}
	
	/**
	 * Is there unread messages ?
	 */
	public static boolean thereIsUnreadMessages() {
		return MessageDAO.thereIsUnreadMessages();
	}
	
	/**
	 * Mark all messages as read
	 */
	public static void markAllMessagesAsRead() {
		MessageDAO.markAllMessagesAsRead();
	}
	
	/**
	 * Add some message
	 */
	public static void addMessage(GedMessage message) {
		MessageDAO.saveOrUpdate(message);
	}
	
}
