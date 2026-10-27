package com.rapik.example.aplikasidatabase.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class MemberInput {
    private MemberInput() {}

    public static String validate(String name, String studentClass, String gender, String birth) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            return "Nama wajib diisi, maksimal 100 karakter.";
        }
        if (studentClass == null || studentClass.trim().isEmpty()) return "Pilih kelas.";
        if (!"Male".equals(gender) && !"Female".equals(gender)) return "Pilih jenis kelamin.";
        if (birth == null || !birth.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            return "Tanggal lahir harus yyyy-MM-dd.";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false);
        try {
            Date date = format.parse(birth);
            if (date == null || date.after(new Date())) return "Tanggal lahir tidak valid.";
        } catch (ParseException error) {
            return "Tanggal lahir tidak valid.";
        }
        return null;
    }
}
