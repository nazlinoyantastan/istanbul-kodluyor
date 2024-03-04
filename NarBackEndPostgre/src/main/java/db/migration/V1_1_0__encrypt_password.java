package db.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class V1_1_0__encrypt_password extends BaseJavaMigration {
	@Override
	public void migrate(Context context) throws Exception {
		PasswordEncoder encoder = new BCryptPasswordEncoder();

		try (PreparedStatement kullanicilar = context.getConnection()
				.prepareStatement("select * from kullanicilar")) {
			try (ResultSet kullanicilarRs = kullanicilar.executeQuery()) {
				while (kullanicilarRs.next()) {
					String clearPassword = kullanicilarRs.getString("sifre");
					UUID id = (UUID) kullanicilarRs.getObject("id");

					try (PreparedStatement kullaniciSifreUpdate = context.getConnection()
							.prepareStatement("update kullanicilar set sifre = ? where id = ?")) {
						kullaniciSifreUpdate.setString(1, encoder.encode(clearPassword));
						kullaniciSifreUpdate.setObject(2, id);
						kullaniciSifreUpdate.execute();
					}
				}
			}
		}
	}
}
