package com.java.junit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smallworld.TransactionDataFetcher;
import com.smallworld.model.TransactionModel;

public class TransactionProcessorTest {

	private double totalTransactionAmountSentBy;
	private double getMaxtransactionamount;
	private long uniqueClientCount;
	private boolean hasOpencomplianceissues;
	private Map<String, List<TransactionModel>> gettransactionsbybeneficiaryname;
	private List<Integer> getunsolvedissueIds;
	private List<String> getallsolvedIssuemessages;
	private List<TransactionModel> getTop3transactionsbyamount;
	private Optional<Object> gettopsender;
	Path filePath = null;
	String jsonContent = null;
	double totalTransactionAmount = 0;
	TransactionDataFetcher transaction = null;

	public TransactionProcessorTest() {

		filePath = Path.of("transactions.json");
		try {
			jsonContent = Files.lines(filePath, StandardCharsets.UTF_8).collect(Collectors.joining());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			transaction = new TransactionDataFetcher(jsonContent);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void getTotalTransactionAmount() throws JsonProcessingException {

		totalTransactionAmount = transaction.getTotalTransactionAmount();
		// verify that the Transaction Amount Sent by is correct
		assertEquals(4371.37, totalTransactionAmount);

	}

	@Test
	public void totalTransactionAmountSentBy() throws JsonProcessingException {

		totalTransactionAmountSentBy = transaction.getTotalTransactionAmountSentBy("Grace Burgess");
		// verify that the Transaction Amount Sent by is correct
		assertEquals(1998.0, totalTransactionAmountSentBy);

	}

	@Test
	public void getMaxtransactionamount() throws JsonProcessingException {
		getMaxtransactionamount = transaction.getMaxTransactionAmount();
		// verify that the Maximum Transaction Amount is correct
		assertEquals(985.0, getMaxtransactionamount);
	}

	@Test
	public void getuniqueClientCount() throws JsonProcessingException {
		uniqueClientCount = transaction.countUniqueClients();
		// verify that the Unique Client Count
		assertEquals(5, uniqueClientCount);
	}

	@Test
	public void getOpencomplianceissues() throws JsonProcessingException {

		hasOpencomplianceissues = transaction.hasOpenComplianceIssues("Arthur Shelby");
		// verify that has open compliance issue.
		assertEquals(true, hasOpencomplianceissues);

	}

	@Test
	public void gettransactionsbybeneficiaryname() throws JsonProcessingException {
		gettransactionsbybeneficiaryname = transaction.getTransactionsByBeneficiaryName();
		// verify that has open compliance issue.
		assertEquals("Luca Changretta",
				gettransactionsbybeneficiaryname.get("Luca Changretta").get(0).getBeneficiaryFullName());
	}

	@Test
	public void getunsolvedissueIds() throws JsonProcessingException {
		getunsolvedissueIds = transaction.getUnsolvedIssueIds();
		// verify that has open compliance issue.
		assertEquals(Arrays.asList(1, 3, 15, 54, 99), getunsolvedissueIds);
	}

	@Test
	public void getallsolvedIssuemessages() throws JsonProcessingException {
		getallsolvedIssuemessages = transaction.getAllSolvedIssueMessages();
		// verify that has open compliance issue.
		assertEquals(Arrays.asList("Never gonna give you up", null, "Never gonna let you down", null,
				"Never gonna run around and desert you", null, null, null), getallsolvedIssuemessages);
	}

	@Test
	public void getTop3transactionsbyamount() throws JsonProcessingException {
		getTop3transactionsbyamount = transaction.getTop3TransactionsByAmount();

		TransactionModel t1 = new TransactionModel(32612651, 666.0, "Grace Burgess", 31, "Michael Gray", 58, 54, false,
				"Something ain't right");
		TransactionModel t2 = new TransactionModel(32612651, 666.0, "Grace Burgess", 31, "Michael Gray", 58, 78, true,
				"Never gonna run around and desert you");
		TransactionModel t3 = new TransactionModel(5465465, 985.0, "Arthur Shelby", 60, "Ben Younger", 47, 15, false,
				"Something's fishy");

		assertEquals(Arrays.asList(t3, t1, t2), getTop3transactionsbyamount);
	}

	@Test
	public void gettopsender() throws JsonProcessingException {
		gettopsender = transaction.getTopSender();
		// verify that has open compliance issue.
		assertEquals(Optional.of("Grace Burgess"), gettopsender);

	}

}