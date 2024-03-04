package db.migration;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUsers extends BaseJavaMigration {
	List<String[]> kullanicilar = Arrays
			.asList(new String[][] { { "levent", "levent" }, { "test", "test" } });

	@Override
	public void migrate(Context context) throws Exception {
		String sql = "insert into kullanicilar values (?, ?, ?)";
		kullanicilar.forEach(kullanici -> {
			try (PreparedStatement insert = context.getConnection().prepareStatement(sql)) {
				insert.setBytes(1, convert(UUID.randomUUID()));
				insert.setString(2, kullanici[0]);
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				insert.setString(3, encoder.encode(kullanici[1])); // password
				insert.execute();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private byte[] convert(UUID uuid) {
		byte[] uuidBytes = new byte[16];
		ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(uuid.getMostSignificantBits())
				.putLong(uuid.getLeastSignificantBits());
		return uuidBytes;
	}
}
