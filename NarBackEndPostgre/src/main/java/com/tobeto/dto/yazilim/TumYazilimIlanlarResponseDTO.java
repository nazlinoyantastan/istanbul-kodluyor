package com.tobeto.dto.yazilim;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class TumYazilimIlanlarResponseDTO {
	private UUID id;
	private String isTanimi;
	private String isim;
	private String soyisim;
	private String sure;
	private BigDecimal ucret;
	private short yazilimDili;
}