package com.smallworld;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.model.TransactionModel;

public class TransactionDataFetcher {
	
    /*TransactionDataFetcher Class is a set of examples 
     * to use the JAVA 8 features of Stream API Collections
     * use the declarative Sql like approach to manipulate 
     * and filter the Collections.
     * */	
	
	public TransactionModel[] transmodel;
	public List<TransactionModel> transactions;
	
	/**
	 * TransactionDataFetcher constructor
	 * **/
	public TransactionDataFetcher(String myJsonString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		this.transmodel = om.readValue(myJsonString, TransactionModel[].class);
		this.transactions = Arrays.asList(transmodel);
	}

	/**
	 * Returns the sum of the amounts of all transactions
	 * 
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public double getTotalTransactionAmount() throws JsonMappingException, JsonProcessingException {

		double transactionAmount = 0;

		for (TransactionModel trans : transmodel) {
			System.out.println(trans);
			transactionAmount += trans.amount;
		}
		return transactionAmount;

	}
	/**
	 * Returns the sum of the amounts of all transactions sent by the specified
	 * client
	 */
	public double getTotalTransactionAmountSentBy(String senderFullName) {

		double totalAmount = transactions
				.stream()
				.filter(t -> t.getSenderFullName().equals(senderFullName))
				.mapToDouble(TransactionModel::getAmount)
				.sum();

		return totalAmount;
	}

	/**
	 * Returns the highest transaction amount
	 */
	public double getMaxTransactionAmount() {

		double maxAmount = transactions
				.stream()
				.mapToDouble(TransactionModel::getAmount)
				.max()
				.orElse(0.0);
		return maxAmount;

	}

	/**
	 * Counts the number of unique clients that sent or received a transaction
	 */
	public long countUniqueClients() {

		long uniqueClients = transactions
				.stream()
				.map(TransactionModel::getSenderFullName)
				.distinct()
				.count();
		return uniqueClients;
	}

	/**
	 * Returns whether a client (sender or beneficiary) has at least one transaction
	 * with a compliance issue that has not been solved
	 */
	public boolean hasOpenComplianceIssues(String clientFullName) {

		boolean hasUnsolvedIssues = transactions.stream()
				.filter(t -> t.getSenderFullName().equals(clientFullName))
				.anyMatch(c -> !c.isIssueSolved());

		if (hasUnsolvedIssues) {
			return true;
		} else {
			return false;
		}
	}
    //Helper Method
	public static Map<String, List<TransactionModel>> 
			indexTransactionsByBeneficiary(
			List<TransactionModel> transactions) {
		
			return transactions.stream()
				.collect(Collectors
						.groupingBy(TransactionModel::getBeneficiaryFullName));
	}

	/**
	 * Returns all transactions indexed by beneficiary name
	 * 
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public Map<String, List<TransactionModel>> getTransactionsByBeneficiaryName()
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper om = new ObjectMapper();
		Map<String, List<TransactionModel>> transactionsByBeneficiary = indexTransactionsByBeneficiary(transactions);

		Set<?> t = transactionsByBeneficiary.entrySet();

		Iterator<?> itr = t.iterator();
		while (itr.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry me = (Map.Entry) itr.next();
			System.out.print(me.getKey() + ":\n");
			
			String jsonInString = om.writeValueAsString(me.getValue());
			System.out.println(jsonInString);
		}
		
		return transactionsByBeneficiary;
	}

	/**
	 * Returns the identifiers of all open compliance issues
	 */
	public List<Integer> getUnsolvedIssueIds() {

		List<Integer> issueId = (List<Integer>) transactions
				.stream().filter(t -> !t.isIssueSolved())
				.map(c -> c.getIssueId())
				.collect(Collectors.toList());
		return issueId;
	}

	/**
	 * Returns a list of all solved issue messages
	 */
	public List<String> getAllSolvedIssueMessages() {

		List<String> issueMessage = transactions.stream()
				.filter(t -> t.isIssueSolved())
				.map(c -> c.getIssueMessage())
				.toList();
		return issueMessage;
	}

