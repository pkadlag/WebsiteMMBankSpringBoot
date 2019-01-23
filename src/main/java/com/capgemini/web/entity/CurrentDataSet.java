
package com.capgemini.web.entity;

import java.util.List;
import org.springframework.hateoas.Link;

public class CurrentDataSet {

	private List<Transaction> transactions;
	private Link previousLink;
	private Link nextLink;

	public CurrentDataSet() {
	}

	public CurrentDataSet(List<Transaction> transactions, Link previousLink, Link nextLink) {
		super();
		this.transactions = transactions;
		this.previousLink = previousLink;
		this.nextLink = nextLink;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Link getPreviousLink() {
		return previousLink;
	}

	public void setPreviousLink(Link previousLink) {
		this.previousLink = previousLink;
	}

	public Link getNextLink() {
		return nextLink;
	}

	public void setNextLink(Link nextLink) {
		this.nextLink = nextLink;
	}

	@Override
	public String toString() {
		return "CurrentDataSet [transactions=" + transactions + ", previousLink=" + previousLink + ", nextLink="
				+ nextLink + "]";
	}

}
