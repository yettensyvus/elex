package com.yettensyvus.elex.service;

import com.yettensyvus.elex.domain.Order;
import com.yettensyvus.elex.domain.Seller;
import com.yettensyvus.elex.domain.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getTransactionsBySellerId(Seller seller);

    List<Transaction> getAllTransactions();
}
