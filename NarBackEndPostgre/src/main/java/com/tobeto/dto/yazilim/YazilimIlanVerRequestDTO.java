package com.tobeto.dto.yazilim;

import lombok.Data;

@Data
public class YazilimIlanVerRequestDTO {
	private String isim;
	private String soyisim;
	private short yazilimDili;
	private String isTanimi;
	private String sure;
	private String ucret;
}
