package com.tobeto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Kullanicilar;
import com.tobeto.repository.KullanicilarRepository;

import jakarta.transaction.Transactional;

@Service
public class KullaniciService {
	@Autowired
	private KullanicilarRepository kullanicilarRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Optional<Kullanicilar> getKullanici(String adi) {
		Optional<Kullanicilar> kullanicilar = kullanicilarRepository.findByKullaniciAdi(adi);
		if (kullanicilar.isPresent()) {
			kullanicilar.get().getRollers();
		}
		return kullanicilar;
	}

	public boolean sifreDegistir(String eskiSifre, String yeniSifre, String adi) {
		Optional<Kullanicilar> kullanicilar = kullanicilarRepository.findByKullaniciAdi(adi);
		if (kullanicilar.isPresent()) {
			// kullanıcı, adına göre veritabanında bulundu.
			// şifresini kontrol edelim.
			Kullanicilar kullanici = kullanicilar.get();
			if (passwordEncoder.matches(eskiSifre, kullanici.getSifre())) {
				// şifresi doğru. Şifresini yeni şifre ile güncelleyelim.
				kullanici.setSifre(passwordEncoder.encode(yeniSifre));
				kullanicilarRepository.save(kullanici);
				return true;
			}
		}
		return false;
	}
}