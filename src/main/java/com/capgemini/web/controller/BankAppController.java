package com.capgemini.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.capgemini.web.entity.CurrentDataSet;
import com.capgemini.web.entity.Transaction;

@Controller
public class BankAppController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdrawForm() {
		return "Withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdraw(@ModelAttribute Transaction transaction, Model model) {
		restTemplate.postForEntity("http://localhost:9090/transactions/withdraw", transaction, null);
		model.addAttribute("message", "Success!");
		return "Withdraw";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String depositForm() {
		return "DepositForm";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public String deposit(@ModelAttribute Transaction transaction, Model model) {
		restTemplate.postForEntity("http://localhost:9090/transactions", transaction, null);
		model.addAttribute("message", "Success!");
		return "DepositForm";
	}

	@RequestMapping(value = "/fundTransfer", method = RequestMethod.GET)
	public String fundTransferForm() {
		return "fundTransfer";
	}

	@RequestMapping(value = "/fundTransfer", method = RequestMethod.POST)
	public String fundTransfer(@RequestParam("senderAccountNo") int senderAccountNo,
			@RequestParam("receiverAccountNo") int receiverAccountNo, @RequestParam("amount") double amount,
			Model model) {
		Transaction senderTransaction = new Transaction(senderAccountNo, amount);
		restTemplate.postForEntity("http://localhost:9090/transactions/withdraw", senderTransaction, null);
		Transaction receiverTransaction = new Transaction(receiverAccountNo, amount);
		restTemplate.postForEntity("http://localhost:9090/transactions", receiverTransaction, null);
		model.addAttribute("message", "Success!");
		return "fundTransfer";
	}

	@RequestMapping("/statement")
	public ModelAndView getStatement(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		int currentSize = size == 0 ? 5 : size;
		int currentOffset = offset == 0 ? 1 : offset;
		Link previous = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class)
				.getStatement(currentOffset - currentSize, currentSize)).withRel("previous");
		Link next = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class)
				.getStatement(currentOffset + currentSize, currentSize)).withRel("next");
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://localhost:9090/transactions",
				CurrentDataSet.class);
		List<Transaction> transactionList = currentDataSet.getTransactions();
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (int value = currentOffset - 1; value < currentOffset + currentSize - 1; value++) {
			if ((transactionList.size() <= value && value > 0) || currentOffset < 1)
				break;
			Transaction transaction = transactionList.get(value);
			transactions.add(transaction);
		}
		currentDataSet.setPreviousLink(previous);
		currentDataSet.setNextLink(next);
		currentDataSet.setTransactions(transactions);
		return new ModelAndView("DepositForm", "currentDataSet", currentDataSet);
	}

}
