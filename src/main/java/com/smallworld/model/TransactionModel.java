package com.smallworld.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
	    public int mtn;
	    public double amount;
	    public String senderFullName;
	    public int senderAge;
	    public String beneficiaryFullName;
	    public int beneficiaryAge;
	    public int issueId;
	    public boolean issueSolved;
	    public String issueMessage;
}