	/**
	 * Returns the 3 transactions with highest amount sorted by amount descending
	 */
	public List<TransactionModel> getTop3TransactionsByAmount() {

		List<TransactionModel> top3Transactions = transactions.stream()
				.sorted(Comparator.comparingDouble(TransactionModel::getAmount)
				.reversed())
				.limit(3)
				.collect(Collectors.toList());
		return top3Transactions;
	}

	/**
	 * Returns the sender with the most total sent amount
	 */
	public Optional<Object> getTopSender() {
		Map<String, Double> senderAmountMap = transactions
				.stream()
				.collect(Collectors.groupingBy(TransactionModel::getSenderFullName,
				 Collectors.summingDouble(TransactionModel::getAmount)));

		return Optional
				.of(senderAmountMap.entrySet()
						.stream()
						.sorted(Map.Entry.<String, Double>comparingByValue()
						.reversed())
						.findFirst()
						.map(Map.Entry::getKey)
						.orElse(null));
	}

	public static void main(String[] args) throws Exception {
		
		double totalTransactionAmount = 0;
		double totalTransactionAmountSentBy = 0;
		long uniqueClientCount = 0;
		double getMaxtransactionamount = 0;
		boolean hasOpencomplianceissues = false;
		
		Map<String, List<TransactionModel>> gettransactionsbybeneficiaryname = null;
		List<Integer> getunsolvedissueIds = null;
		List<String> getallsolvedIssuemessages = null;
		List<TransactionModel> getTop3transactionsbyamount = null;
		Optional<Object> gettopsender = null;

		/* Read the transactions.json file using 
		 * StandardCharSets UTF8 from the path as follows
		 * */
		
		Path filePath = Path.of("transactions.json");
		String jsonContent = Files.lines(filePath, StandardCharsets.UTF_8).collect(Collectors.joining());
		TransactionDataFetcher transaction = new TransactionDataFetcher(jsonContent);
	
		totalTransactionAmount = transaction.getTotalTransactionAmount();
		System.out.println("totalTransactionAmount");
		System.out.println(totalTransactionAmount);

		totalTransactionAmountSentBy = transaction.getTotalTransactionAmountSentBy("Grace Burgess");
		System.out.println("totalTransactionAmountSentBy");
		System.out.println(totalTransactionAmountSentBy);

		getMaxtransactionamount = transaction.getMaxTransactionAmount();
		System.out.println("getMaxtransactionamount");
		System.out.println(getMaxtransactionamount);

		uniqueClientCount = transaction.countUniqueClients();
		System.out.println("uniqueClientCount");
		System.out.println(uniqueClientCount);

		hasOpencomplianceissues = transaction.hasOpenComplianceIssues("Arthur Shelby");
		System.out.println("hasOpenComplianceIssues");
		System.out.println(hasOpencomplianceissues);

		gettransactionsbybeneficiaryname = transaction.getTransactionsByBeneficiaryName();
		System.out.println("getTransactionsByBeneficiaryName");
		// System.out.println(gettransactionsbybeneficiaryname.toString());

		getunsolvedissueIds = transaction.getUnsolvedIssueIds();
		System.out.println("getUnsolvedIssueIds");
		System.out.println(getunsolvedissueIds);

		getallsolvedIssuemessages = transaction.getAllSolvedIssueMessages();
		System.out.println("getallsolvedIssuemessages");
		System.out.println(getallsolvedIssuemessages);

		getTop3transactionsbyamount = transaction.getTop3TransactionsByAmount();
		System.out.println("getTop3TransactionsByAmount");
		System.out.println(getTop3transactionsbyamount);

		gettopsender = transaction.getTopSender();
		System.out.println("getTopSender");
		System.out.println(gettopsender.stream().toList());
	}
}
