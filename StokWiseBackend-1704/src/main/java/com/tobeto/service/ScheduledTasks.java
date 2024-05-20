package com.tobeto.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tobeto.repository.user.UserRepository;
import com.tobeto.repository.warehouse.ProductRepository;

@Service
public class ScheduledTasks {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

//	@Scheduled(fixedRate = 2592000000L) // 30 günde bir çalışacak (30 gün * 24 saat * 60 dakika * 60 saniye * 1000
//										// milisaniye)
//  fixedDelay → program çalışır sonra boşluk koyar
//  fixedRate → her belirtilen sürede bir çalışır	

//	@Scheduled(fixedRateString = "P1M") // 30 günde bir çalışacak. Bu yazım şekli 1 Ay'a denk gelir. → HATA VERDİ

	@Scheduled(cron = "0 0 0 1 * ?") // Her Ay'ın 1'inde gece yarısında çalışır. (00:00)
	@Transactional
	public void deleteOldSoftDeletedUsers() {
		LocalDateTime cutoffTime = LocalDateTime.now().minusYears(1);
		userRepository.deleteOldSoftDeletedUsers(cutoffTime);
	}

	@Scheduled(cron = "0 0 0 1 * ?") // Her Ay'ın 1'inde gece yarısında çalışır. (00:00)
	@Transactional
	public void deleteOldSoftDeletedProducts() {
		LocalDateTime cutoffTime = LocalDateTime.now().minusYears(1);
		productRepository.deleteOldSoftDeletedProducts(cutoffTime);
	}

}