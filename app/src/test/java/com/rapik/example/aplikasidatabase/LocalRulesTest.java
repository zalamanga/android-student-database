package com.rapik.example.aplikasidatabase;

import com.rapik.example.aplikasidatabase.config.MemberInput;
import com.rapik.example.aplikasidatabase.config.PasswordHasher;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalRulesTest {
    @Test public void saltedHashesAcceptOnlyTheOriginalPassword() {
        String first = PasswordHasher.hash("local-demo-123");
        String second = PasswordHasher.hash("local-demo-123");
        assertNotEquals(first, second);
        assertTrue(PasswordHasher.verify("local-demo-123", first).matches);
        assertFalse(PasswordHasher.verify("wrong-demo-123", first).matches);
        assertNull(PasswordHasher.verify("local-demo-123", first).replacementHash);
    }

    @Test public void legacyPasswordUpgradesOnlyAfterItMatches() {
        PasswordHasher.Verification correct = PasswordHasher.verify("legacy-demo", "legacy-demo");
        assertTrue(correct.matches);
        assertNotNull(correct.replacementHash);
        assertNotEquals("legacy-demo", correct.replacementHash);
        assertTrue(PasswordHasher.verify("legacy-demo", correct.replacementHash).matches);
        PasswordHasher.Verification wrong = PasswordHasher.verify("wrong-demo", "legacy-demo");
        assertFalse(wrong.matches);
        assertNull(wrong.replacementHash);
    }

    @Test public void corruptOrUnboundedHashInputIsRejectedWithoutLegacyFallback() {
        String salt = "00000000000000000000000000000000";
        String hash = "0000000000000000000000000000000000000000000000000000000000000000";
        assertFalse(PasswordHasher.verify("demo", "pbkdf2-sha1$9999999$" + salt + "$" + hash).matches);
        assertFalse(PasswordHasher.verify("demo", "pbkdf2-sha1$120000$bad$bad").matches);
        assertFalse(PasswordHasher.verify("", "").matches);
        assertFalse(PasswordHasher.verify(null, "old").matches);
    }

    @Test public void calendarDatesRejectInvalidLeapDaysAndFutureBirths() {
        assertNull(MemberInput.validate("Siswa Demo", "Class A", "Male", "2024-02-29"));
        assertNull(MemberInput.validate("Siswa Demo", "Class A", "Male", "2020-12-31"));
        assertNotNull(MemberInput.validate("Siswa Demo", "Class A", "Male", "2023-02-29"));
        assertNotNull(MemberInput.validate("Siswa Demo", "Class A", "Male", "9999-01-01"));
        assertNotNull(MemberInput.validate("", "Class A", "Male", "2000-01-01"));
        assertNotNull(MemberInput.validate("Siswa Demo", "Class A", "", "2000-01-01"));
    }
}
