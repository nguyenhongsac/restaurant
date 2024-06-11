package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Bill;
import com.example.repository.BillRepository;

@Service
public class BillService {
	 @Autowired
	    private BillRepository billRepository;

	    public List<Bill> getAllBills() {
	        return billRepository.findAll();
	    }

	    public Bill getBillById(Integer id) {
	        return billRepository.findById(id).orElse(null);
	    }

	    public Bill saveBill(Bill bill) {
	        return billRepository.save(bill);
	    }

	    public void deleteBill(Integer id) {
	        billRepository.deleteById(id);
	    }
}
